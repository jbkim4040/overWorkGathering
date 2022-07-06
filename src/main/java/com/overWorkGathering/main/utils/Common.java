package com.overWorkGathering.main.utils;

import com.overWorkGathering.main.controller.UserController;
import com.overWorkGathering.main.controller.WebController;
import org.springframework.stereotype.Controller;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;

import static com.sun.javafx.font.FontResource.SALT;

@Controller
public class Common {
    private static String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
    private static String RSA_INSTANCE = "RSA"; // rsa transformation
    private static final int SALT_SIZE = 16;

    public final static void initRsa(HttpServletRequest request) {
        HttpSession session = request.getSession();

        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(Common.RSA_INSTANCE);
            generator.initialize(1024);

            KeyPair keyPair = generator.genKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance(Common.RSA_INSTANCE);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            session.setAttribute(Common.RSA_WEB_KEY, privateKey); // session에 RSA 개인키를 세션에 저장

            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

            request.setAttribute("RSAModulus", publicKeyModulus); // rsa modulus 를 request 에 추가
            request.setAttribute("RSAExponent", publicKeyExponent); // rsa exponent 를 request 에 추가
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        //AES256 방식으로 암호화하기위한 설정
        Security.setProperty("crypto.policy", "unlimited");

        System.out.println("privateKey :: " + privateKey);
        System.out.println("securedValue :: " + securedValue);
        Cipher cipher = Cipher.getInstance(Common.RSA_INSTANCE);
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }

    public final static  byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) { return new byte[] {}; }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }

    /**
     * Base64 인코딩
     * @param info
     * @return
     */
    public final static String base64Encoding(String info){
        byte[] message = info.getBytes(StandardCharsets.UTF_8);
        String encodedInfo = Base64.getEncoder().encodeToString(message);
        return encodedInfo;
    }

    /**
     * Base64 디코딩
     * @param info
     * @return
     */
    public final static String base64Decoding(String info){
        byte[] decoded = Base64.getDecoder().decode(info);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    /**
     * 비밀번호 암호화
     * @param password
     * @return
     */
    public static final HashMap<String, String> hashingPASSWORD(String password, String userSALT){
        String hashedPASSWORD = "";
        HashMap<String, String> map = new HashMap<>();

        try {
            byte[] PW = password.getBytes();

            String SALT = userSALT == "" ? getSALT() : userSALT;

            // 첫번째 해싱
            hashedPASSWORD = Hashing(PW, SALT);

            // 두번째 해싱
            hashedPASSWORD = Hashing(hashedPASSWORD.getBytes(), SALT);

            // 세번째 해싱
            hashedPASSWORD = Hashing(hashedPASSWORD.getBytes(), SALT);

            map.put("password", hashedPASSWORD);
            map.put("salt", SALT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    private static String Hashing(byte[] password, String Salt) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");	// SHA-256 해시함수를 사용

        // key-stretching
        for(int i = 0; i < 10000; i++) {
            String temp = Byte_to_String(password) + Salt;	// 패스워드와 Salt 를 합쳐 새로운 문자열 생성
            md.update(temp.getBytes());						// temp 의 문자열을 해싱하여 md 에 저장해둔다
            password = md.digest();							// md 객체의 다이제스트를 얻어 password 를 갱신한다
        }

        return Byte_to_String(password);
    }

    private static String getSALT() throws Exception {
        SecureRandom rnd = new SecureRandom();
        byte[] temp = new byte[SALT_SIZE];
        rnd.nextBytes(temp);

        return Byte_to_String(temp);
    }

    private static String Byte_to_String(byte[] temp) {
        StringBuilder sb = new StringBuilder();
        for(byte a : temp) {
            sb.append(String.format("%02x", a));
        }
        return sb.toString();
    }

    public static String codeGenerator(int codeLength){
        StringBuffer buf =new StringBuffer();

        for(int i=0;i<codeLength;i++){
            buf.append((char)((int)(Math.random()*26)+65));
        }

        return buf.toString();
    }

}
