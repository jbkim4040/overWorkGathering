package com.overWorkGathering.main.service;

import com.overWorkGathering.main.DTO.WorkDTO;
import com.overWorkGathering.main.entity.WorkEntity;
import com.overWorkGathering.main.mapper.WorkMapper;
import com.overWorkGathering.main.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkService {
	
	@Autowired
	WorkRepository workRepository;


	WorkMapper workMapper;
	
	public List<WorkDTO> retrieveWork(String userId) {

		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String nowDt = localDate.format(formatter);
		
		String yyyyMM = nowDt.substring(0, 7);
		
		//현재 달
		String workDt = "%" + yyyyMM + "%";
		
		List<WorkEntity> workEntityList =
				workRepository.findAllByUserIdAndWorkDtLike(userId, workDt);


		return workMapper.toWorkDTOList(workEntityList);
	}
	
	public WorkDTO retrieveWorkOne(String userId, String workDt) {
		WorkEntity workEntity = workRepository.findAllByUserIdAndWorkDt("jhyuk97", workDt);
		if(workEntity != null) {

			return workMapper.toWorkDTO(workEntity);
		}
		return null;
	}

}
