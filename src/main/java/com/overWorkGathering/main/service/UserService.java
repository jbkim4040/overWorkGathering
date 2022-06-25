package com.overWorkGathering.main.service;

import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.controller.WebController;
import com.overWorkGathering.main.entity.UserEntity;
import com.overWorkGathering.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.PrivateKey;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;

	WebController webController;

	private static String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
	private static String RSA_INSTANCE = "RSA"; // rsa transformation


	public void auth(HttpServletRequest request, HttpServletResponse response, UserDTO userInfo, HttpSession session) throws Exception{
		UserEntity user = userRepository.findByUserIdAndPw(userInfo.getUserId(), userInfo.getPw());

		// 로그인 코드가 999 : 실패     000 : 성공
		if(user.getUserId() == null || user.getUserId().equals("")){
			response.setStatus(999);
			session.setAttribute("login", "999");
			System.out.println("login CODE >>>>> " + (String) session.getAttribute("login"));
		}else{
			response.setStatus(000);
			session.setAttribute("userInfo", user);
			session.setAttribute("userId", user.getUserId());
			session.setAttribute("auth", user.getAuth());
			session.setAttribute("login", "000");
			System.out.println("login CODE >>>>> " + (String) session.getAttribute("login"));
		}

	}


}
