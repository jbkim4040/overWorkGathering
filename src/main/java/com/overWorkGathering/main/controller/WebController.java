package com.overWorkGathering.main.controller;

import com.overWorkGathering.main.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;

import static com.overWorkGathering.main.utils.Common.*;

@Controller
public class WebController {
	
	@Autowired
	WorkService workService;
	
	@RequestMapping(value="/calendar")
	public String calendar() {

		return "Calendar";
	}

	@RequestMapping(value="/login")
	public String login(final HttpServletRequest request) {
		// RSA 키 생성
		initRsa(request);
		String encodedInfo = base64Encoding("test");
		String decodedInfo = base64Decoding(encodedInfo);

		return "Login";
	}
	
	@RequestMapping(value="/SignUp")
	public String SignUp(HttpServletRequest request) {
		// RSA 키 생성
		initRsa(request);

		return "SignUp";
	}
	
	@RequestMapping(value="/FindPw")
	public String FindPw() {
		return "FindPw";
	}
	
	@RequestMapping(value="/CalendarPopup")
	public ModelAndView CalendarPopup(String userID, String workDt) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("CalendarPopup");
		
		return mav;
	}


}
