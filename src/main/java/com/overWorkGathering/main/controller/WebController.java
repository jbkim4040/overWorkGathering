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
import java.util.UUID;

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

		return "Login";
	}
	
	@RequestMapping(value="/SignUp")
	public String SignUp(HttpServletRequest request) {
		// RSA 키 생성
		initRsa(request);

		// 임시 ID 발급
		HttpSession session = request.getSession();
		session.setAttribute("tempId", UUID.randomUUID().toString().replace("-", ""));
		session.setAttribute("idDupChk", "N");
		session.setAttribute("emailChk", "N");

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

	@RequestMapping(value="/Master")
	public String Master(final HttpServletRequest request) {
		// RSA 키 생성
		initRsa(request);

		return "Master";
	}

	@RequestMapping(value="/Master_dtl")
	public String Master_dtl(final HttpServletRequest request) {
		// RSA 키 생성
		initRsa(request);

		return "Master_dtl";
	}

	@RequestMapping(value="/User_dtl")
	public String User_dtl(final HttpServletRequest request) {
		// RSA 키 생성
		initRsa(request);

		return "User_dtl";
	}
}
