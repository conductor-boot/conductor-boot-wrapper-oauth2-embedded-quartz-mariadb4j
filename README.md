# Conductor Boot Wrapper - Quartz Bundle

Before starting, for details on Netflix Conductor, refer to <a href="http://netflix.github.io/conductor/">Conductor Documentation</a>


##### Note

      • Only MySQL is supported as external Database
      
      				Or
      
      • Embedded MariaDB4j is supported, which is persistent and data is not lost, thus eliminating the need for external

Conductor Boot Wrapper + Quartz Bundle = Conductor Quartz Workflow Scheduler Bundle


The idea is to build a single suite of loosely coupled open source tools to perform scheduling i.e. triggering a Netflix Conductor workflow at a defined time or regular intervals of time, with a pre defined input, WITHOUT THE NEED TO CODE (To the maximum extent possible), by implementing

 • Spring Boot - Quartz Scheduler - embedded MariaDB support & external MySQL DB support.


The catch point being, it will be a UI and Scheduler Jar  which will enable Conductor Workflows to be scheduled by user using UI interface 

How ? 

By bridging the communication with RestFUL JSON based web services / APIs.

By combining Quartz with Conductor, the Workflows that are otherwise triggered manually are now automatically triggered in scheduled manner and Quartz does the Scheduler part.

Though the idea of making Scheduler for Conductor is good, it still needs a lot of knowledge on JSON, to write up the Web Service calls and lot of knowledge on Quartz, to define the Scheduler part.

## Overview

The idea is to build a single Spring Boot Jar with the following 

      • Micro Services Orchestration - by Conductor Server
      
      • OAuth2 Authentication & Authorization
      
      • Conductor Workflow Scheduler - by Quartz Scheduler
      
      • Zuul Gateway - by Netflix Zuul Proxy for acting as proxy to Conductor Server APIs
      
      • Optional Embedded Persistent MariaDB4j

## Build Status - ![Java CI with Maven](https://github.com/conductor-boot/conductor-boot-wrapper-embedded-quartz-mariadb4j/workflows/Java%20CI%20with%20Maven/badge.svg)

## Pre-Requisites
1. openjdk8 or Java 8
2. Maven / mvn

## Setup Instructions

 • Clone / Download and extract the Repo

## Startup Instructions

 • To start the scheduler with embedded mariadb4j as persistence, use Maven run. 
To use a permanent Port for mariadb4j or persistent dataDir, please uncomment and provide corresponding details in application.yml
To use external mysql database as persistence unit, please change db=mysql and provide the DB url and driver class accordingly. The properties are already pasted , commented in application.yml file.

Also an important property to be configured in application.yml is conductor.server.api.endpoint , which internally is used to trigger the workflows through rest API call.

cd conductor-boot-wrapper-embedded-quartz-mariadb4j

mvn spring-boot:run

In case, you would like to package the jar first and then run.

mvn install

cd target

java -jar conductor-boot-wrapper-${conductor.version}-all.jar

## Application URLs

		1) HTTPS
			a) https://localhost:8080/openapi - To access the Swagger pertaining to APIs for OAUTH
			b) https://localhost:8080/index.html - To access the Swagger pertaining to APIs for Conductor, as https redirection doesn't work to index.html
			
		2) HTTP
			a) http://localhost:8080/openapi - To access the Swagger pertaining to APIs for OAUTH
			b) http://localhost:8080/ - To access the Swagger pertaining to APIs for Conductor, as redirection is taken care to index.html

## Screenshots

1) Swagger UI Home - Conductor Quartz Workflow Scheduler Jar

![alt text](https://raw.githubusercontent.com/maheshyaddanapudi/images/master/CQWSchedulerSwaggerUIHome.png)

2) Swagger UI - Conductor Quartz Workflow Scheduler - API Part 1

![alt text](https://raw.githubusercontent.com/maheshyaddanapudi/images/master/CQWSchedulerSwaggerUIAPI1.png)

3) Swagger UI - Conductor Quartz Workflow Scheduler - API Part 2

