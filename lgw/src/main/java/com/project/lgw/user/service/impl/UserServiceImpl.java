package com.project.lgw.user.service.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.project.lgw.user.mapper.UserMapper;
import com.project.lgw.user.service.UserService;
import com.project.lgw.user.vo.UserVo;
import com.project.lgw.user.vo.KakaoVo;
import com.project.lgw.user.vo.NaverVo;
import com.project.lgw.user.vo.SnsApiResponse;
import com.project.lgw.user.vo.SnsApiResponse.KakaoAccount;
import com.project.lgw.user.vo.UserProfile;

@Service
public class UserServiceImpl implements UserService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserMapper userMapper;
	
    @Autowired
    private WebClient webClient;

    @Value("${api.naver.client_id}")
    private String client_id;

    @Value("${api.naver.client_secret}")
    private String client_secret;
    
	@Override
	public int idChk(UserVo userVo) throws Exception {
		int result = userMapper.idChk(userVo);
		return result;
	}
	
	@Override
	public int signUp(UserVo userVo) throws Exception {
		userVo.setUserType("N");
		userVo.setUserStatus("U");
		
		int returnValue = 0;
		int result = userMapper.insertUserInfo(userVo);		
		int result2 = userMapper.insertUserDetailInfo(userVo);
		
		if(result == 1) {
			returnValue = 1;
		}else if(result2 != 1) {
			returnValue = 0;
			
		}else {
			returnValue = 1;
		}
		return returnValue;
	}
	
	/* naver sns Login*/

    /**
     * @description Naver 로그인을 위하여 Access_tokin을 발급받음
     * @param resValue
     *          1) code: 토큰 발급용 1회용 코드
     *          2) state: CORS 를 방지하기 위한 임의의 토큰
     * @param grant_type
     *          1) 발급:'authorization_code'
     *          2) 갱신:'refresh_token'
     *          3) 삭제: 'delete'
     * @return
     */
    public NaverVo requestNaverLoginAcceccToken(Map<String, String> resValue, String grant_type){
        final String uri = UriComponentsBuilder
                .fromUriString("https://nid.naver.com")
                .path("/oauth2.0/token")
                .queryParam("grant_type", grant_type)
                .queryParam("client_id", this.client_id)
                .queryParam("client_secret", this.client_secret)
                .queryParam("code", resValue.get("code"))
                .queryParam("state", resValue.get("state"))
                .queryParam("refresh_token", resValue.get("refresh_token")) // Access_token 갱신시 사용
                .build()
                .encode()
                .toUriString();

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(NaverVo.class)
                .block();
    }

    // ----- 프로필 API 호출 (Unique한 id 값을 가져오기 위함) -----
    public UserProfile requestNaverLoginProfile(NaverVo naverLoginVo){
        final String profileUri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/nid/me")
                .build()
                .encode()
                .toUriString();

        return webClient
                .get()
                .uri(profileUri)
                .header("Authorization", "Bearer " + naverLoginVo.getAccess_token())
                .retrieve()
                .bodyToMono(SnsApiResponse.class)
                .block()
                .getResponse(); // NaverLoginProfile 은 건네준다.
    }

    /* 소셜 로그인 회원 정보 확인 */
	@Override
	public int validationOauthJoin(UserProfile userProfile) throws Exception {
		int result = 0;
		/* DB - 유저정보 조회 */
		result = userMapper.findOauthUserInfo(userProfile);
				
		return result;
	}

	/* 카카오 로그인 API restTemplate - accessToken  */
	@Override
	public String requestKaKaoLoginAcceccToken(String code, String kakaoAuthUrl, String kakaoApiKey, String redirectURI) {
			String accessToken = "";

		    // restTemplate을 사용하여 API 호출
		    RestTemplate restTemplate = new RestTemplate();
		    String reqUrl = "/oauth/token";
		    URI uri = URI.create(kakaoAuthUrl + reqUrl);

		    HttpHeaders headers = new HttpHeaders();

		    MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		    parameters.set("grant_type", "authorization_code");
		    parameters.set("client_id", kakaoApiKey);
		    parameters.set("redirect_uri", redirectURI);
		    parameters.set("code", code);

		    HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(parameters, headers);
		    ResponseEntity<JSONObject> apiResponse = restTemplate.postForEntity(uri, restRequest, JSONObject.class);
		    JSONObject responseBody = apiResponse.getBody();
		    	
		    logger.info("Kakao responseBody :{}", responseBody);
		    accessToken = (String) responseBody.get("access_token");

		    return accessToken;
	}
	
	/* 카카오 로그인 API restTemplate */
	public String getKakaoUniqueNo(String accessToken, String kakaoApiKey) throws Exception {

	    String kakaoUniqueNo = "";
	    String KakaoApiUrl ="https://kapi.kakao.com";
	    // restTemplate을 사용하여 API 호출
	    RestTemplate restTemplate = new RestTemplate();
	    String reqUrl = "/v2/user/me";
	    URI uri = URI.create(KakaoApiUrl + reqUrl);

	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Authorization", "bearer " + accessToken);
	    headers.set("KakaoAK", kakaoApiKey);

	    MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
	    parameters.add("property_keys", "[\"id\"]");
	    parameters.add("property_keys", "[\"kakao_account.name\"]");
	    parameters.add("property_keys", "[\"kakao_account.email\"]");
	    
	    HttpEntity<MultiValueMap<String, Object>> restRequest = new HttpEntity<>(parameters, headers);
	    ResponseEntity<JSONObject> apiResponse = restTemplate.postForEntity(uri, restRequest, JSONObject.class);
	    JSONObject responseBody = apiResponse.getBody();
	   
	    logger.info("responseBody:{}", responseBody);
	    kakaoUniqueNo =  responseBody.getAsString("id");
		/* Integer.toString(responseBody.gent("id")); */

	    return kakaoUniqueNo;

	}
	
	
	 public KakaoVo requestKakaoLoginAcceccToken(Map<String, String> resValue, String grant_type, String kakao_client_id){
		    logger.info("resValue:{}" , resValue.toString());
	        final String uri = UriComponentsBuilder
	                .fromUriString("https://kauth.kakao.com")
	                .path("/oauth/token")
	                .queryParam("grant_type", grant_type)
	                .queryParam("client_id", kakao_client_id)
	                .queryParam("code", resValue.get("code"))
	                .queryParam("state", resValue.get("state"))
	                .queryParam("refresh_token", resValue.get("refresh_token")) // Access_token 갱신시 사용
	                .build()
	                .encode()
	                .toUriString();

	        return webClient
	                .get()
	                .uri(uri)
	                .retrieve()
	                .bodyToMono(KakaoVo.class)
	                .block();
	    }
	 
	 
	 // ----- 프로필 API 호출 - kakao (Unique한 id 값을 가져오기 위함) -----
     public  ResponseEntity<Map<String, String>> requestKakaoLoginProfile(KakaoVo kakaoVo, String kakaoApiKey){
    	 // webClient 설정
    	String kakaoApiUrl = "https://kapi.kakao.com";
         WebClient kakaoApiWebClient =
                 WebClient.builder()
                         .baseUrl(kakaoApiUrl)
                         .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                         .build();

        // info api 설정
        @SuppressWarnings("unchecked")
		Map<String, Object> infoResponse =
                 kakaoApiWebClient
                         .post()
                         .uri(uriBuilder -> uriBuilder
                         .path("/v2/user/me")
                         .build())
                         .header("Authorization", "Bearer " + kakaoVo.getAccess_token())
                         .header("KakaoAK", "c46e5a623636ed4c32dd8c374ecf56b0")
                         .retrieve()
                         .bodyToMono(Map.class)
                         .block();

        
         @SuppressWarnings("unchecked")
		 Map<String, Object> kakaoAccountMap = (Map<String, Object>) infoResponse.get("kakao_account");
         logger.info("kakaoAccountMap:", kakaoAccountMap);
         @SuppressWarnings("unchecked")
		 Map<String, String> profileMap = (Map<String, String>) kakaoAccountMap.get("profile");
         Map<String, String> responseMap = new HashMap<>();

         // 닉네임 정보 담기
         if (StringUtils.hasText(profileMap.get("nickname"))) {
             responseMap.put("nickname", profileMap.get("nickname"));
         }
         // 프로필 사진 정보 담기
         if (StringUtils.hasText(profileMap.get("profile_image_url"))) {
             responseMap.put("profileImageUrl", profileMap.get("profile_image_url"));
         }
//         // 이메일 정보 담기
//         if ("true".equals(kakaoAccountMap.get("has_email").toString())) {
//             responseMap.put("email", kakaoAccountMap.get("email").toString());
//         }
//         // 성별 정보 담기
//         if ("true".equals(kakaoAccountMap.get("has_gender").toString())) {
//             responseMap.put("gender", kakaoAccountMap.get("gender").toString());
//         }
//         // 연령대 정보 담기
//         if ("true".equals(kakaoAccountMap.get("has_age_range").toString())) {
//             responseMap.put("ageRange", kakaoAccountMap.get("age_range").toString());
//         }
//         // 생일 정보 담기
//         if ("true".equals(kakaoAccountMap.get("has_birthday").toString())) {
//             responseMap.put("birthday", kakaoAccountMap.get("birthday").toString());
//         }
         
         logger.info("kakao login 최종 responseMap: [}", responseMap.toString());
         // 결과 반환
         return ResponseEntity.ok(responseMap);
     }

}
 