/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE file at the root of the source
 * tree and available online at
 *
 * https://github.com/keeps/roda
 */
package org.roda.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.roda.core.data.v2.PluginType;

/**
 * This class contains information about a plugin.
 * 
 * @author Rui Castro
 */
public class PluginInfo implements Serializable {
  private static final long serialVersionUID = -7977449299190436877L;

  /**
   * The ID is the plugin classname.
   */
  private String id = null;

  private String name = null;

  private String version = null;

  private String description = null;

  private PluginType type = PluginType.MISC;

  private List<PluginParameter> parameters = new ArrayList<PluginParameter>();

  /**
   * Constructs a new {@link PluginInfo}.
   */
  public PluginInfo() {
  }

  /**
   * Constructs a new {@link PluginInfo} cloning an existing {@link PluginInfo}.
   * 
   * @param pluginInfo
   *          the {@link PluginInfo} to clone.
   */
  public PluginInfo(PluginInfo pluginInfo) {
    this(pluginInfo.getId(), pluginInfo.getName(), pluginInfo.getVersion(), pluginInfo.getDescription(),
      pluginInfo.getType(), pluginInfo.getParameters());
  }

  /**
   * Constructs a new {@link PluginInfo} with the given parametes.
   * 
   * @param id
   * @param name
   * @param version
   * @param description
   * @param parameters
   */
  public PluginInfo(String id, String name, String version, String description, PluginType type,
    List<PluginParameter> parameters) {
    setId(id);
    setName(name);
    setVersion(version);
    setDescription(description);
    setType(type);
    setParameters(parameters);
  }

  /**
   * @see Object#equals(Object)
   */
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof PluginInfo) {
      PluginInfo other = (PluginInfo) obj;
      return getId().equals(other.getId());
    } else {
      return false;
    }
  }

  /**
   * @see Object#toString()
   */
  @Override
  public String toString() {
    return "PluginInfo [id=" + id + ", name=" + name + ", version=" + version + ", description=" + description
      + ", type=" + type + ", parameters=" + parameters + "]";
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the version
   */
  public String getVersion() {
    return version;
  }

  /**
   * @param version
   *          the version to set
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description
   *          the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  public PluginType getType() {
    return type;
  }

  public void setType(PluginType type) {
    this.type = type;
  }

  /**
   * @return the parameters
   */
  public List<PluginParameter> getParameters() {
    return parameters;
  }

  /**
   * @param parameters
   *          the parameters to set
   */
  public void setParameters(List<PluginParameter> parameters) {
    this.parameters.clear();
    if (parameters != null) {
      this.parameters.addAll(parameters);
    }
  }

}
