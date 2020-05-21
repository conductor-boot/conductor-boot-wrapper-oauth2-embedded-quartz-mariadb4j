package com.my.conductorbootwrapper;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.my.conductorbootwrapper.config.EnvironmentVariablesToSystemPropertiesMappingConfiguration;
import com.my.conductorbootwrapper.oauth2.conductor.runner.thread.ConductorRunnerThreadProvider;

@SpringBootApplication
@EnableAutoConfiguration(exclude={ErrorMvcAutoConfiguration.class, DataSourceAutoConfiguration.class, RestClientAutoConfiguration.class, CassandraAutoConfiguration.class})
@EnableZuulProxy
@EnableBatchProcessing
@EnableTransactionManagement
public class ConductorBootWrapper {
	
	@Autowired
	EnvironmentVariablesToSystemPropertiesMappingConfiguration environmentVariablesToSystemPropertiesMappingConfiguration;

	private static String[] args_buffer;
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ConductorBootWrapper.class);
        args_buffer = args;
        app.run(args);
	}
	
	@EventListener(ApplicationStartedEvent.class)
	public void mapEnvToProp()
	{
		this.environmentVariablesToSystemPropertiesMappingConfiguration.mapEnvToProp();
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void startConductorServer() throws InterruptedException {
		
		ConductorRunnerThreadProvider conductorRunnerThread = ConductorRunnerThreadProvider.getInstance();
		
		conductorRunnerThread.configureArgs(args_buffer);
		conductorRunnerThread.start();  
	}

}
