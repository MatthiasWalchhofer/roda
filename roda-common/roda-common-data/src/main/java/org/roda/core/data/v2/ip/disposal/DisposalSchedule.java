package org.roda.core.data.v2.ip.disposal;

import java.util.Date;
import java.util.Objects;

import org.roda.core.data.common.RodaConstants;
import org.roda.core.data.v2.IsModelObject;
import org.roda.core.data.v2.ip.HasId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Miguel Guimarães <mguimaraes@keep.pt>
 */
@javax.xml.bind.annotation.XmlRootElement(name = RodaConstants.RODA_OBJECT_DISPOSAL_SCHEDULE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisposalSchedule implements IsModelObject, HasId {
  private static final long serialVersionUID = -2870778207871507847L;
  private static final int VERSION = 1;

  private String id;
  private String title;
  private String description;
  // describes the authority and jurisdiction under which the disposal schedule
  // operates
  private String mandate;
  // provide additional information to users on how the disposal schedule should
  // be interpreted and applied
  private String scopeNotes;

  private DisposalActionCode actionCode;
  private RetentionTriggerCode retentionTriggerCode;
  private String retentionTriggerElementId;
  private RetentionPeriodIntervalCode retentionPeriodIntervalCode;
  private Integer retentionPeriodDuration;

  // If any AIP was destroyed by this disposal schedule the date should be set to
  // prevent this disposal from being deleted
  private Date destroyedTimestamp = null;

  private Long numberOfAIPUnder;

  private Date createdOn = null;
  private String createdBy = null;
  private Date updatedOn = null;
  private String updatedBy = null;

  public DisposalSchedule() {
    super();
  }

  public DisposalSchedule(String id, String title, String description, String mandate, String scopeNotes,
    DisposalActionCode actionCode, RetentionTriggerCode retentionTriggerCode, String retentionTriggerElementId,
    RetentionPeriodIntervalCode retentionPeriodIntervalCode, Integer retentionPeriodDuration, Date createdOn,
    String createdBy) {
    super();
    this.id = id;
    this.title = title;
    this.description = description;
    this.mandate = mandate;
    this.scopeNotes = scopeNotes;
    this.actionCode = actionCode;
    this.retentionTriggerCode = retentionTriggerCode;
    this.retentionTriggerElementId = retentionTriggerElementId;
    this.retentionPeriodIntervalCode = retentionPeriodIntervalCode;
    this.retentionPeriodDuration = retentionPeriodDuration;

    this.createdOn = createdOn;
    this.createdBy = createdBy;
    this.updatedOn = createdOn;
    this.updatedBy = createdBy;
  }

  @JsonIgnore
  @Override
  public int getClassVersion() {
    return VERSION;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMandate() {
    return mandate;
  }

  public void setMandate(String mandate) {
    this.mandate = mandate;
  }

  public String getScopeNotes() {
    return scopeNotes;
  }

  public void setScopeNotes(String scopeNotes) {
    this.scopeNotes = scopeNotes;
  }

  public DisposalActionCode getActionCode() {
    return actionCode;
  }

  public void setActionCode(DisposalActionCode actionCode) {
    this.actionCode = actionCode;
  }

  public RetentionTriggerCode getRetentionTriggerCode() {
    return retentionTriggerCode;
  }

  public void setRetentionTriggerCode(RetentionTriggerCode retentionTriggerCode) {
    this.retentionTriggerCode = retentionTriggerCode;
  }

  public String getRetentionTriggerElementId() {
    return retentionTriggerElementId;
  }

  public void setRetentionTriggerElementId(String retentionTriggerElementId) {
    this.retentionTriggerElementId = retentionTriggerElementId;
  }

  public RetentionPeriodIntervalCode getRetentionPeriodIntervalCode() {
    return retentionPeriodIntervalCode;
  }

  public void setRetentionPeriodIntervalCode(RetentionPeriodIntervalCode retentionPeriodIntervalCode) {
    this.retentionPeriodIntervalCode = retentionPeriodIntervalCode;
  }

  public Integer getRetentionPeriodDuration() {
    return retentionPeriodDuration;
  }

  public void setRetentionPeriodDuration(Integer retentionPeriodDuration) {
    this.retentionPeriodDuration = retentionPeriodDuration;
  }

  public Date getDestroyedTimestamp() {
    return destroyedTimestamp;
  }

  public void setDestroyedTimestamp(Date destroyedTimestamp) {
    this.destroyedTimestamp = destroyedTimestamp;
  }

  public Long getNumberOfAIPUnder() {
    return numberOfAIPUnder;
  }

  public void setNumberOfAIPUnder(Long numberOfAIPUnder) {
    this.numberOfAIPUnder = numberOfAIPUnder;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Date getUpdatedOn() {
    return updatedOn;
  }

  public void setUpdatedOn(Date updatedOn) {
    this.updatedOn = updatedOn;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  @JsonIgnore
  public void incrementNumberOfAIPs(int number) {
    this.numberOfAIPUnder += number;
  }

  @JsonIgnore
  public void decreaseNumberOfAIPs(int number) {
    this.numberOfAIPUnder -= number;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DisposalSchedule schedule = (DisposalSchedule) o;
    return Objects.equals(getId(), schedule.getId()) && Objects.equals(getTitle(), schedule.getTitle())
      && Objects.equals(getDescription(), schedule.getDescription())
      && Objects.equals(getMandate(), schedule.getMandate())
      && Objects.equals(getScopeNotes(), schedule.getScopeNotes()) && getActionCode() == schedule.getActionCode()
      && getRetentionTriggerCode() == schedule.getRetentionTriggerCode()
      && Objects.equals(getRetentionTriggerElementId(), schedule.getRetentionTriggerElementId())
      && getRetentionPeriodIntervalCode() == schedule.getRetentionPeriodIntervalCode()
      && Objects.equals(getRetentionPeriodDuration(), schedule.getRetentionPeriodDuration())
      && Objects.equals(getDestroyedTimestamp(), schedule.getDestroyedTimestamp())
      && Objects.equals(getNumberOfAIPUnder(), schedule.getNumberOfAIPUnder())
      && Objects.equals(getCreatedOn(), schedule.getCreatedOn())
      && Objects.equals(getCreatedBy(), schedule.getCreatedBy())
      && Objects.equals(getUpdatedOn(), schedule.getUpdatedOn())
      && Objects.equals(getUpdatedBy(), schedule.getUpdatedBy());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTitle(), getDescription(), getMandate(), getScopeNotes(), getActionCode(),
      getRetentionTriggerCode(), getRetentionTriggerElementId(), getRetentionPeriodIntervalCode(),
      getRetentionPeriodDuration(), getDestroyedTimestamp(), getNumberOfAIPUnder(), getCreatedOn(), getCreatedBy(),
      getUpdatedOn(), getUpdatedBy());
  }

  @Override
  public String toString() {
    return "DisposalSchedule{" + "id='" + id + '\'' + ", title='" + title + '\'' + ", description='" + description
      + '\'' + ", mandate='" + mandate + '\'' + ", scopeNotes='" + scopeNotes + '\'' + ", actionCode=" + actionCode
      + ", retentionTriggerCode=" + retentionTriggerCode + ", retentionTriggerElementId='" + retentionTriggerElementId
      + '\'' + ", retentionPeriodIntervalCode=" + retentionPeriodIntervalCode + ", retentionPeriodDuration="
      + retentionPeriodDuration + ", destroyedTimestamp=" + destroyedTimestamp + ", numberOfAIPUnder="
      + numberOfAIPUnder + ", createdOn=" + createdOn + ", createdBy='" + createdBy + '\'' + ", updatedOn=" + updatedOn
      + ", updatedBy='" + updatedBy + '\'' + '}';
  }
}