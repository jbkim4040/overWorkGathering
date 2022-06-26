package com.overWorkGathering.main.service;

import com.overWorkGathering.main.DTO.UserDTO;
import com.overWorkGathering.main.controller.WebController;
import com.overWorkGathering.main.entity.UserEntity;
import com.overWorkGathering.main.repository.UserRepository;
import com.overWorkGathering.main.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

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


	public void auth(String userId, String pw, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
		UserEntity user = userRepository.findByUserIdAndPw(userId, pw);

		// 로그인 코드가 999 : 실패     000 : 성공
		if(ObjectUtils.isEmpty(user)){
			session.setAttribute("login", "999");
		}else{
			session.setAttribute("login", "000");
			session.setAttribute("userId", user.getUserId());
			session.setAttribute("userName", user.getName());
			session.setAttribute("auth", user.getAuth());
		}

	}

	public boolean duplicatedIdCheck(String userId) throws Exception{
		UserEntity user = userRepository.findByUserId(userId);

		return ObjectUtils.isEmpty(user);
	}


	public void signUp(String userId, String pw, String name, String email,
					   String phone, String part, String partleader) throws Exception{

		UserEntity user = UserEntity.builder()
				.userId(userId)
				.pw(pw)
				.name(name)
				.email(email)
				.phone(phone)
				.part(part)
				.partleader(partleader)
				.auth(Constant.Auth.U.code)		// 기본 권한은 U( 일반사용자 )
				.build();

		userRepository.save(user);
	}
}
