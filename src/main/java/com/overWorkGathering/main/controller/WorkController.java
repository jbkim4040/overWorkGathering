package com.overWorkGathering.main.controller;

import com.overWorkGathering.main.DTO.WorkDTO;
import com.overWorkGathering.main.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

	@RequestMapping(value="/retrievePartInfo", method = RequestMethod.GET)
	public WorkDTO retrievePartInfo(@RequestParam String userID, @RequestParam String workDt) {
		return workService.retrieveWorkOne(userID, workDt);
	}
}
