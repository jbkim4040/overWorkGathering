package com.overWorkGathering.main.controller;


import com.overWorkGathering.main.DTO.PrssRsltDTO;
import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.service.UserService;
import com.overWorkGathering.main.utils.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.Security;
import java.util.HashMap;

import static com.overWorkGathering.main.utils.Common.*;

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
			String pw = decryptRsa(privateKey, encrypt_pw);

			session.removeAttribute(UserController.RSA_WEB_KEY);

			System.out.println("encrypted userID :: " + encrypt_userId);
			System.out.println("decrypted userID :: " + userId);
			System.out.println("encrypted PASSWORD :: " + encrypt_pw);

			userService.auth(userId, pw, request, response, session);

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

	@RequestMapping(value = "/logOut", method = RequestMethod.POST)
	public void logOut(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession();
			session.invalidate();

			System.out.println(":: LogOut ::");
			response.sendRedirect("/WEB-INF/views/login");
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

			// 회원가입 정보 복호화
			String userId = decryptRsa(privateKey, encrypt_userId);
			String password = decryptRsa(privateKey, encrypt_pw);
			String userName = decryptRsa(privateKey, encrypt_name);
			String userEmail = decryptRsa(privateKey, encrypt_email);
			String userPhone = decryptRsa(privateKey, encrypt_phone);

			session.removeAttribute(UserController.RSA_WEB_KEY);

			HashMap<String, String> map = hashingPASSWORD(password, "");

			userService.signUp(userId, map.get("password"), userName, userEmail, userPhone, part, partleader, map.get("salt"));

			response.sendRedirect("/login");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/dupIdChk", method = RequestMethod.POST)
	public String duplicatedIdCheck(@RequestParam("USER_ID") String encrypt_userId, HttpServletRequest request){
		String dupYn = "N";

		try {
			HttpSession session = request.getSession();
			PrivateKey privateKey = (PrivateKey) session.getAttribute(UserController.RSA_WEB_KEY);

			// 회원가입 정보 복호화
			String userId = decryptRsa(privateKey, encrypt_userId);

			dupYn = userService.duplicatedIdCheck(userId);
		}catch(Exception e){
			dupYn = e.getMessage();
		}

		return dupYn;
	}

	@RequestMapping(value = "/sendMail", method = RequestMethod.POST)
	public PrssRsltDTO sendMail(@RequestParam("USER_EMAIL") String encrypt_userEmail, HttpServletRequest request){
		PrssRsltDTO prssRsltDTO = PrssRsltDTO.builder().prssYn("N").prssMsg("").build();

		try{
			HttpSession session = request.getSession();

			PrivateKey privateKey = (PrivateKey) session.getAttribute(UserController.RSA_WEB_KEY);

			// 회원가입 정보 복호화
			String mail = decryptRsa(privateKey, encrypt_userEmail);
			String code = userService.sendCode(session, mail);

			prssRsltDTO.setPrssYn("Y");
			prssRsltDTO.setPrssMsg("메일 전송 성공");
			prssRsltDTO.setContent(StringUtils.isEmpty(code) ? "error" : code);
		}catch (Exception e){
			prssRsltDTO.setPrssYn("N");
			prssRsltDTO.setPrssMsg(e.getMessage());
		}

		return prssRsltDTO;
	}
}
