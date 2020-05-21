package com.my.conductorbootwrapper.dto.request;
import javax.validation.constraints.NotEmpty;

import org.springframework.lang.Nullable;

import com.google.gson.Gson;

public class ScheduleConductorWorkflowExecutionRequest {
	
	@NotEmpty
	private String workflowName;
	
	private int workflowVersion;
	
	@NotEmpty
	private String scheduleName;
	
	private int scheduleVersion;
	
	@Nullable
	private String scheduleDescription;

    @NotEmpty
    private String json;
    
    @NotEmpty
    private String cronExpression;
    
    @Nullable
    private String startDate;
    
    @NotEmpty
    private String endDate;

	public ScheduleConductorWorkflowExecutionRequest(@NotEmpty String workflowName, int workflowVersion,
			@NotEmpty String scheduleName, int scheduleVersion, String scheduleDescription, @NotEmpty String json,
			@NotEmpty String cronExpression, String startDate, @NotEmpty String endDate) {
		super();
		this.workflowName = workflowName;
		this.workflowVersion = workflowVersion;
		this.scheduleName = scheduleName;
		this.scheduleVersion = scheduleVersion;
		this.scheduleDescription = scheduleDescription;
		this.json = json;
		this.cronExpression = cronExpression;
		this.startDate = startDate;
		this.endDate = endDate;
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

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getScheduleDescription() {
		return scheduleDescription;
	}

	public void setScheduleDescription(String scheduleDescription) {
		this.scheduleDescription = scheduleDescription;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getScheduleVersion() {
		return scheduleVersion;
	}

	public void setScheduleVersion(int scheduleVersion) {
		this.scheduleVersion = scheduleVersion;
	}

	@Override
	public String toString() {
		return "ScheduleConductorWorkflowExecutionRequest [workflowName=" + workflowName + ", workflowVersion="
				+ workflowVersion + ", scheduleName=" + scheduleName + ", scheduleVersion=" + scheduleVersion
				+ ", scheduleDescription=" + scheduleDescription + ", json=" + json + ", cronExpression="
				+ cronExpression + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}