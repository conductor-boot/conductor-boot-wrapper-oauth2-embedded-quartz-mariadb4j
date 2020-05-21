package com.my.conductorbootwrapper.dto.response;
import org.quartz.JobDetail;
import org.quartz.Trigger;

public class ScheduleDetailsResponse {

	private JobDetail jobDetail;
	private Trigger jobTrigger;
	
	public ScheduleDetailsResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ScheduleDetailsResponse(JobDetail jobDetail, Trigger jobTrigger) {
		super();
		this.jobDetail = jobDetail;
		this.jobTrigger = jobTrigger;
	}

	public JobDetail getJobDetail() {
		return jobDetail;
	}

	public void setJobDetail(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	public Trigger getJobTrigger() {
		return jobTrigger;
	}

	public void setJobTrigger(Trigger jobTrigger) {
		this.jobTrigger = jobTrigger;
	}

	@Override
	public String toString() {
		return "ScheduleDetailsResponse [jobDetail=" + jobDetail + ", jobTrigger=" + jobTrigger + "]";
	}
	
}