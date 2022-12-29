package com.overWorkGathering.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.overWorkGathering.main.entity.WorkHisEntity;

@Repository
public interface WorkRepository extends JpaRepository<WorkHisEntity, String> {

	List<WorkHisEntity> findAllByUserId(String id);

	WorkHisEntity findAllByUserIdAndWorkDt(String userId, String workDt);

	List<WorkHisEntity> findAllByUserIdAndWorkDtLike(String userId, String workDt);
	
	@Transactional
	void deleteByUserIdAndWorkDt(String userId, String workDt);

	List<WorkHisEntity> findAllByUserIdInAndWorkDtLike(List<String> userIdList, String workDt);
	
}
