package com.my.conductorbootwrapper.controllers.rest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.my.conductorbootwrapper.config.QuartzConfig;
import com.my.conductorbootwrapper.constants.Constants;
import com.my.conductorbootwrapper.db.entities.ConductorQuartzExecutionHistory;
import com.my.conductorbootwrapper.db.entities.ConductorQuartzMapping;
import com.my.conductorbootwrapper.db.repositories.ConductorQuartzExecutionHistoryRepository;
import com.my.conductorbootwrapper.db.repositories.ConductorQuartzMappingRepository;
import com.my.conductorbootwrapper.dto.request.ScheduleConductorWorkflowExecutionRequest;
import com.my.conductorbootwrapper.dto.response.ScheduleConductorWorkflowExecutionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/quartz/scheduler/")
@Tag(name = "Quartz Conductor Workflow Scheduler Controller", description = "The Quartz Scheduler API")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ScheduleConductorWorkflowExecutionController {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    
    @Autowired
    ConductorQuartzMappingRepository conductorQuartzMappingRepository;
    
    @Autowired
    ConductorQuartzExecutionHistoryRepository conductorQuartzExecutionHistoryRepository;
    
    @Autowired
    Gson gson;
    
    @Operation(summary = "Schedule a Conductor Workflow Payload for execution", description = "Consumes the entire workflow trigger JSON and schedules it to the provided intervals", tags = { "schedule" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Scheduled Conductor Workflow Trigger Successfully", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ScheduleConductorWorkflowExecutionResponse.class)))) ,
        @ApiResponse(responseCode = "400", description = "Invalid Request Format", content = @Content()) ,
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@ResponseBody
    @PostMapping(value = "schedule", produces = "application/json")
    public ResponseEntity<ScheduleConductorWorkflowExecutionResponse> schedule(@Valid @RequestBody ScheduleConductorWorkflowExecutionRequest request) {
    
    	try {
    		String uniqueIdentifier = UUID.randomUUID().toString();
    		
    		DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
    		
    		ConductorQuartzMapping conductorQuartzMapping = new ConductorQuartzMapping();
    		
    		conductorQuartzMapping.setQuartzSchedulerId(uniqueIdentifier);
    		conductorQuartzMapping.setScheduleCronExpression(request.getCronExpression());
    		conductorQuartzMapping.setScheduleCurrentStatus(Constants.DRAFT);
    		conductorQuartzMapping.setScheduleName(request.getScheduleName());
    		conductorQuartzMapping.setScheduleVersion(request.getScheduleVersion());
    		conductorQuartzMapping.setScheduleDescription(request.getScheduleDescription());
    		conductorQuartzMapping.setWorkflowName(request.getWorkflowName());
    		conductorQuartzMapping.setWorkflowVersion(request.getWorkflowVersion());
    		conductorQuartzMapping.setScheduleWorkflowPayload(request.getJson().getBytes());
    		if(null!=request.getStartDate())
    			conductorQuartzMapping.setScheduleStartTimestamp(dateFormat.parse(request.getStartDate()));
    		conductorQuartzMapping.setScheduleStopTimestamp(dateFormat.parse(request.getEndDate()));
    		
    		conductorQuartzMapping = this.conductorQuartzMappingRepository.saveAndFlush(conductorQuartzMapping);
    		
    		JobDetail jobDetail = QuartzConfig.buildJobDetail(uniqueIdentifier, request);
            Trigger trigger = QuartzConfig.buildJobTrigger(request.getStartDate(), request.getEndDate(), jobDetail, request.getCronExpression());
            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
            
            if(schedulerFactoryBean.getScheduler().isShutdown())
            	schedulerFactoryBean.getScheduler().start();
            
            conductorQuartzMapping.setScheduleCurrentStatus(Constants.SCHEDULED);
            conductorQuartzMapping = this.conductorQuartzMappingRepository.saveAndFlush(conductorQuartzMapping);
    		
            return new ResponseEntity(new ScheduleConductorWorkflowExecutionResponse(uniqueIdentifier, Constants.SUCCESS), HttpStatus.OK);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping(value = "pause", produces = "application/json")
    @Operation(summary = "Pause a Conductor Workflow from execution", description = "Consumes the schedulerId and pauses it", tags = { "pause" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paused Conductor Workflow Schedule Successfully", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ScheduleConductorWorkflowExecutionResponse.class)))) ,
        @ApiResponse(responseCode = "400", description = "Invalid Request Format", content = @Content()) ,
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@ResponseBody
    public ResponseEntity<ScheduleConductorWorkflowExecutionResponse> pause(@RequestParam String scheduleId)
    {
    	try {
    		
    		ConductorQuartzMapping conductorQuartzMapping = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(scheduleId);
    		
    		if(null!=conductorQuartzMapping) {
    			
    			schedulerFactoryBean.getScheduler().pauseJob(new JobKey(scheduleId, Constants.JOB_IDENTIFIER_GROUP));
    			
    			conductorQuartzMapping.setScheduleCurrentStatus(Constants.PAUSED);
    			
    			conductorQuartzMapping = this.conductorQuartzMappingRepository.saveAndFlush(conductorQuartzMapping);

    			return new ResponseEntity(new ScheduleConductorWorkflowExecutionResponse(conductorQuartzMapping.getQuartzSchedulerId(), Constants.SUCCESS), HttpStatus.OK);
    		}
    		else
    		{
    			return new ResponseEntity(HttpStatus.BAD_REQUEST);
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        
    	return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping(value = "resume", produces = "application/json")
    @Operation(summary = "Resume a Conductor Workflow to execution", description = "Consumes the schedulerId and resumes it", tags = { "resume" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumed Conductor Workflow Schedule Successfully", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ScheduleConductorWorkflowExecutionResponse.class)))) ,
        @ApiResponse(responseCode = "400", description = "Invalid Request Format", content = @Content()) ,
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@ResponseBody
	public ResponseEntity<ScheduleConductorWorkflowExecutionResponse> resume(@RequestParam String scheduleId)
    {
    	try {
    		
    		ConductorQuartzMapping conductorQuartzMapping = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(scheduleId);
    		
    		if(null!=conductorQuartzMapping) {
    			
    			schedulerFactoryBean.getScheduler().resumeJob(new JobKey(scheduleId, Constants.JOB_IDENTIFIER_GROUP));
    			
    			conductorQuartzMapping.setScheduleCurrentStatus(Constants.SCHEDULED);
    			
    			conductorQuartzMapping = this.conductorQuartzMappingRepository.saveAndFlush(conductorQuartzMapping);

    			return new ResponseEntity(new ScheduleConductorWorkflowExecutionResponse(conductorQuartzMapping.getQuartzSchedulerId(), Constants.SUCCESS), HttpStatus.OK);
    		}
    		else
    		{
    			return new ResponseEntity(HttpStatus.BAD_REQUEST);
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        
    	return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping(value = "delete", produces = "application/json")
    @Operation(summary = "Delete a Conductor Workflow from execution", description = "Consumes the schedulerId and deletes it", tags = { "delete" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumed Conductor Workflow Schedule Successfully", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ScheduleConductorWorkflowExecutionResponse.class)))) ,
        @ApiResponse(responseCode = "400", description = "Invalid Request Format", content = @Content()) ,
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@ResponseBody
	public ResponseEntity<ScheduleConductorWorkflowExecutionResponse> deleteWorkflowExecution(@RequestParam String scheduleId)
    {
    	try {
    		ConductorQuartzMapping conductorQuartzMapping = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(scheduleId);
    		
    		if(null!=conductorQuartzMapping) {
    		
    			schedulerFactoryBean.getScheduler().deleteJob(new JobKey(scheduleId, Constants.JOB_IDENTIFIER_GROUP));
    			
    			this.conductorQuartzMappingRepository.delete(conductorQuartzMapping);
    			
    			return new ResponseEntity(new ScheduleConductorWorkflowExecutionResponse(scheduleId, Constants.SUCCESS), HttpStatus.OK);
    		}
    		else
    		{
    			return new ResponseEntity(HttpStatus.BAD_REQUEST);
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        
    	return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping(value = "get-all-schedules", produces = "application/json")
    @Operation(summary = "Gets all Conductor Workflow Schedules", description = "Provides the list of Quartz Schedules for Conductor Workflows", tags = { "get-all-schedules" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Obtained Conductor Workflow Schedules Successfully", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ConductorQuartzMapping[].class)))) ,
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@ResponseBody
	public ResponseEntity<List<ConductorQuartzMapping>> getAllSchedules()
    {
    	try {
    		
    		List<ConductorQuartzMapping> response = this.conductorQuartzMappingRepository.findAll();
    		
    		if(null!=response) {
    			
      			return new ResponseEntity(response, HttpStatus.OK);
    		}
    		else
    		{
    			return new ResponseEntity(HttpStatus.BAD_REQUEST);
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        
    	return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping(value = "get-schedule", produces = "application/json")
    @Operation(summary = "Gets a Conductor Workflow Schedule", description = "Consumes the schedulerId and provides details of it", tags = { "get-schedule" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Obtained Conductor Workflow Schedule Successfully", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ConductorQuartzMapping.class)))) ,
        @ApiResponse(responseCode = "400", description = "Invalid Request Format", content = @Content()) ,
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@ResponseBody
	public ResponseEntity<ConductorQuartzMapping> getSchedule(@RequestParam String scheduleId)
    {
    	try {
    		
    		ConductorQuartzMapping conductorQuartzMapping = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(scheduleId);
    		
    		if(null!=conductorQuartzMapping && conductorQuartzMapping.getMappingId() > 0) {
    			
      			return new ResponseEntity(conductorQuartzMapping, HttpStatus.OK);
    		}
    		else
    		{
    			return new ResponseEntity(HttpStatus.BAD_REQUEST);
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        
    	return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping(value = "get-schedule-history", produces = "application/json")
    @Operation(summary = "Gets a Conductor Workflow Schedule Execution History", description = "Consumes the schedulerId and provides details of it's execution history", tags = { "get-schedule-history" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Obtained Conductor Workflow Schedule Execution History Successfully", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ConductorQuartzExecutionHistory[].class)))) ,
        @ApiResponse(responseCode = "400", description = "Invalid Request Format", content = @Content()) ,
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@ResponseBody
	public ResponseEntity<List<ConductorQuartzExecutionHistory>> getScheduleHistory(@RequestParam String scheduleId)
    {
    	try {
    		
    		ConductorQuartzMapping conductorQuartzMapping = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(scheduleId);
    		
    		if(null!=conductorQuartzMapping) {
    			
    			List<ConductorQuartzExecutionHistory> response = this.conductorQuartzExecutionHistoryRepository.findByConductorQuartzMapping(conductorQuartzMapping);
    			
      			return new ResponseEntity(response, HttpStatus.OK);
    		}
    		else
    		{
    			return new ResponseEntity(HttpStatus.BAD_REQUEST);
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
        
    	return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}