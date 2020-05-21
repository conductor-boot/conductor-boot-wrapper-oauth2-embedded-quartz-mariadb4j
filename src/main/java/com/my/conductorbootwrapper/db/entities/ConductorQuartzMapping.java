package com.my.conductorbootwrapper.db.entities;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.Gson;

@Entity
@Table(name="conductor_quartz_mapping")
public class ConductorQuartzMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "mapping_id", updatable = false, nullable = false)
	private Long mappingId;
	
	@Column (name = "quartz_scheduler_id", length = 150, nullable = true, unique = true)
	private String quartzSchedulerId;
	
	@Column (name = "schedule_name", length = 100, nullable = false, unique = true)
	private String scheduleName;
	
	@Column (name = "schedule_version")
	private int scheduleVersion;
	
	@Column (name = "schedule_description", length = 500, nullable = true, unique = false)
	private String scheduleDescription;
	
	@Column (name = "workflow_name", length = 150, nullable = false, unique = false)
	private String workflowName;
	
	@Column (name = "workflow_version")
	private int workflowVersion;
	
	@Lob
	@Column (name = "schedule_workflow_payload", length = 100000, nullable = false)
	private byte[] scheduleWorkflowPayload;
	
	@Column (name = "schedule_cron_expression", length = 45, nullable = false, unique = false)
	private String scheduleCronExpression;
	
	@Column (name = "schedule_current_status", length = 45, nullable = false, unique = false)
	private String scheduleCurrentStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "schedule_start_timestamp", nullable = true)
	private Date scheduleStartTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "schedule_end_timestamp", nullable = false)
	private Date scheduleStopTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "insert_timestamp", nullable = false)
	private Date insertTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_timestamp", nullable = false)
	private Date updateTimestamp;
	
	@PrePersist
	protected void onCreate() {
		updateTimestamp = insertTimestamp = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updateTimestamp = new Date();
	}

	public ConductorQuartzMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public ConductorQuartzMapping(Long mappingId, String quartzSchedulerId, String scheduleName, int scheduleVersion,
			String scheduleDescription, String workflowName, int workflowVersion, byte[] scheduleWorkflowPayload,
			String scheduleCronExpression, String scheduleCurrentStatus, Date scheduleStartTimestamp,
			Date scheduleStopTimestamp, Date insertTimestamp, Date updateTimestamp) {
		super();
		this.mappingId = mappingId;
		this.quartzSchedulerId = quartzSchedulerId;
		this.scheduleName = scheduleName;
		this.scheduleVersion = scheduleVersion;
		this.scheduleDescription = scheduleDescription;
		this.workflowName = workflowName;
		this.workflowVersion = workflowVersion;
		this.scheduleWorkflowPayload = scheduleWorkflowPayload;
		this.scheduleCronExpression = scheduleCronExpression;
		this.scheduleCurrentStatus = scheduleCurrentStatus;
		this.scheduleStartTimestamp = scheduleStartTimestamp;
		this.scheduleStopTimestamp = scheduleStopTimestamp;
		this.insertTimestamp = insertTimestamp;
		this.updateTimestamp = updateTimestamp;
	}

	public Long getMappingId() {
		return mappingId;
	}

	public void setMappingId(Long mappingId) {
		this.mappingId = mappingId;
	}

	public String getQuartzSchedulerId() {
		return quartzSchedulerId;
	}

	public void setQuartzSchedulerId(String quartzSchedulerId) {
		this.quartzSchedulerId = quartzSchedulerId;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public int getScheduleVersion() {
		return scheduleVersion;
	}

	public void setScheduleVersion(int scheduleVersion) {
		this.scheduleVersion = scheduleVersion;
	}

	public String getScheduleDescription() {
		return scheduleDescription;
	}

	public void setScheduleDescription(String scheduleDescription) {
		this.scheduleDescription = scheduleDescription;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public int getWorkflowVersion() {
		return workflowVersion;
	}

	public void setWorkflowVersion(int workflowVersion) {
		this.workflowVersion = workflowVersion;
	}

	public byte[] getScheduleWorkflowPayload() {
		return scheduleWorkflowPayload;
	}

	public void setScheduleWorkflowPayload(byte[] scheduleWorkflowPayload) {
		this.scheduleWorkflowPayload = scheduleWorkflowPayload;
	}

	public String getScheduleCronExpression() {
		return scheduleCronExpression;
	}

	public void setScheduleCronExpression(String scheduleCronExpression) {
		this.scheduleCronExpression = scheduleCronExpression;
	}

	public String getScheduleCurrentStatus() {
		return scheduleCurrentStatus;
	}

	public void setScheduleCurrentStatus(String scheduleCurrentStatus) {
		this.scheduleCurrentStatus = scheduleCurrentStatus;
	}

	public Date getScheduleStartTimestamp() {
		return scheduleStartTimestamp;
	}

	public void setScheduleStartTimestamp(Date scheduleStartTimestamp) {
		this.scheduleStartTimestamp = scheduleStartTimestamp;
	}

	public Date getScheduleStopTimestamp() {
		return scheduleStopTimestamp;
	}

	public void setScheduleStopTimestamp(Date scheduleStopTimestamp) {
		this.scheduleStopTimestamp = scheduleStopTimestamp;
	}

	public Date getInsertTimestamp() {
		return insertTimestamp;
	}

	public void setInsertTimestamp(Date insertTimestamp) {
		this.insertTimestamp = insertTimestamp;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	@Override
	public String toString() {
		return "ConductorQuartzMapping [mappingId=" + mappingId + ", quartzSchedulerId=" + quartzSchedulerId
				+ ", scheduleName=" + scheduleName + ", scheduleVersion=" + scheduleVersion + ", scheduleDescription="
				+ scheduleDescription + ", workflowName=" + workflowName + ", workflowVersion=" + workflowVersion
				+ ", scheduleWorkflowPayload=" + Arrays.toString(scheduleWorkflowPayload) + ", scheduleCronExpression="
				+ scheduleCronExpression + ", scheduleCurrentStatus=" + scheduleCurrentStatus
				+ ", scheduleStartTimestamp=" + scheduleStartTimestamp + ", scheduleStopTimestamp="
				+ scheduleStopTimestamp + ", insertTimestamp=" + insertTimestamp + ", updateTimestamp="
				+ updateTimestamp + "]";
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
