package com.overWorkGathering.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.overWorkGathering.main.DTO.WorkCollectionDtlReqDTO;
import com.overWorkGathering.main.DTO.WorkCollectionReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.overWorkGathering.main.DTO.WorkDTO;
import com.overWorkGathering.main.service.UserService;
import com.overWorkGathering.main.service.WorkService;

@RestController
@RequestMapping(path = "/work")
public class WorkController {

	@Autowired
	WorkService workService;	

	@RequestMapping(value="/retrievework", method = RequestMethod.GET)
	public List<WorkDTO> retrieveWork(@RequestParam String userId) {
		return workService.retrieveWork(userId);
	}
	
	@RequestMapping(value="/retrieveWorkOne", method = RequestMethod.GET)
	public WorkDTO retrieveWorkOne(@RequestParam String userID, @RequestParam String workDt) {
		return workService.retrieveWorkOne(userID, workDt);
	}
	
	@RequestMapping(value="/SaveWork", method = RequestMethod.POST)
	public void saveWork(@RequestBody Map<String, Object> param) {
		workService.saveWork(param);
	}
	
	@RequestMapping(value="/DeleteWork", method = RequestMethod.POST)
	public void DeleteWork(@RequestBody Map<String, Object> param) {
		workService.deleteWork(param);
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
}
