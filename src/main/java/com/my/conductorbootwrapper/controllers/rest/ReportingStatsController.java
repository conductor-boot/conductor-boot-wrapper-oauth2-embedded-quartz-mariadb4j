package com.my.conductorbootwrapper.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.my.conductorbootwrapper.db.entities.SchedulerPortfolioStats;
import com.my.conductorbootwrapper.db.repositories.ConductorQuartzExecutionHistoryRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/quartz/reports/")
@Tag(name = "Quartz Conductor Workflow Scheduler Reports Controller", description = "The Quartz Scheduler API for Reports")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportingStatsController {
	
	@Autowired
    ConductorQuartzExecutionHistoryRepository conductorQuartzExecutionHistoryRepository;
	
	@GetMapping(value = "report", produces = "application/json")
    @Operation(summary = "Gets Stats of Scheduled Workflows", description = "Gives a Total, Success, Failure count and Success Percentage detail", tags = { "report" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Obtained Conductor Workflow Schedule Stats Successfully", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = SchedulerPortfolioStats.class)))) ,
        @ApiResponse(responseCode = "500", description = "Internal Server Error - Returned when an unexpected error occurs on server side", content = @Content())})
	@ResponseBody
	public ResponseEntity<SchedulerPortfolioStats> report()
    {
    	try {
    		
    		SchedulerPortfolioStats schedulerPortfolioStats = this.conductorQuartzExecutionHistoryRepository.getSchedulerPortfolioStats();
    		
    		if(null!=schedulerPortfolioStats) {
    			
      			return new ResponseEntity(schedulerPortfolioStats, HttpStatus.OK);
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
