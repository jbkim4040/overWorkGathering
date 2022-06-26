package com.overWorkGathering.main.controller;


import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.PrivateKey;
import java.security.Security;

import static com.overWorkGathering.main.utils.Common.decryptRsa;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	private static String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
	private static String RSA_INSTANCE = "RSA"; // rsa transformation

	@Autowired
	UserService userService;
	
	@RequestMapping(value="/calendar", method = RequestMethod.GET)
	public String calendar() {
		System.out.println("CalendarCalendarCalendarCalendarCalendar");
		
		return "Calendar";
	}

	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public void auth(@RequestParam("USER_ID") String encrypt_userId, @RequestParam("USER_PW") String encrypt_pw, HttpServletRequest request, HttpServletResponse response){
		try {
			HttpSession session = request.getSession();
			PrivateKey privateKey = (PrivateKey) session.getAttribute(UserController.RSA_WEB_KEY);
			
			// 로그인 정보 복호화
			String userId = decryptRsa(privateKey, encrypt_userId);

			session.removeAttribute(UserController.RSA_WEB_KEY);

			System.out.println("encrypted userID :: " + encrypt_userId);
			System.out.println("decrypted userID :: " + userId);
			System.out.println("encrypted PASSWORD :: " + encrypt_pw);

			userService.auth(userId, encrypt_pw, request, response, session);

			if("999".equals(session.getAttribute("login"))){
				System.out.println(":: Login FAIL ::");
				response.sendRedirect("/login");
			}else{
				System.out.println(":: Login SUCCESS ::");
				response.sendRedirect("/calendar");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public void signUp(@RequestParam("USER_ID") String encrypt_userId, @RequestParam("USER_PW") String encrypt_pw, @RequestParam("USER_NAME") String encrypt_name,
					 @RequestParam("USER_EMAIL") String encrypt_email, @RequestParam("USER_PHONE") String encrypt_phone,
					 @RequestParam("part") String part, @RequestParam("partleader") String partleader,
					 HttpServletRequest request, HttpServletResponse response){
		try {
			HttpSession session = request.getSession();
			PrivateKey privateKey = (PrivateKey) session.getAttribute(UserController.RSA_WEB_KEY);
			System.out.println("encrypted userID :: " + encrypt_userId);
			System.out.println("encrypted userName :: " + encrypt_name);
			System.out.println("encrypted userEmail :: " + encrypt_email);
			System.out.println("encrypted userPhone :: " + encrypt_phone);

			// 회원가입 정보 복호화
			String userId = decryptRsa(privateKey, encrypt_userId);
			String userName = decryptRsa(privateKey, encrypt_name);
			String userEmail = decryptRsa(privateKey, encrypt_email);
			String userPhone = decryptRsa(privateKey, encrypt_phone);

			session.removeAttribute(UserController.RSA_WEB_KEY);


			System.out.println("decrypted userID :: " + userId);
			System.out.println("decrypted userName :: " + userName);
			System.out.println("decrypted userEmail :: " + userEmail);
			System.out.println("decrypted userPhone :: " + userPhone);

			userService.signUp(userId, encrypt_pw, userName, userEmail, userPhone, part, partleader);

			response.sendRedirect("/login");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
