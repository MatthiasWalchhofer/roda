/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE file at the root of the source
 * tree and available online at
 *
 * https://github.com/keeps/roda
 */
package org.roda.core.plugins;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.roda.core.CorporaConstants;
import org.roda.core.RodaCoreFactory;
import org.roda.core.TestsHelper;
import org.roda.core.common.iterables.CloseableIterable;
import org.roda.core.common.monitor.TransferredResourcesScanner;
import org.roda.core.data.common.RodaConstants;
import org.roda.core.data.exceptions.AlreadyExistsException;
import org.roda.core.data.exceptions.AuthorizationDeniedException;
import org.roda.core.data.exceptions.GenericException;
import org.roda.core.data.exceptions.InvalidParameterException;
import org.roda.core.data.exceptions.IsStillUpdatingException;
import org.roda.core.data.exceptions.NotFoundException;
import org.roda.core.data.exceptions.RODAException;
import org.roda.core.data.exceptions.RequestNotValidException;
import org.roda.core.data.utils.IdUtils;
import org.roda.core.data.v2.common.OptionalWithCause;
import org.roda.core.data.v2.index.IndexResult;
import org.roda.core.data.v2.index.filter.Filter;
import org.roda.core.data.v2.index.filter.SimpleFilterParameter;
import org.roda.core.data.v2.index.select.SelectedItemsAll;
import org.roda.core.data.v2.index.select.SelectedItemsList;
import org.roda.core.data.v2.index.sort.Sorter;
import org.roda.core.data.v2.index.sublist.Sublist;
import org.roda.core.data.v2.ip.AIP;
import org.roda.core.data.v2.ip.DIP;
import org.roda.core.data.v2.ip.DIPFile;
import org.roda.core.data.v2.ip.File;
import org.roda.core.data.v2.ip.IndexedAIP;
import org.roda.core.data.v2.ip.IndexedDIP;
import org.roda.core.data.v2.ip.Permissions;
import org.roda.core.data.v2.ip.Representation;
import org.roda.core.data.v2.ip.StoragePath;
import org.roda.core.data.v2.ip.TransferredResource;
import org.roda.core.data.v2.jobs.Job;
import org.roda.core.data.v2.jobs.PluginType;
import org.roda.core.data.v2.jobs.Report;
import org.roda.core.index.IndexService;
import org.roda.core.model.ModelService;
import org.roda.core.model.utils.ModelUtils;
import org.roda.core.plugins.plugins.characterization.DigitalSignatureDIPPlugin;
import org.roda.core.plugins.plugins.characterization.DigitalSignatureDIPPluginUtils;
import org.roda.core.plugins.plugins.characterization.PDFSignatureUtils;
import org.roda.core.plugins.plugins.ingest.TransferredResourceToAIPPlugin;
import org.roda.core.storage.DirectResourceAccess;
import org.roda.core.storage.fs.FSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = {"all"})
public class DigitalSignatureDIPTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(DigitalSignatureDIPTest.class);

  private static Path basePath;
  private static ModelService model;
  private static IndexService index;
  private static Path corporaPath;

  @BeforeMethod
  public void setUp() throws Exception {
    basePath = TestsHelper.createBaseTempDir(this.getClass(), true);

    boolean deploySolr = true;
    boolean deployLdap = false;
    boolean deployFolderMonitor = true;
    boolean deployOrchestrator = true;
    boolean deployPluginManager = true;
    boolean deployDefaultResources = false;
    RodaCoreFactory.instantiateTest(deploySolr, deployLdap, deployFolderMonitor, deployOrchestrator,
      deployPluginManager, deployDefaultResources);
    model = RodaCoreFactory.getModelService();
    index = RodaCoreFactory.getIndexService();

    URL corporaURL = getClass().getResource("/corpora");
    corporaPath = Paths.get(corporaURL.toURI());

    LOGGER.info("Running internal convert plugins tests under storage {}", basePath);
  }

  @AfterMethod
  public void tearDown() throws Exception {
    RodaCoreFactory.shutdown();
    FSUtils.deletePath(basePath);
  }

  public List<TransferredResource> createCorpora() throws InterruptedException, IOException, FileAlreadyExistsException,
    NotFoundException, GenericException, AlreadyExistsException, SolrServerException, IsStillUpdatingException {
    TransferredResourcesScanner f = RodaCoreFactory.getTransferredResourcesScanner();

    List<TransferredResource> resources = new ArrayList<TransferredResource>();

    Path corpora = corporaPath.resolve(RodaConstants.STORAGE_CONTAINER_AIP)
      .resolve(CorporaConstants.SOURCE_AIP_CONVERTER_3).resolve(RodaConstants.STORAGE_DIRECTORY_REPRESENTATIONS)
      .resolve(CorporaConstants.REPRESENTATION_CONVERTER_ID_3).resolve(RodaConstants.STORAGE_DIRECTORY_DATA);

    String transferredResourceId = "testt";
    FSUtils.copy(corpora, f.getBasePath().resolve(transferredResourceId), true);

    f.updateTransferredResources(Optional.empty(), true);
    index.commit(TransferredResource.class);

    resources
      .add(index.retrieve(TransferredResource.class, IdUtils.createUUID(transferredResourceId), new ArrayList<>()));
    return resources;
  }

  public AIP ingestCorpora() throws RequestNotValidException, NotFoundException, GenericException,
    AlreadyExistsException, AuthorizationDeniedException, InvalidParameterException, InterruptedException, IOException,
    FileAlreadyExistsException, SolrServerException, IsStillUpdatingException {
    String parentId = null;
    String aipType = RodaConstants.AIP_TYPE_MIXED;
    AIP root = model.createAIP(parentId, aipType, new Permissions(), RodaConstants.ADMIN);

    Map<String, String> parameters = new HashMap<>();
    parameters.put(RodaConstants.PLUGIN_PARAMS_PARENT_ID, root.getId());

    List<TransferredResource> transferredResources = new ArrayList<TransferredResource>();
    transferredResources = createCorpora();

    AssertJUnit.assertEquals(1, transferredResources.size());

    Job job = TestsHelper.executeJob(TransferredResourceToAIPPlugin.class, parameters, PluginType.SIP_TO_AIP,
      SelectedItemsList.create(TransferredResource.class,
        transferredResources.stream().map(tr -> tr.getUUID()).collect(Collectors.toList())));

    TestsHelper.getJobReports(index, job, true);

    index.commitAIPs();

    IndexResult<IndexedAIP> find = index.find(IndexedAIP.class,
      new Filter(new SimpleFilterParameter(RodaConstants.AIP_PARENT_ID, root.getId())), null, new Sublist(0, 10),
      new ArrayList<>());

    AssertJUnit.assertEquals(1L, find.getTotalCount());
    IndexedAIP indexedAIP = find.getResults().get(0);

    return model.retrieveAIP(indexedAIP.getId());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testDigitalSignatureDIPPlugin() throws RODAException, FileAlreadyExistsException, InterruptedException,
    IOException, SolrServerException, IsStillUpdatingException {
    AIP aip = ingestCorpora();
    Representation rep = aip.getRepresentations().get(0);
    CloseableIterable<OptionalWithCause<File>> allFiles = model.listFilesUnder(aip.getId(), rep.getId(), true);
    OptionalWithCause<File> file = allFiles.iterator().next();

    StoragePath fileStoragePath = ModelUtils.getFileStoragePath(file.get());
    DirectResourceAccess directAccess = model.getStorage().getDirectAccess(fileStoragePath);
    AssertJUnit.assertEquals(0, PDFSignatureUtils.countSignaturesPDF(directAccess.getPath()));
    IOUtils.closeQuietly(directAccess);

    Map<String, String> parameters = new HashMap<>();
    DigitalSignatureDIPPluginUtils.setKeystorePath(corporaPath.toString() + "/Certification/keystore.jks");

    Job job = TestsHelper.executeJob(DigitalSignatureDIPPlugin.class, parameters, PluginType.AIP_TO_AIP,
      SelectedItemsAll.create(Representation.class));

    List<Report> jobReports = TestsHelper.getJobReports(index, job, true);
    index.commit(IndexedDIP.class);

    for (Report report : jobReports) {
      if (report.getSourceObjectId().equals(aip.getRepresentations().get(0))) {
        String repUUID = IdUtils.getRepresentationId(rep);
        IndexResult<IndexedDIP> dips = index.find(IndexedDIP.class,
          new Filter(new SimpleFilterParameter(RodaConstants.DIP_REPRESENTATION_UUIDS, repUUID)), Sorter.NONE,
          new Sublist(0, 1), new ArrayList<>());
        DIP dip = dips.getResults().get(0);
        IndexResult<DIPFile> files = index.find(DIPFile.class,
          new Filter(new SimpleFilterParameter(RodaConstants.DIPFILE_DIP_ID, dip.getId())), Sorter.NONE,
          new Sublist(0, 1), new ArrayList<>());
        DIPFile dipfile = files.getResults().get(0);

        StoragePath newFileStoragePath = ModelUtils.getDIPFileStoragePath(dipfile);
        DirectResourceAccess newDirectAccess = model.getStorage().getDirectAccess(newFileStoragePath);
        AssertJUnit.assertEquals(1, PDFSignatureUtils.countSignaturesPDF(newDirectAccess.getPath()));
        IOUtils.closeQuietly(newDirectAccess);
      }
    }
  }

}
