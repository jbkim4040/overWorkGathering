package com.overWorkGathering.main.controller;

import com.overWorkGathering.main.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;

@Controller
public class WebController {
	
	@Autowired
	WorkService workService;

	private static String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
	private static String RSA_INSTANCE = "RSA"; // rsa transformation
	
	@RequestMapping(value="/calendar")
	public String calendar() {

		return "Calendar";
	}

	@RequestMapping(value="/login")
	public String login(final HttpServletRequest request) {
		HttpSession session = request.getSession();
 
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(RSA_INSTANCE);
            generator.initialize(1024);
 
            KeyPair keyPair = generator.genKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_INSTANCE);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
 
            session.setAttribute(RSA_WEB_KEY, privateKey); // session에 RSA 개인키를 세션에 저장
 
            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
 
            request.setAttribute("RSAModulus", publicKeyModulus); // rsa modulus 를 request 에 추가
            request.setAttribute("RSAExponent", publicKeyExponent); // rsa exponent 를 request 에 추가
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Login";
	}
	
	@RequestMapping(value="/SignUp")
	public String SignUp() {
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
