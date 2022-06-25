package com.overWorkGathering.main.controller;


import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public void auth(HttpServletRequest request, HttpServletResponse response, UserDTO userInfo){
		System.out.println("userId :: " + userInfo.getUserId());
		System.out.println("password :: " + userInfo.getPw());
		HttpSession session = request.getSession();

		try {
			userService.auth(request, response, userInfo, session);

			if(session.getAttribute("login").equals("999")){
				response.sendRedirect("/login");
			}else{
				response.sendRedirect("/calendar");
			}
		}catch(Exception e){
			e.printStackTrace();
		}


	}
}
