package com.overWorkGathering.main.controller;


import com.overWorkGathering.main.DTO.PartInfoDTO;
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
import java.util.List;

import static com.overWorkGathering.main.utils.Common.*;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	private final String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
	private final String RSA_INSTANCE = "RSA"; // rsa transformation

	@Autowired
	UserService userService;

	@RequestMapping(value="/calendar", method = RequestMethod.GET)
	public String calendar() {
		System.out.println("CalendarCalendarCalendarCalendarCalendar");

		return "Calendar";
	}

	@RequestMapping(value="/allPartInfo", method = RequestMethod.GET)
	public List<PartInfoDTO> retrieveAllPartInfo(){
		return userService.retrieveAllPartInfo();
	}


	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public void auth(@RequestParam("USER_ID") String encrypt_userId, @RequestParam("USER_PW") String encrypt_pw, HttpServletRequest request, HttpServletResponse response){
		String resultCd = userService.auth(encrypt_userId, encrypt_pw, request, response);

		try {
			if ("999".equals(resultCd)) {
				System.out.println(":: Login FAIL ::");
				response.sendRedirect("/login");
			} else {
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
			response.sendRedirect("/login");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public void signUp(@RequestParam("USER_ID") String encrypt_userId, @RequestParam("USER_PW") String encrypt_pw, @RequestParam("USER_NAME") String encrypt_name,
					   @RequestParam("USER_EMAIL") String encrypt_email, @RequestParam("USER_PHONE") String encrypt_phone,
					   @RequestParam("USER_ACCOUNT") String encrypt_account, @RequestParam("part") String part, @RequestParam("rank") String rank,
					   HttpServletRequest request, HttpServletResponse response){
		try {
			HttpSession session = request.getSession();
			PrivateKey privateKey = (PrivateKey) session.getAttribute(RSA_WEB_KEY);

			// 회원가입 정보 복호화
			String userId = decryptRsa(privateKey, encrypt_userId);
			String password = decryptRsa(privateKey, encrypt_pw);
			String userName = decryptRsa(privateKey, encrypt_name);
			String userEmail = decryptRsa(privateKey, encrypt_email);
			String userPhone = decryptRsa(privateKey, encrypt_phone);
			String userAccount = decryptRsa(privateKey, encrypt_account);

			String tempId = (String)session.getAttribute("tempId");

			session.removeAttribute(RSA_WEB_KEY);

			HashMap<String, String> map = hashingPASSWORD(password, "");

			userService.signUp(tempId, userId, map.get("password"), userName, userEmail, userPhone, userAccount, part, map.get("salt"), rank);

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
			PrivateKey privateKey = (PrivateKey) session.getAttribute(RSA_WEB_KEY);

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
			PrivateKey privateKey = (PrivateKey) session.getAttribute(RSA_WEB_KEY);

			// 회원가입 정보 복호화
			String mail = decryptRsa(privateKey, encrypt_userEmail);
			userService.sendCode(prssRsltDTO, mail);

		}catch (Exception e){
			prssRsltDTO.setPrssYn("N");
			prssRsltDTO.setPrssMsg(e.getMessage());
		}

		return prssRsltDTO;
	}

	@RequestMapping(value = "/temp/info", method = RequestMethod.POST)
	public void saveTempInfo(HttpServletRequest request){
		userService.saveTempInfo(request);
	}

	@RequestMapping(value = "/temp/info", method = RequestMethod.DELETE)
	public void deleteTempInfo(HttpServletRequest request){
		userService.deleteTempInfo(request);
	}
}
