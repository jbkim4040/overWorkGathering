package com.overWorkGathering.main.service;

import com.overWorkGathering.main.DTO.PartInfoDTO;
import com.overWorkGathering.main.DTO.PrssRsltDTO;
import com.overWorkGathering.main.controller.WebController;
import com.overWorkGathering.main.entity.PartInfoEntity;
import com.overWorkGathering.main.entity.UserInfoEntity;
import com.overWorkGathering.main.mapper.PartInfoMapper;
import com.overWorkGathering.main.repository.PartInfoRepository;
import com.overWorkGathering.main.repository.UserRepository;
import com.overWorkGathering.main.utils.Constant;
import com.overWorkGathering.main.utils.SHA256;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.overWorkGathering.main.utils.Common.*;
import static com.overWorkGathering.main.utils.Common.decryptRsa;

@Slf4j
@Service
public class UserService {

	@Value("${spring.profiles.active}")
	private String currentEnvironment;
	private final String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
	private final String RSA_INSTANCE = "RSA"; // rsa transformation

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PartInfoRepository partInfoRepository;

	@Autowired
	private PartInfoMapper partInfoMapper;

	@Autowired
	MessageSource messageSource;


	public List<PartInfoDTO> retrieveAllPartInfo(){
		List<PartInfoEntity> partInfoEntityList = partInfoRepository.findAll();

		return partInfoMapper.toPartINfoDTOList(partInfoEntityList);
	}

	public String auth(String encrypt_userId, String encrypt_pw, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		PrivateKey privateKey = (PrivateKey) session.getAttribute(RSA_WEB_KEY);
		UserInfoEntity user = null;
		String resultCd = "000";

		try{
			// 로그인 정보 복호화
			String userId = decryptRsa(privateKey, encrypt_userId);
			String pw = decryptRsa(privateKey, encrypt_pw);

			session.removeAttribute(RSA_WEB_KEY);

			user = userRepository.findByUserId(userId);

			if(!ObjectUtils.isEmpty(user)){
				String salt = user.getSalt();
				HashMap<String, String> map = hashingPASSWORD(pw, salt);

				user = userRepository.findByUserIdAndPw(userId, map.get("password"));
			}



			// 로그인 코드가 999 : 실패     000 : 성공
			if(ObjectUtils.isEmpty(user)){
				resultCd = "999";
				session.setAttribute("login", resultCd);
			}else{
				resultCd = "000";
				session.setAttribute("login", resultCd);
				session.setAttribute("userId", user.getUserId());
				session.setAttribute("userName", user.getName());
				session.setAttribute("userPart", user.getPart());
				session.setAttribute("auth", user.getAuth());
			}
		} catch(Exception e){
			resultCd = "999";
			session.setAttribute("login", resultCd);
		}

		return resultCd;
	}

	public String duplicatedIdCheck(String userId) throws Exception{
		UserInfoEntity user = userRepository.findByUserId(userId);

		return ObjectUtils.isEmpty(user) ? "N" : "Y";
	}

	public void saveTempInfo (HttpServletRequest request){
		System.out.println("임시 데이터 저장");
		HttpSession session = request.getSession();
		String tempId = (String)session.getAttribute("tempId");

		UserInfoEntity user = UserInfoEntity.builder()
				.userId(tempId)
				.pw("")
				.name("")
				.email("")
				.phone("")
				.part("")
				.partleader("")
				.salt("")
				.auth("")		// 기본 권한은 U( 일반사용자 )
				.acnt("")
				.build();

		userRepository.save(user);
	}

	public void deleteTempInfo (HttpServletRequest request){
		System.out.println("임시 데이터 삭제");
		HttpSession session = request.getSession();
		String tempId = (String)session.getAttribute("tempId");

		userRepository.deleteById(tempId);

		session.removeAttribute("tempId");
		session.removeAttribute("idDupChk");
		session.removeAttribute("emailChk");
	}


	public void signUp(String tempId, String userId, String pw, String name, String email,
					   String phone, String account, String partCd, String salt) throws Exception{
		String partLeader = partInfoRepository.findByPartCd(partCd).getPartLeader();

		UserInfoEntity user = UserInfoEntity.builder()
				.userId(userId)
				.pw(pw)
				.name(name)
				.email(email)
				.phone(phone)
				.part(partCd)
				.partleader(partLeader)
				.salt(salt)
				.auth(Constant.Auth.U.code)		// 기본 권한은 U( 일반사용자 )
				.acnt(account)
				.build();

		userRepository.save(user);
		userRepository.deleteById(tempId);
	}

	public void sendCode(PrssRsltDTO prssRsltDTO, String mail) {
		String code = codeGenerator(4);

		try{
			/** 전송할 메일 내용 세팅 */
			String to = mail;
			String from = "hirofac@naver.com";
			String subject = "야근식대 홈페이지 이메일 인증 코드";

			StringBuilder body = new StringBuilder();
			body.append("<html> <body><h1>"+ code + "</h1>");
			body.append("<div>안녕하세요. \n" +
					"야근식대 홈페이지 회원가입 인증입니다.\n" +
					"해당 코드를 입력해주세요. </div> </body></html>");

			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

			mimeMessageHelper.setFrom(from,"애버커스");
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(body.toString(), true);

			/** 코드 암호화 */
			SHA256 SHA256 = new SHA256();
			code = SHA256.encrypt(code);

			/** 메일전송 */
			javaMailSender.send(message);

			prssRsltDTO.setPrssYn("Y");
			prssRsltDTO.setPrssMsg("메일 전송 성공");
			prssRsltDTO.setContent(code);
		}catch(NoSuchAlgorithmException e){
			prssRsltDTO.setPrssYn("N");
			prssRsltDTO.setPrssMsg("메일 전송 실패 : 코드 암호화 에러");
			prssRsltDTO.setContent("");
		}catch(UnsupportedEncodingException uee){
			prssRsltDTO.setPrssYn("N");
			prssRsltDTO.setPrssMsg("메일 전송 실패 : 적절치못한 문자 사용");
			prssRsltDTO.setContent("");
		}catch(MessagingException me){
			prssRsltDTO.setPrssYn("N");
			prssRsltDTO.setPrssMsg("메일 전송 실패 : 잘못된 메일 정보");
			prssRsltDTO.setContent("");
		}
	}
}