![alt text](https://raw.githubusercontent.com/maheshyaddanapudi/images/master/CQWSchedulerSwaggerUIAPI2.png)


## Motivation

There is a saying from recent times that "Every idea comes from a pain point"

Scheduler in daily IT life are more needed in areas where implementation of Microservice Orchestrators like Netflix Conductor etc. is done. As known, not everywhere is comfortable, easy to plugin scheduler without coding available.
For such scenarios, usually a dependency is created on external scheduler tools, usually org wide tools like Control M or Autosys. That is where Quartz came into picture to ease the scheduler dependency with some efficient standards. Then the final hurdle of making a framework which hosts the configured Schedules and executes the schedule on time or maintain a history of what happened to a configured schedule etc. 

To avoid all this pain of below listed, this idea of Conductor Boot Wrapper - Quartz Bundle came up.

 • Learning to code at least moderately even though it's an easier JSON API

 • Learning new, though simple tools, like Quartz

 • Dependency on external schedulers 

 • Hosting OAuth2 Server for securing Conductor Server APIs
      
 • Housing external database engine for Conductor Server persistence unit


## Features

 • Schedule Netflix Conductor Workflows with Predefined inputs JSON payload, with a variety of Minute / Hourly / Daily / Weekly / Yearly scheduling options.

 • View a comprehensive list of all the Netflix Conductor Workflow trigger schedules, scheduled through Quartz & API

 • View a summary of run history of each and every Netflix Conductor Workflow trigger schedules, scheduled through Quartz & API


## Tech/framework used

 • conductor-server - Presumably downloaded from Maven repository

 • Java Spring Boot : Quartz + Mariadb4j

      --> Spring Boot Wrapper
			
            • Spring Cloud OAuth2
            
            • MariaDB4j
            
            • Flyway Initialiser
            
            • Netflix Conductor Server with MySQL as persistence option (with pre-existing flyway migration).
			
            • Netflix Zuul Proxy Server
            
## Authentication Process

##### Note
	
  • HTTPS is NOT enabled by default (with self signed certificate). Change the property to true and HTTPS will be enabled
  
	  		server:
	  		  ssl:
	  		    enabled: true
	  		    
	  		--> To view the Swagger URLs in browser - In Chrome, you can use url chrome://flags/#allow-insecure-localhost to allow insecure localhost. Refer to https://stackoverflow.com/questions/7580508/getting-chrome-to-accept-self-signed-localhost-certificate
	  			And	  		
	  		--> To call APIs in Postman - There is an option in Postman, in the settings, turn off the SSL certificate verification option 
	  		
	  		NOTE: For accessing Condcutor Swagger on HTTPS https://localhost:8080/index.html should be used as a default redirection doesnt work.
  
  • HTTP only mode - Change the property to true and HTTPS will be disabled
  
	  		server:
	  		  ssl:
	  		    enabled: false
	  		    
NOTE: For accessing Condcutor Swagger on HTTPS https://localhost:8080/ will be sufficient as redirection works without https.


##### Generating a self signed certificate

Certificate for HTTPS

	We can store as many numbers of key-pair in the same keystore each identified by a unique alias.
	
	  • For generating our keystore in a JKS format, we can use the following command:
			keytool -genkeypair -alias my -keyalg RSA -keysize 2048 -keystore my.jks -validity 3650
	  
	  • It is recommended to use the PKCS12 format which is an industry standard format. So in case we already have a JKS keystore, we can convert it to PKCS12 format using the following command:
    		keytool -importkeystore -srckeystore my.jks -destkeystore my.p12 -deststoretype pkcs12
	  
	  Place the generated my.p12 file in src/main/resources/keystore
	  

Certificate for OAUTH2

	
	  • For generating our keystore in a JKS format, we can use the following command:
	  	  keytool -genkeypair -alias jwt -keyalg RSA -keypass password -keystore jwt.jks -storepass password
	  	  
	  • It is recommended to use the PKCS12 format which is an industry standard format. So in case we already have a JKS keystore, we can convert it to PKCS12 format using the following command:
	  	  keytool -importkeystore -srckeystore jwt.jks -destkeystore jwt.jks -deststoretype pkcs12
	  	  
	  • To view the private and public key generated and to extract the public key, we can use the following command:
    	  	  keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey
	
        	  Copy everything including the tags from, as shown below and paste it into the applications.yml as a property
          	  -----BEGIN PUBLIC KEY-----
          	  .....
          	  -----END PUBLIC KEY-----
	  	 
	  Place the generated jwt.jks file in src/main/resources

## Conductor Authentication & Authorization - Roles

##### Category of APIs available at Conductor level.
	
		☐  Event Services - For Event Handling APIs
		
		☐  Workflow Management - For workflow executing, rerun, terminate, pause etc. functionalities.
		
		☐  Metadata Management - Workflow or task creation / updation / deletion etc. functionalities.
		
		☐  Health Check - Ignore for now
		
		☐  Admin - Ignore for now
		
		☐  Workflow Bulk Management - For workflow bulk executing, rerun, terminate, pause etc. functionalities.
		
		☐  Task Management - For task executing, rerun, terminate, pause etc. functionalities.
		

##### Roles that are to be mapped to APIs
		
		role_conductor_super_manager
		role_conductor_super_viewer
		role_conductor_core_manager
		role_conductor_core_viewer
		role_conductor_execution_manager
		role_conductor_execution_viewer
		role_conductor_event_manager
		role_conductor_event_view
		role_conductor_metadata_manager
		role_conductor_metadata_viewer
		role_conductor_metadata_workflow_manager
		role_conductor_metadata_workflow_viewer
		role_conductor_metadata_taskdefs_manager
		role_conductor_metadata_taskdefs_viewer
		role_conductor_workflow_manager
		role_conductor_workflow_viewer
		role_conductor_task_manager
		role_conductor_task_viewer

##### Technical mapping to roles.
		
		☐  All Manager roles will be able to Create/Update/Delete the mentioned API implemented functionalities.
		
		☐  All Viewer roles will be able to View existing API implemented functionalities.
		
		☐  A default user for each role is created while the flyway migration happens and the username is same as the role (example - 'role_conductor_super_manager') and the password is 'password'
	  		
			1) role_conductor_super_manager - POST / PUT / DELETE
				
			    Event Services
			    Workflow Management
			    Metadata Management
			    Health Check
			    Admin
			    Workflow Bulk Management
			    Task Management
			
			2) role_conductor_super_viewer - GET
				
			    Event Services
			    Workflow Management
			    Metadata Management
			    Health Check
			    Admin
			    Workflow Bulk Management
			    Task Management
			
			3) role_conductor_core_manager - POST / PUT / DELETE
			    Event Services
			    Workflow Management
			    Metadata Management
			    Workflow Bulk Management
			    Task Management
			
			4) role_conductor_core_viewer - GET
				
			    Event Services
			    Workflow Management
			    Metadata Management
			    Workflow Bulk Management
			    Task Management
			
			5) role_conductor_execution_manager - POST / PUT / DELETE
				
			    Event Services
			    Workflow Management
			    Task Management
			
			6) role_conductor_execution_viewer - GET
				
			    Event Services
			    Workflow Management
			    Task Management
			
			7) role_conductor_event_manager - POST / PUT / DELETE
				
			  	Event Services
			  
			8) role_conductor_event_viewer - GET
			  	
			  	Event Services
			
			9) role_conductor_metadata_manager - POST / PUT / DELETE
				
			    Metadata Management
			
			10) role_conductor_metadata_viewer - GET
			
			    Metadata Management
			
			11) role_conductor_workflow_manager - POST / PUT / DELETE
				
			  	Workflow Management
			  
			12) role_conductor_workflow_viewer - GET
			  	
			  	Workflow Management
			
			13) role_conductor_task_manager - POST / PUT / DELETE
				
			  	Task Management
			  
			14) role_conductor_task_viewer - GET
				
			  	Task Management
			  	
