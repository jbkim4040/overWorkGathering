package com.overWorkGathering.main.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.overWorkGathering.main.DTO.WorkCollectionDtlReqDTO;
import com.overWorkGathering.main.entity.UserInfoEntity;
import com.overWorkGathering.main.entity.WorkHisEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mapstruct.Mapper;

import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.DTO.WorkCollectionReqDTO;
import com.overWorkGathering.main.DTO.WorkDTO;
import com.overWorkGathering.main.mapper.UserMapper;
import com.overWorkGathering.main.mapper.WorkMapper;
import com.overWorkGathering.main.repository.UserRepository;
import com.overWorkGathering.main.repository.WorkRepository;

@Service
public class WorkService {

	@Autowired
	WorkRepository workRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	WorkMapper workMapper;
	@Autowired
	UserMapper userMapper;

	public List<WorkDTO> retrieveWork(String userId) {

		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String nowDt = localDate.format(formatter);

		String yyyyMM = nowDt.substring(0, 7);

		//현재 달
		String workDt = "%" + yyyyMM + "%";

		List<WorkHisEntity> workHisEntityList =
				workRepository.findAllByUserIdAndWorkDtLike(userId, workDt);

		return workMapper.toWorkDTOList(workHisEntityList);
	}

	/*
	 * 해당날짜 식대신청여부확인을 위한 조회
	 */
	public WorkDTO retrieveWorkOne(String userId, String workDt) {
		WorkHisEntity workHisEntity = workRepository.findAllByUserIdAndWorkDt("jhyuk97", workDt);
		if(workHisEntity != null) {
			return workMapper.toWorkDTO(workHisEntity);
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

		List<WorkHisEntity> workHisEntityList =
				workRepository.findAllByUserIdInAndWorkDtLike(userIdList ,workDt);

		List<WorkDTO> WorkDTOList = workMapper.toWorkDTOList(workHisEntityList);

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

		List<UserInfoEntity> userInfoEntityList = userRepository.findAllByPart(part);

		List<UserDTO> userDTOList = userMapper.toUserDTOList(userInfoEntityList);

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

		if("true".equals(param.get("dinnerYn").toString())) {
			param.replace("dinnerYn", "Y");
		}else {
			param.replace("dinnerYn", "N");
		}

		if("true".equals(param.get("taxiYn").toString())) {
			param.replace("taxiYn", "Y");
		}else {
			param.replace("taxiYn", "N");
		}

		WorkDTO workDTO = WorkDTO.builder().userId(param.get("userID").toString())
				.workDt(param.get("workDt").toString())
				.startTime(param.get("startTime").toString())
				.endTime(param.get("endTime").toString())
				.img(param.get("Img").toString())
				.taxiPay(param.get("taxiPay").toString())
				.dinnerYn(param.get("dinnerYn").toString())
				.taxiYn(param.get("taxiYn").toString())
				.remarks(param.get("remarks").toString()).build();

		WorkHisEntity workHisEntity = workMapper.toWorkEntity(workDTO);

		workRepository.save(workHisEntity);
	}

	/*
	 * 야근식대 삭제
	 */
	public void deleteWork(Map<String, Object> param) {
		workRepository.deleteByUserIdAndWorkDt(param.get("userID").toString(), param.get("workDt").toString());
	}

	/*
	 * 월간 야근식대 요청 현황 상세 조회
	 */
	public List<WorkDTO> retrieveWorkCollectionDtl(String part, String dt){
		//파트 구성원 조회
		List<UserInfoEntity> userInfoEntityList = userRepository.findAllByPart(part);
		List<UserDTO> userDTOList = userMapper.toUserDTOList(userInfoEntityList);
		List<String> userIdList = userDTOList.stream().map(UserDTO::getUserId).collect(Collectors.toList());

		//월간 야근식대 요청 현황 전체 조회
		List<WorkDTO> workDTOList = retrieveWorkCollectionReqDTOList(userIdList, dt);

		//유저 한글명 세팅
		return setpUserNm(workDTOList, userDTOList);
	}

	/*
	 * 유저 한글명 세팅
	 */
	private List<WorkDTO> setpUserNm(List<WorkDTO> workDTOList, List<UserDTO> userDTOList) {

		Map<String, String> userNm = userDTOList.stream().collect(Collectors.toMap(UserDTO::getUserId, UserDTO::getName));

		workDTOList.stream().forEach(item ->{
			item.setUserId(userNm.get(item.getUserId()));
		});

		return workDTOList;
	}


	/*
	 * 월간 야근식대 요청 현황 전체 조회
	 */
	public List<WorkDTO> retrieveWorkCollectionReqDTOList(List<String> userIdList, String dt) {

		String workDt = "%" + dt + "%";

		List<WorkHisEntity> workHisEntityList =
				workRepository.findAllByUserIdInAndWorkDtLike(userIdList ,workDt);

		return workMapper.toWorkDTOList(workHisEntityList);
	}
}
