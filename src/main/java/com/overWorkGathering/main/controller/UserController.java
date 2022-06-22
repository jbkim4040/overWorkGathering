package com.overWorkGathering.main.controller;


import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/calendar", method = RequestMethod.GET)
	public String calendar() {
		System.out.println("CalendarCalendarCalendarCalendarCalendar");
		
		return "Calendar";
	}

	@PostMapping(value = "/auth")
	public void auth(HttpServletRequest request, HttpServletResponse response, UserDTO userInfo){
		System.out.println("화면에서 넘어 온 값" + userInfo.toString());
		boolean auth =true;
		try {
			auth = userService.auth(request, response, userInfo);

			if(auth){
				response.sendRedirect("/calendar");
			}else{
				response.sendRedirect("/");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
