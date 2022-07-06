package com.overWorkGathering.main.service;

import com.overWorkGathering.main.controller.WebController;
import com.overWorkGathering.main.entity.UserInfoEntity;
import com.overWorkGathering.main.repository.UserRepository;
import com.overWorkGathering.main.utils.Constant;
import com.overWorkGathering.main.utils.SHA256;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.overWorkGathering.main.utils.Common.codeGenerator;
import static com.overWorkGathering.main.utils.Common.hashingPASSWORD;

@Slf4j
@Service
public class UserService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private UserRepository userRepository;

	private static String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
	private static String RSA_INSTANCE = "RSA"; // rsa transformation


	public void auth(String userId, String pw, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		UserInfoEntity user = null;

		try{
			if(userId.equals("hirofac") || userId.equals("jbkim4040") || userId.equals("jhyuk97")){
				user = userRepository.findByUserIdAndPw(userId, pw);
			}else{
				String salt = userRepository.findByUserId(userId).getSalt();
				HashMap<String, String> map = hashingPASSWORD(pw, salt);
				System.out.println("아이디 :: " + userId);
				System.out.println("비밀번호 :: " + map.get("password"));

				user = userRepository.findByUserIdAndPw(userId, map.get("password"));

				System.out.println("로그인 성공 :: " + ObjectUtils.isEmpty(user));
			}
		} catch(Exception e){

		}



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

	public String duplicatedIdCheck(String userId) throws Exception{
		UserInfoEntity user = userRepository.findByUserId(userId);

		return ObjectUtils.isEmpty(user) ? "N" : "Y";
	}


	public void signUp(String userId, String pw, String name, String email,
					   String phone, String part, String partleader, String salt) throws Exception{

		UserInfoEntity user = UserInfoEntity.builder()
				.userId(userId)
				.pw(pw)
				.name(name)
				.email(email)
				.phone(phone)
				.part(part)
				.partleader(partleader)
				.salt(salt)
				.auth(Constant.Auth.U.code)		// 기본 권한은 U( 일반사용자 )
				.build();

		userRepository.save(user);
	}

	public String sendCode(HttpSession session, String mail)throws MessagingException, UnsupportedEncodingException {
		String to = mail;
		String from = "jbkim404037@gmail.com";
		String subject = "이메일 확인 코드 전송 테스트";
		String code = codeGenerator(4);

		StringBuilder body = new StringBuilder();
		body.append("<html> <body><h1>"+ code + "</h1>");
		body.append("<div>테스트 입니다. </div> </body></html>");

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

		mimeMessageHelper.setFrom(from,"김정빈");
		mimeMessageHelper.setTo(to);
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setText(body.toString(), true);

		javaMailSender.send(message);

		try{
			SHA256 SHA256 = new SHA256();

			code = SHA256.encrypt(code);
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}

		return code;
	}
}
