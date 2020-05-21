package com.my.conductorbootwrapper.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.conductorbootwrapper.db.entities.ConductorQuartzMapping;

public interface ConductorQuartzMappingRepository extends JpaRepository<ConductorQuartzMapping, Long> {

	ConductorQuartzMapping findByQuartzSchedulerId(String quartzSchedulerId);
}
