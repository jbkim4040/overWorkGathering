package com.overWorkGathering.main.repository;

import com.overWorkGathering.main.entity.WorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkRepository extends JpaRepository<WorkEntity, String> {

	List<WorkEntity> findAllByUserId(String id);

	WorkEntity findAllByUserIdAndWorkDt(String userId, String workDt);

	List<WorkEntity> findAllByUserIdAndWorkDtLike(String userId, String workDt);
	
}
