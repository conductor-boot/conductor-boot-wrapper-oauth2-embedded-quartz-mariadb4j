package com.my.conductorbootwrapper.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.my.conductorbootwrapper.constants.Constants;
import com.my.conductorbootwrapper.dto.request.ScheduleConductorWorkflowExecutionRequest;
import com.my.conductorbootwrapper.jobs.ScheduleConductorWorkflowExecutionJob;

public class QuartzConfig {

	 public static JobDetail buildJobDetail(String uniqueIdentifier, ScheduleConductorWorkflowExecutionRequest scheduleConductorWorkflowExecutionRequest) {
	        

	        JobDataMap jobDataMap = new JobDataMap();

	        jobDataMap.put(Constants.JSON, scheduleConductorWorkflowExecutionRequest.getJson());
	        
	        return JobBuilder.newJob(ScheduleConductorWorkflowExecutionJob.class)
	                .withIdentity(uniqueIdentifier, Constants.JOB_IDENTIFIER_GROUP)
	                .withDescription(Constants.JOB_IDENTIFIER_GROUP_DESCRIPTION)
	                .usingJobData(jobDataMap)
	                .storeDurably()
	                .build();
	    }

	    public static Trigger buildJobTrigger(String startDate, String endDate, JobDetail jobDetail, String cronExpression) {
	       try {
		    	   Date endAt = new SimpleDateFormat(Constants.DATE_FORMAT).parse(endDate);
		    	   if(null!=startDate)
		           {
		           	Date startAt = new SimpleDateFormat(Constants.DATE_FORMAT).parse(startDate);
		
		               
		           	return TriggerBuilder.newTrigger()
		                       .forJob(jobDetail)
		                       .withIdentity(jobDetail.getKey().getName(), Constants.WORKFLOW_TRIGGERS)
		                       .startAt(startAt)
		                       .endAt(endAt)
		                       .withDescription(Constants.SEND_WORKFLOW_TRIGGER)
		                       .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
		                       .build();
		           }
		           else
		           {
		           	return TriggerBuilder.newTrigger()
		                       .forJob(jobDetail)
		                       .withIdentity(jobDetail.getKey().getName(), Constants.WORKFLOW_TRIGGERS)
		                       .startNow()
		                       .endAt(endAt)
		                       .withDescription(Constants.SEND_WORKFLOW_TRIGGER)
		                       .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
		                       .build();
		           }
	       }
	       catch(Exception e)
	       {
	    	   e.printStackTrace();
	    	   return TriggerBuilder.newTrigger()
	                   .forJob(jobDetail)
	                   .withIdentity(jobDetail.getKey().getName(), Constants.WORKFLOW_TRIGGERS)
	                   .withDescription(Constants.SEND_WORKFLOW_TRIGGER)
	                   .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
	                   .build();
	       }
	    }
}
