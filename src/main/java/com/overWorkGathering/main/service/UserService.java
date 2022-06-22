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


	public boolean auth(HttpServletRequest request, HttpServletResponse response, UserDTO userInfo) throws Exception{
		UserEntity user = userRepository.findByUserIdAndPw(userInfo.getUserId(), userInfo.getPw());
		HttpSession session = request.getSession();

		System.out.println("ID >>>>> " + user.getUserId());
		if(user.getUserId() == null || user.getUserId().equals("")){
			return false;
		};
		session.setAttribute("ID", user.getUserId());
		return true;
	}


}
