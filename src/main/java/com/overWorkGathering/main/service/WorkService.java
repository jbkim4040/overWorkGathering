package com.overWorkGathering.main.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.DTO.WorkCollectionReqDTO;
import com.overWorkGathering.main.DTO.WorkDTO;
import com.overWorkGathering.main.entity.UserEntity;
import com.overWorkGathering.main.entity.WorkEntity;
import com.overWorkGathering.main.repository.UserRepository;
import com.overWorkGathering.main.repository.WorkRepository;

@Service
public class WorkService {
	
	@Autowired
	WorkRepository workRepository;
	@Autowired
	UserRepository userRepository;
	
	ModelMapper modelMapper = new ModelMapper();
	
	public List<WorkDTO> retrieveWork(String userId) {

		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String nowDt = localDate.format(formatter);
		
		String yyyyMM = nowDt.substring(0, 7);
		
		//현재 달
		String workDt = "%" + yyyyMM + "%";
		
		List<WorkEntity> workEntityList =
				workRepository.findAllByUserIdAndWorkDtLike(userId, workDt);
		
		return workEntityList
				.stream() 
				.map(workEntity -> modelMapper.map(workEntity,
						WorkDTO.class)) 
				.collect(Collectors.toList());
	}
	
	/*
	 * 해당날짜 식대신청여부확인을 위한 조회
	 */
	public WorkDTO retrieveWorkOne(String userId, String workDt) {
		WorkEntity workEntity = workRepository.findAllByUserIdAndWorkDt("jhyuk97", workDt);
		if(workEntity != null) {
			return modelMapper.map(workEntity, WorkDTO.class);
		}
		return null;
	}

	/*
	 * 파트, 월별 야근식대, 택시비 계산
	 */
	public List<WorkCollectionReqDTO> retrieveWorkCollection(String part, String dt) {
		
		//파트 구성원 조회
		List<String> userIdList = retireveUserId(part, dt);
		
		//야근식대 택시비 취합
		List<WorkCollectionReqDTO> WorkCollectionReqDTOList = collectionDinnerPayAndTaxiPay(userIdList, dt);
		
		return WorkCollectionReqDTOList;
	}

	/*
	 * 야근식대 택시비 취합
	 */
	private List<WorkCollectionReqDTO> collectionDinnerPayAndTaxiPay(List<String> userIdList, String dt) {
		
		List<WorkCollectionReqDTO> WorkCollectionReqDTOList = new ArrayList<WorkCollectionReqDTO>();
		
		String workDt = "%" + dt + "%";
		
		List<WorkEntity> workEntityList =
				workRepository.findAllByUserIdInAndWorkDtLike(userIdList ,workDt);
		
		List<WorkDTO> WorkDTOList = 
				workEntityList.stream() 
					.map(workEntity -> modelMapper.map(workEntity,
							WorkDTO.class)) 
					.collect(Collectors.toList());
		
		WorkDTOList.stream().forEach(item ->{
			WorkCollectionReqDTOList.add(WorkCollectionReqDTO.builder()
					.workDt(item.getWorkDt())
					.dinnerPay(plusDinnerPay(WorkDTOList, item.getWorkDt()))
					.taxiPay(plusTaxiPay(WorkDTOList, item.getWorkDt()))
					.other("")
					.build());
		});
		
		return WorkCollectionReqDTOList.stream().distinct().collect(Collectors.toList());
	}

	/*
	 * 파트 구성원 조회
	 */
	private List<String> retireveUserId(String part, String Dt) {
		
		List<UserEntity> userEntityList = userRepository.findAllByPart(part);
		
		List<UserDTO> userDTOList = 
				userEntityList.stream() 
					.map(userEntity -> modelMapper.map(userEntity,
							UserDTO.class)) 
					.collect(Collectors.toList());
		
		return userDTOList.stream().map(UserDTO::getUserId).collect(Collectors.toList());
	}

	/*
	 * 택시비 취합
	 */
	private String plusTaxiPay(List<WorkDTO> workDTOList, String workDt) {
		
		List<String> taxiPays 
			= workDTOList.stream()
					 .filter(item -> workDt.equals(item.getWorkDt()))
					 .filter(item -> "Y".equals(item.getTaxiYn()))
					 .map(WorkDTO::getTaxiPay)
					 .collect(Collectors.toList());
		
		if(taxiPays.size() == 0) { return "0"; }
		
		int plusTaxiPay = 0;
		
		for (String item : taxiPays) {
			plusTaxiPay += Integer.parseInt(item);
		}
	
		return Integer.toString(plusTaxiPay);
	}
/*
 * 저녁식대 취합
 */
	private String plusDinnerPay(List<WorkDTO> workDTOList, String workDt) {
		
		List<String> dinnerPays 
			= workDTOList.stream()
						 .filter(item -> workDt.equals(item.getWorkDt()))
						 .filter(item -> "Y".equals(item.getDinnerYn()))
						 .map(WorkDTO::getDinnerYn)
						 .collect(Collectors.toList());
		
		return Integer.toString(7000 * dinnerPays.size());
	}
	
	/*
	 * 야근식대 저장 및 업데이트
	 */
	public void saveWork(Map<String, Object> param) {
		String startTime = "09:00";
		String endTime = "";
		if("true".equals(param.get("dinnerYn").toString())) {
			param.replace("dinnerYn", "Y");
			endTime = "21:00";
		}else {
			param.replace("dinnerYn", "N");
		}
		
		if("true".equals(param.get("taxiYn").toString())) {
			param.replace("taxiYn", "Y");
			endTime = "23:00";
		}else {
			param.replace("taxiYn", "N");
		}
		
		WorkDTO workDTO = WorkDTO.builder().userId(param.get("userID").toString())
						 .workDt(param.get("workDt").toString())
						 .startTime(startTime)
						 .endTime(endTime)
						 .img(param.get("Img").toString())
						 .taxiPay(param.get("taxiPay").toString())
						 .dinnerYn(param.get("dinnerYn").toString())
						 .taxiYn(param.get("taxiYn").toString()).build();
		
		WorkEntity workEntity = modelMapper.map(workDTO, WorkEntity.class);
		
		workRepository.save(workEntity);
	}
	
	/*
	 * 야근식대 삭제
	 */
	public void deleteWork(Map<String, Object> param) {
		workRepository.deleteByUserIdAndWorkDt(param.get("userID").toString(), param.get("workDt").toString());
	}

}
