package org.roda.index;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.roda.common.monitor.FolderObserver;
import org.roda.core.common.RodaConstants;
import org.roda.index.utils.SolrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexFolderObserver implements FolderObserver {

  private static final Logger LOGGER = LoggerFactory.getLogger(IndexFolderObserver.class);

  private final SolrClient index;

  public IndexFolderObserver(SolrClient index, Path basePath) throws SolrServerException, IOException {
    super();
    this.index = index;
    clearIndex();
    indexPath(basePath);
  }

  private void indexPath(Path basePath) throws SolrServerException, IOException {
    Files.walkFileTree(basePath, new FileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        pathAdded(basePath, file);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
      }
    });
  }

  private void clearIndex() throws SolrServerException, IOException {
    index.deleteByQuery(RodaConstants.INDEX_SIP, "*:*");
    index.commit(RodaConstants.INDEX_SIP);

  }

  public void pathAddedSimple(Path basePath, Path createdPath, boolean addParents) throws IOException {
    try {
      Path relativePath = basePath.relativize(createdPath);
      if (relativePath.getNameCount() > 1) {
        SolrInputDocument pathDocument = SolrUtils.transferredResourceToSolrDocument(createdPath, relativePath);
        index.add(RodaConstants.INDEX_SIP, pathDocument);
        if (addParents) {
          Path parentPath = createdPath.getParent();
          while (!Files.isSameFile(basePath, parentPath)) {
            pathAddedSimple(basePath, parentPath, false);
            parentPath = parentPath.getParent();
          }
        }
      }
    } catch (SolrServerException | IOException e) {
      LOGGER.error("Could not commitbasePath, pathCreated indexed path to SIPMonitor index: " + e.getMessage(), e);
    } catch (Throwable t) {
      LOGGER.error("ERROR: " + t.getMessage(), t);
    }
  }

  @Override
  public void pathAdded(Path basePath, Path createdPath) {
    LOGGER.debug("ADDED: " + createdPath);
    try {
      Path relativePath = basePath.relativize(createdPath);

      if (relativePath.getNameCount() > 1) {
        SolrInputDocument pathDocument = SolrUtils.transferredResourceToSolrDocument(createdPath, relativePath);
        Path parentPath = createdPath.getParent();
        while (!Files.isSameFile(basePath, parentPath)) {
          pathAddedSimple(basePath, parentPath, false);
          parentPath = parentPath.getParent();
        }

        if (createdPath.toFile().isDirectory()) {
          Files.walkFileTree(createdPath, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
              pathAddedSimple(basePath, file, true);
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
              return FileVisitResult.CONTINUE;
            }
          });
        }

        index.add(RodaConstants.INDEX_SIP, pathDocument);
        index.commit(RodaConstants.INDEX_SIP);
      }
    } catch (IOException | SolrServerException e) {
      LOGGER.error("Error adding path to SIPMonitorIndex: " + e.getMessage(), e);
    } catch (Throwable t) {
      LOGGER.error("ERROR: " + t.getMessage(), t);
    }
  }

  @Override
  public void pathModified(Path basePath, Path modifiedPath) {
    LOGGER.debug("MODIFIED: " + modifiedPath);
    pathAdded(basePath, modifiedPath);
  }

  @Override
  public void pathDeleted(Path basePath, Path deletedPath) {
    LOGGER.debug("DELETED: " + deletedPath);
    try {
      Path relativePath = basePath.relativize(deletedPath);
      if (relativePath.getNameCount() > 1) {
        index.deleteById(RodaConstants.INDEX_SIP, relativePath.toString().replaceAll("\\s+", ""));
        index.deleteByQuery(RodaConstants.INDEX_SIP, "id:" + relativePath.toString().replaceAll("\\s+", "") + "*");
        index.commit(RodaConstants.INDEX_SIP);
      }
    } catch (IOException | SolrServerException e) {
      LOGGER.error("Error deleting path to SIPMonitorIndex: " + e.getMessage(), e);
    } catch (Throwable t) {
      LOGGER.error("ERROR: " + t.getMessage(), t);
    }
  }
}