## OAuth Authentication & Authorization - Roles

##### Category of APIs available at OAuth level.
	
		☐  Admin Services - For Admin APIs - to onboard new clients and users, update or delete users / clients, reset client secret / user password.
		
		☐  Client Services - For Client Admin APIs - to onboard new users, update or delete users, reset client secret / user password.
		
		☐  User Services - For User APIs - to get basic principal, update details / reset password.

##### Roles that are to be mapped to APIs
		
		role_oauth_super_admin
		role_oauth_client_admin
		
##### Technical mapping to roles.
		
		☐  All Manager roles will be able to Create/Update/Delete the mentioned API implemented functionalities.
		
		☐  All Viewer roles will be able to View existing API implemented functionalities.
		
		☐  A default user for each role is created while the flyway migration happens and the username is same as the role (example - 'role_conductor_super_manager') and the password is 'password'
	  		
			1) role_oauth_super_admin - GET / POST / PUT / PATCH / DELETE
				
			    Admin Services
			    User Services
			
			2) role_oauth_client_admin - GET / POST / PUT / PATCH / DELETE
				
			    Client Services
			    User Services
			
##### NOTE: All the User Services will be OPEN for Conductor Roles as well.

## Inspiration

 • Netflix Conductor

 • Quartz Scheduler


## Credits

 • Mahesh Yaddanapudi - zzzmahesh@live.com
