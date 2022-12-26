package com.overWorkGathering.main.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.overWorkGathering.main.DTO.WorkCollectionDtlReqDTO;
import com.overWorkGathering.main.DTO.WorkCollectionReqDTO;
import com.overWorkGathering.main.utils.FTPUploader;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.overWorkGathering.main.DTO.WorkDTO;
import com.overWorkGathering.main.service.UserService;
import com.overWorkGathering.main.service.WorkService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/work")
public class WorkController {

	@Autowired
	WorkService workService;	

//	@RequestMapping(value="/retrievework", method = RequestMethod.GET)
//	public List<WorkDTO> retrieveWork(@RequestParam String userId) {
//		return workService.retrieveWork(userId);
//	}

	@RequestMapping(value="/retrievework", method = RequestMethod.GET)
	public List<WorkDTO> retrieveWork(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		String userName = (String)session.getAttribute("userName");

		System.out.println("세션에서 가져온 ID >>>>> " + userId);
		System.out.println("세션에서 가져온 이름 >>>>> " + userName);

		return workService.retrieveWork(userId);
	}
	
	@RequestMapping(value="/retrieveWorkOne", method = RequestMethod.GET)
	public WorkDTO retrieveWorkOne(@RequestParam String userID, @RequestParam String workDt) {
		return workService.retrieveWorkOne(userID, workDt);
	}

	@RequestMapping(value="/SaveWork", method = RequestMethod.POST)
	public void saveWork(@RequestParam HashMap<String, Object> param, MultipartFile file, HttpServletRequest request) {
		workService.saveWork(param, file, request);
	}
	
	@RequestMapping(value="/DeleteWork", method = RequestMethod.POST)
	public void DeleteWork(@RequestParam HashMap<String, Object> param, MultipartFile file, HttpServletRequest request) {
		workService.deleteWork(param, file, request);
	}

	/*
	 * 월간 야근식대 요청 현황 합계 조회
	 */
	@RequestMapping(value="/retrieveWorkCollection", method = RequestMethod.GET)
	public List<WorkCollectionReqDTO> retrieveWorkCollection(@RequestParam String part, @RequestParam String dt) {
		return workService.retrieveWorkCollection(part, dt);
	}

	/*
	 * 월간 야근식대 요청 현황 상세 조회
	 */
	@RequestMapping(value="/retrieveWorkCollectionDtl", method = RequestMethod.GET)
	public Map<String, List<WorkCollectionDtlReqDTO>> retrieveExcelDtl(@RequestParam String part, @RequestParam String dt) {
		return workService.retrieveExcelDtl(part, dt);
	}

	/*
	 * 월간 야근식대 요청 개인현황 상세 조회
	 */
	@RequestMapping(value="/retrieveWorkCollectionUserDtl", method = RequestMethod.GET)
	public List<WorkCollectionDtlReqDTO> retrieveWorkCollectionUserDtl(@RequestParam String part, @RequestParam String dt, @RequestParam String userId) {
		return workService.retrieveWorkCollectionUserDtl(part, dt, userId);
	}

	@RequestMapping(value="/fileSendingTest", method = RequestMethod.POST)
	public void retrieveWorkCollectionUserDtl(@RequestParam MultipartFile imageFile, HttpServletRequest request) {
		workService.saveTaxiReceiptImgFile(imageFile, request);
	}

	@RequestMapping(value="/taxiReceiptImgFile", method = RequestMethod.POST)
	public void saveTaxiReceiptImgFile(@RequestParam MultipartFile imageFile, HttpServletRequest request) {
		workService.saveTaxiReceiptImgFile(imageFile, request);
	}

	@RequestMapping(value="/taxiReceiptImgFileList", method = RequestMethod.POST)
	public void saveTaxiReceiptImgFileList(@RequestParam List<MultipartFile> imageFileList, HttpServletRequest request) {
		workService.saveTaxiReceiptImgFileList(imageFileList, request);
	}

	@RequestMapping(value="/taxiReceiptImgFile", method = RequestMethod.GET)
	public void downloadTaxiReceiptImgFile(String imageFileName, HttpServletRequest request) throws Exception{
		workService.downloadTaxiReceiptImgFile(imageFileName, request);
	}


	@RequestMapping(value="/taxiReceiptImgFile", method = RequestMethod.DELETE)
	public void deleteTaxiReceiptImgFile(String imageFileName, HttpServletRequest request) throws Exception{
		workService.deleteTaxiReceiptImgFile(imageFileName, request);
	}
}
