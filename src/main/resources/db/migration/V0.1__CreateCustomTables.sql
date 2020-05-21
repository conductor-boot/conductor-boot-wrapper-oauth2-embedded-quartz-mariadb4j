CREATE TABLE `conductor_quartz_mapping` (
	`mapping_id` BIGINT(8) NOT NULL AUTO_INCREMENT,
	`quartz_scheduler_id` VARCHAR(150) NULL,
	`schedule_name` VARCHAR(150) NOT NULL,
	`schedule_version` INT NOT NULL DEFAULT 0,
	`schedule_description` VARCHAR(500) NOT NULL,
	`workflow_name` VARCHAR(150) NOT NULL,
	`workflow_version` INT NOT NULL DEFAULT 0,
	`schedule_workflow_payload` BLOB NOT NULL,
	`schedule_cron_expression` VARCHAR(45) NOT NULL,
	`schedule_current_status` VARCHAR(45) NOT NULL,
	`schedule_start_timestamp` TIMESTAMP NULL,
	`schedule_end_timestamp` TIMESTAMP NOT NULL,
	`insert_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
	`update_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
	PRIMARY KEY (`mapping_id`),
	UNIQUE INDEX `mapping_id_UNIQUE` (`mapping_id` ASC) ,
	UNIQUE INDEX `schedule_detail_UNIQUE` (`schedule_name` ASC, `schedule_version` ASC) ,
	UNIQUE INDEX `quartz_scheduler_id_UNIQUE` (`quartz_scheduler_id` ASC) ,
	INDEX `workflow_metadata_IDX` (`workflow_name` ASC, `workflow_version` ASC) );

CREATE TABLE `conductor_quartz_execution_history`(

	`history_id` BIGINT(8) NOT NULL AUTO_INCREMENT,
	`mapping_id` BIGINT(8) NOT NULL,
	`conductor_correlation_id` VARCHAR(150) NULL,
	`quartz_execution_status` BIT(1) NOT NULL DEFAULT b'0',
	`quartz_execution_log` VARCHAR(150) NULL,
	`insert_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
	`update_timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP(),
	PRIMARY KEY (`history_id`),
	UNIQUE INDEX `history_id_UNIQUE` (`history_id` ASC) ,
	UNIQUE INDEX `conductor_correlation_id_UNIQUE` (`conductor_correlation_id` ASC) ,
	INDEX `mapping_id_FKEY_IDX` (`mapping_id` ASC) ,
	CONSTRAINT `mapping_id_FKEY` 
		FOREIGN KEY (`mapping_id`)
		REFERENCES `conductor_quartz_mapping` (`mapping_id`)
		ON DELETE CASCADE
		ON UPDATE CASCADE);