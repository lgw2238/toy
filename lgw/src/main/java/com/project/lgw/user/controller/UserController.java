package com.project.lgw.user.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.lgw.base.controller.BaseController;
import com.project.lgw.user.service.UserService;
import com.project.lgw.user.vo.UserVo;
import com.project.lgw.user.vo.KakaoVo;
import com.project.lgw.user.vo.NaverVo;
import com.project.lgw.user.vo.SnsApiResponse.KakaoAccount;
import com.project.lgw.user.vo.UserProfile;

@RequestMapping(value = "/user")
@Controller
public class UserController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	   
	 @Value("${api.naver.client_id}")
	 private String client_id;

     @Value("${api.naver.client_secret}")
     private String client_secret;
     
     @Value("${api.naver.redirect.uri}")
     private String naver_redirect_uri;
     
	 @Value("${api.kakao.client_id}")
	 private String kakao_client_id;

     @Value("${api.kakao.auth_url}")
     private String kakaoAuthUrl;
     
     @Value("${api.kakao.redirect.uri}")
     private String kakako_redirect_uri;
     
	 @Autowired
	 UserService userSerivce;
	 
	 	// 유저 가입 정보 입력  
		@RequestMapping(value = "/insertUser")
	    public String insertUserInfo(HttpServletRequest request, Model model) {
			logger.debug("==============================================================");
			logger.info("session id : {} ", request.getSession().getId());
			logger.debug("==============================================================");
			System.out.println("signUp start");
	
	       
			model.addAttribute("date", "");
	        return "main";
	        
	    }
		
		// 유저 아이디 유효성 체크
		@ResponseBody
		@RequestMapping(value = "/userIdChk")
	    public int idChk(HttpServletRequest request, Model model, UserVo userVo) {
			
			String inputValue = userVo.getUserId();
			String message = "";
	        int idChk = 0;
			try {
				idChk = userSerivce.idChk(userVo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message = "유저 체크 로직 중 에러가 발생했습니다.";
			}
			
			logger.info("idChk:", idChk);
	        return idChk;
	        
	    }
		
		// 일반 회원 가입
		@RequestMapping(value = "/signUp")
	    public void signUp(HttpServletRequest request,
	    		HttpServletResponse response, Model model, @RequestBody UserVo userVo ) throws Exception {
			
			int result = 0;
			String resultCode = "";
			String resultMsg = "";
			PrintWriter out = response.getWriter();
			try {
				result = userSerivce.signUp(userVo);
				resultCode = "200";
				resultMsg = "회원가입 완료되었습니다.";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultCode = "100";
				resultMsg = "회원가입에 실패하였습니다.";
				
			}
			
			out.print(getResultModel(resultCode, resultMsg, null, null));
	        
	    }
		
		// 네이버 로그인 창 호출
		@RequestMapping(value="/login/naver", method=RequestMethod.GET)
	    public String loginNaver(HttpServletRequest request) throws UnsupportedEncodingException {
	        String state = UUID.randomUUID().toString();
	        String sessionState = generateSessionState(request);
	        
	        String authorizeUrl = "https://nid.naver.com/oauth2.0/authorize";
	        authorizeUrl += "?response_type=code";
	        authorizeUrl += "&client_id=" + client_id;
	        authorizeUrl += "&redirect_uri=" + URLEncoder.encode(naver_redirect_uri, "UTF-8");
	        authorizeUrl += "&state=" + state;
	        authorizeUrl += "&session_state=" + sessionState;
	        
	        return "redirect:" + authorizeUrl;
	    }
	    
		/* naver Login Callback */
	    @RequestMapping(value="/naverCallback", method=RequestMethod.GET)
	    public String loginCallbackNaver(@RequestParam Map<String, String> resValue,
	    		Model model) throws Exception {
	    	logger.info("naverLogin start");
	    	logger.info("===========naver Login Info============");
	    	logger.info(resValue.toString());
	    	logger.info("=======================================");

			// code 를 받아오면 code 를 사용하여 access_token를 발급받는다. 
	    	final NaverVo naverLoginVo = userSerivce.requestNaverLoginAcceccToken(resValue,"authorization_code");
			  
			// access_token를 사용하여 사용자의 고유 id값을 가져온다. 
	    	final UserProfile naverLoginProfile = userSerivce.requestNaverLoginProfile(naverLoginVo);
			  
	    	int result = userSerivce.validationOauthJoin(naverLoginProfile);

	    	String resultMessage = "";
	    	if(result == 1) { // 가입 가능 유저
	    		logger.info("가입 불가능상태입니다.");
	    		resultMessage = "해당 고객은 해당 SNS에 이미 가입이 되어 있는 상태입니다.";
	    	}else { // 비가입 유저
	    		logger.info("가입 가능상태입니다.");
	    		resultMessage = "해당 고객은  가입 가능한 상태입니다.";
	    	}
	    	
	        String date = getTodayDate();
	        
	    	model.addAttribute("id", naverLoginProfile.getNickname());
	    	model.addAttribute("date", date);
	    	logger.info("===========naver response Info============");
	    	logger.info(naverLoginProfile.toString());
	    	logger.info(resultMessage);
	    	logger.info("=======================================");
	    	return "loginSuccess";
	    }
	    
	    // 카카오 로그인창 호출
	    @ResponseBody
	    @RequestMapping(value = "/login/kakao")
	    public String loginKakao(HttpServletRequest request) throws Exception {
	    	String reqUrl = kakaoAuthUrl + "/oauth/authorize?client_id=" + kakao_client_id + "&redirect_uri="+ kakako_redirect_uri + "&response_type=code";
	    	return reqUrl;
	    }
	    
	    // 카카오 로그인 API - Callback 
	    @RequestMapping(value="/kakaoCallback", method=RequestMethod.GET)
	    public String loginCallbackKakao(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> resValue,
	    		Model model) throws Exception {
	    	    	
	    	// code 를 받아오면 code 를 사용하여 access_token를 발급받는다. 
	    	final KakaoVo kakaoLoginVo = userSerivce.requestKakaoLoginAcceccToken(resValue,"authorization_code", kakao_client_id);
			// access_token를 사용하여 사용자의 고유 id값을 가져온다. 
	    	final ResponseEntity<Map<String, String>> kakaoLoginProfile = userSerivce.requestKakaoLoginProfile(kakaoLoginVo, kakao_client_id);
	    	
	    	Map<String, String> responseMap = kakaoLoginProfile.getBody();

	    	UserProfile kakaoProfile = new UserProfile();
	    	kakaoProfile.setNickname(responseMap.get("nickname"));
	    	int result = userSerivce.validationOauthJoin(kakaoProfile);

	    	String resultMessage = "";
	    	if(result == 1) { // 가입 가능 유저
	    		logger.info("가입 불가능상태입니다.");
	    		resultMessage = "해당 고객은 해당 SNS에 이미 가입이 되어 있는 상태입니다.";
	    	}else { // 비가입 유저
	    		logger.info("가입 가능상태입니다.");
	    		resultMessage = "해당 고객은  가입 가능한 상태입니다.";
	    	}
	    	
	        String date = getTodayDate();
	        
	    	model.addAttribute("id", kakaoProfile.getNickname());
	    	model.addAttribute("date", date);
	    	model.addAttribute("resultMessage", resultMessage);
	    	
	    	return "loginSuccess";
	    }
 
	    private String generateSessionState(HttpServletRequest request) {
	        String referer = request.getHeader("Referer");
	        String userAgent = request.getHeader("User-Agent");
	        String remoteAddr = request.getRemoteAddr();
	        
	        String sessionState = referer + userAgent + remoteAddr;
	        sessionState = DigestUtils.sha256Hex(sessionState);
	        
	        HttpSession session = request.getSession(true);
	        session.setAttribute("session_state", sessionState);
	        
	        return sessionState;
	    }
	    
	    private String getTodayDate() {
	    	LocalDate now = LocalDate.now();		        
	        // 현재 날짜 구하기(지역:한국-서울 기준)
	        LocalDate seoulNow = LocalDate.now(ZoneId.of("Asia/Seoul"));
	        int year = seoulNow.getYear();
	        String month = seoulNow.getMonth().toString();
	        int dayOfMonth = seoulNow.getDayOfMonth();
		       	            
		    String dateString = month+" "+Integer.toString(dayOfMonth)+","+Integer.toString(year);
		    
		    return dateString;
	    }
}

