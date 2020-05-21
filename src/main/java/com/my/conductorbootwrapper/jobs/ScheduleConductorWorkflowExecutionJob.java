package com.my.conductorbootwrapper.jobs;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.my.conductorbootwrapper.constants.Constants;
import com.my.conductorbootwrapper.db.entities.ConductorQuartzExecutionHistory;
import com.my.conductorbootwrapper.db.entities.ConductorQuartzMapping;
import com.my.conductorbootwrapper.db.repositories.ConductorQuartzExecutionHistoryRepository;
import com.my.conductorbootwrapper.db.repositories.ConductorQuartzMappingRepository;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ScheduleConductorWorkflowExecutionJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleConductorWorkflowExecutionJob.class);

    @Autowired
    RestTemplate restTemplate;
    
    private String runLog = Constants.DEFAULT_STRING_INITIALIZOR;
    
    @Value("${conductor.server.api.url:'http://localhost:8080/api/'}")
    private String CONDUCTOR_SERVER_API_URL;
    
    @Autowired
    ConductorQuartzMappingRepository conductorQuartzMappingRepository;
    
    @Autowired
    ConductorQuartzExecutionHistoryRepository conductorQuartzExecutionHistoryRepository;
    
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        
        this.runLog = Constants.DEFAULT_STRING_INITIALIZOR;
        
        
        String schedulerId = jobExecutionContext.getJobDetail().getKey().toString();
        
        logger.info("SchedulerId: "+schedulerId);
        
        ConductorQuartzMapping conductorQuartzMapping = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(schedulerId);
        ConductorQuartzExecutionHistory conductorQuartzExecutionHistory = new ConductorQuartzExecutionHistory();
        
        if(null!=conductorQuartzMapping)
        {
        	conductorQuartzExecutionHistory.setConductorQuartzMapping(conductorQuartzMapping);
        	conductorQuartzExecutionHistory.setQuartzExecutionStatus(false);
        	
        	conductorQuartzExecutionHistory = this.conductorQuartzExecutionHistoryRepository.saveAndFlush(conductorQuartzExecutionHistory);
        }

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String json = jobDataMap.getString(Constants.JSON);

        String conductorCorrelationId = triggerWorkflow(json);
        
        if(conductorQuartzExecutionHistory.getHistoryId() > 0 && null!=conductorCorrelationId)
        {
        	conductorQuartzExecutionHistory.setQuartzExecutionStatus(true);
        	conductorQuartzExecutionHistory.setConductorCorrelationId(conductorCorrelationId);
        	
        	conductorQuartzExecutionHistory = this.conductorQuartzExecutionHistoryRepository.saveAndFlush(conductorQuartzExecutionHistory);
        }
        else if(conductorQuartzExecutionHistory.getHistoryId() > 0 )
        {
        	conductorQuartzExecutionHistory.setQuartzExecutionLog(this.runLog.substring(0, 500));
        	conductorQuartzExecutionHistory = this.conductorQuartzExecutionHistoryRepository.saveAndFlush(conductorQuartzExecutionHistory);
        }
    }
    
    private String triggerWorkflow(String requestJson)
	{
		
		  logger.info(">> triggerWorkflow()\n"+requestJson);
		  
		  try {
		  
		  HttpHeaders headers = new HttpHeaders(); headers.setContentType(
		  MediaType.APPLICATION_JSON );
		  
		  HttpEntity request= new HttpEntity( requestJson, headers );
		  
		 String triggerWorkflowResponse = restTemplate.postForObject(
				 CONDUCTOR_SERVER_API_URL+"workflow", request, String.class );
		  
		  logger.info("triggerWorkflowResponse: "+triggerWorkflowResponse);

		  logger.info("<< triggerWorkflow()");
		  return triggerWorkflowResponse;
		   
		  } catch(HttpClientErrorException e) {
		  if(e.getStatusCode().compareTo(HttpStatus.CONFLICT) == 0) {
		  logger.warn(e.getMessage());
		  } else {
			  logger.error(e.getMessage());
		  }
		  
		  e.printStackTrace();
		  this.runLog = e.getMessage();
		  }
		  
		  logger.info("<< triggerWorkflow()");
		  return null;
		 }
}