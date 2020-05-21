package com.my.conductorbootwrapper.db.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.my.conductorbootwrapper.db.entities.ConductorQuartzExecutionHistory;
import com.my.conductorbootwrapper.db.entities.ConductorQuartzMapping;
import com.my.conductorbootwrapper.db.entities.SchedulerPortfolioStats;

public interface ConductorQuartzExecutionHistoryRepository extends JpaRepository<ConductorQuartzExecutionHistory, Long> {

	List<ConductorQuartzExecutionHistory> findByConductorQuartzMapping(ConductorQuartzMapping conductorQuartzMapping);

	@Query(value="SELECT new com.my.conductorbootwrapper.db.entities.SchedulerPortfolioStats(count(*), (SELECT count(*) FROM ConductorQuartzExecutionHistory WHERE quartzExecutionStatus = false), (SELECT count(*) FROM ConductorQuartzExecutionHistory WHERE quartzExecutionStatus = true)) FROM ConductorQuartzExecutionHistory")
	SchedulerPortfolioStats getSchedulerPortfolioStats();
}
