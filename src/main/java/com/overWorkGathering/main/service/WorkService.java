package com.overWorkGathering.main.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.overWorkGathering.main.DTO.WorkCollectionDtlReqDTO;
import com.overWorkGathering.main.entity.UserInfoEntity;
import com.overWorkGathering.main.entity.WorkHisEntity;
import com.overWorkGathering.main.utils.FTPUploader;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.mapstruct.Mapper;

import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.DTO.WorkCollectionReqDTO;
import com.overWorkGathering.main.DTO.WorkDTO;
import com.overWorkGathering.main.mapper.UserMapper;
import com.overWorkGathering.main.mapper.WorkMapper;
import com.overWorkGathering.main.repository.UserRepository;
import com.overWorkGathering.main.repository.WorkRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
public class WorkService {
	@Value("${spring.profiles.active}")
	private String currentEnvironment;
	private final String SFTP_HOST = "iabacus.fun25.co.kr";
	private final int SFTP_PORT = 23203;
	private final String SFTP_USER_ID = "root";
	private final String SFTP_USER_PWD = "abacus2017!";
	private final String SFTP_TAXI_RECEIPT_IMG_PATH = "/var/dev/overworkgathering/images/";

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
	private int plusTaxiPay(List<WorkDTO> workDTOList, String workDt) {

		List<Integer> taxiPays
				= workDTOList.stream()
				.filter(item -> workDt.equals(item.getWorkDt()))
				.filter(item -> "Y".equals(item.getTaxiYn()))
				.map(WorkDTO::getTaxiPay)
				.collect(Collectors.toList());

		if(taxiPays.size() == 0) { return 0; }

		int plusTaxiPay = 0;

		for (int item : taxiPays) {
			plusTaxiPay += item;
		}

		return plusTaxiPay;
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

		return Integer.toString(9000 * dinnerPays.size());
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

		if("".equals(param.get("taxiPay").toString())){
			param.replace("taxiPay", "0");
		}

		WorkDTO workDTO = WorkDTO.builder().userId(param.get("userID").toString())
				.workDt(param.get("workDt").toString())
				.startTime(param.get("startTime").toString())
				.endTime(param.get("endTime").toString())
				.taxiReceiptImg(param.get("Img").toString())
				.taxiPay(Integer.parseInt(param.get("taxiPay").toString()))
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
	public List<WorkCollectionDtlReqDTO> retrieveWorkCollectionDtl(String part, String dt){
		//파트 구성원 조회
		List<UserInfoEntity> userInfoEntityList = userRepository.findAllByPart(part);
		List<UserDTO> userDTOList = userMapper.toUserDTOList(userInfoEntityList);
		List<String> userIdList = userDTOList.stream().map(UserDTO::getUserId).collect(Collectors.toList());

		//월간 야근식대 요청 현황 전체 조회
		List<WorkDTO> workDTOList = retrieveWorkCollectionReqDTOList(userIdList, dt);

		List<WorkCollectionDtlReqDTO> workCollectionDtlReqDTOList = setpWorkCollectionDtlReqDTO(workDTOList, userDTOList);

		return workCollectionDtlReqDTOList;
	}


	private List<WorkCollectionDtlReqDTO> setpWorkCollectionDtlReqDTO(List<WorkDTO> workDTOList, List<UserDTO> userDTOList) {
		List<WorkCollectionDtlReqDTO> workCollectionDtlReqDTOlist = new ArrayList<>();
		workDTOList.stream().forEach(item ->{
			workCollectionDtlReqDTOlist.add(WorkCollectionDtlReqDTO
					.builder()
					.workDt(item.getWorkDt())
					.startTime(item.getStartTime())
					.endTime(item.getEndTime())
					.userId(item.getUserId())
					.taxiPay(item.getTaxiPay())
					.dinnerYn(item.getDinnerYn())
					.name(setUserNm(userDTOList, item.getUserId()))
					.remarks(item.getRemarks())
					.build()
			);
		});

		return workCollectionDtlReqDTOlist;
	}

	private String setUserNm(List<UserDTO> userDTOList, String userId) {
		return userDTOList.stream().filter(item -> userId.equals( item.getUserId() )).map(UserDTO::getName).findFirst().orElseGet(()-> "");
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

	public Map<String, List<WorkCollectionDtlReqDTO>> retrieveExcelDtl(String part, String dt){

		List<WorkCollectionDtlReqDTO> workCollectionDtlReqDTOList = retrieveWorkCollectionDtl( part, dt );

		return setResultMap(workCollectionDtlReqDTOList);
	}

	private Map<String, List<WorkCollectionDtlReqDTO>> setResultMap(List<WorkCollectionDtlReqDTO> workCollectionDtlReqDTOList) {

		Map<String, List<WorkCollectionDtlReqDTO>> resultMap = new HashMap<>();

		List<String> reqUserIdList = workCollectionDtlReqDTOList.stream().map(WorkCollectionDtlReqDTO::getUserId).distinct().collect(Collectors.toList());

		if ( !reqUserIdList.isEmpty() ) {
			reqUserIdList.stream().forEach(item -> {
				resultMap.put(item, workCollectionDtlReqDTOList.stream().filter(e -> item.equals(e.getUserId())).collect(Collectors.toList()));
			});
		}

		return resultMap;
	}


	public List<WorkCollectionDtlReqDTO> retrieveWorkCollectionUserDtl(String part, String dt, String userId) {

		Map<String, List<WorkCollectionDtlReqDTO>> retrieveWorkCollectionDtl = retrieveExcelDtl(part, dt);

		return retrieveWorkCollectionDtl.get(userId);
	}

	public void saveTaxiReceiptImgFile(MultipartFile imageFile, HttpServletRequest request){
		System.out.println("현재 개발환경 :: " + currentEnvironment);

		FTPUploader fileUploader = null;
		String currentDt = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		final String newImgFileName = UUID.randomUUID().toString().replace("-", "");

		if(imageFile.isEmpty()){
			System.out.println("imageFile 비어있음");
		}else{
			String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
			String currentPath = System.getProperty("user.dir");

			String fullPath = currentPath + "/ " + newImgFileName;
			File uploadfile = new File(fullPath); // 파일 객체 생성

			try{
				if(Integer.parseInt(currentDt.substring(6)) < 15){
					currentDt = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
				}else {
					currentDt = currentDt.substring(0, 6);
				}

				imageFile.transferTo(new File(fullPath));

				fileUploader = new FTPUploader(SFTP_HOST, SFTP_PORT, SFTP_USER_ID, SFTP_USER_PWD,null);

				if(!fileUploader.exists(SFTP_TAXI_RECEIPT_IMG_PATH + currentDt)){
					fileUploader.mkdir(SFTP_TAXI_RECEIPT_IMG_PATH, currentDt);
				}

				fileUploader.uploadFile(SFTP_TAXI_RECEIPT_IMG_PATH + currentDt, uploadfile); //업로드
			}catch(IOException ioe){
				ioe.printStackTrace();
				System.out.println(ioe.getMessage());
			}catch(Exception ex){
				ex.printStackTrace();
				System.out.println(ex.getMessage());
			}finally {
				fileUploader.disconnect();
				uploadfile.delete();
			}
		}
	}

	public void saveTaxiReceiptImgFileList(List<MultipartFile> imageFileList, HttpServletRequest request){
		for(MultipartFile imageFile : imageFileList){
			saveTaxiReceiptImgFile(imageFile, request);
		}
	}

	public void downloadTaxiReceiptImgFile(String imageFileName, HttpServletRequest request){
		FTPUploader fileUploader = null;
		String currentDt = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		if(StringUtils.isEmpty(imageFileName)){
			System.out.println("파일명이 존재하지 않습니다.");
		}else{

		}
	}
}
