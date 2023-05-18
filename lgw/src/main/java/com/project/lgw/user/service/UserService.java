package com.project.lgw.user.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.project.lgw.user.vo.KakaoVo;
import com.project.lgw.user.vo.NaverVo;
import com.project.lgw.user.vo.SnsApiResponse.KakaoAccount;
import com.project.lgw.user.vo.UserProfile;
import com.project.lgw.user.vo.UserVo;

public interface UserService {
	
	
	/**
	  * @description 유저 아이디 중복체크 (일반유저)
	  * @date 2023. 5. 11.
	  * @author lgw
	  * @param userVo
	  * @return
	  * @throws Exception
	  */
	public int idChk(UserVo userVo) throws Exception;

	public int signUp(UserVo userVo) throws Exception;
	
	public UserVo selectUserInfo(UserVo userVo) throws Exception;
	
	public void updateUserConnectionYn(UserVo userVo) throws Exception;
	/* oauth 2.0 naver */
	public NaverVo requestNaverLoginAcceccToken(Map<String, String> resValue, String grant_type);
	
	public UserProfile requestNaverLoginProfile(NaverVo naverLoginVo);
	
	/* oauth 2.0 kakao - class type */
	public KakaoVo requestKakaoLoginAcceccToken(Map<String, String> resValue, String grant_type, String kakao_client_id);
	
	public ResponseEntity<Map<String, String>> requestKakaoLoginProfile(KakaoVo kakaoVo, String kakaoApiKey);
	
	/* oauth 2.0 kakao - restemplate type */
	public String requestKaKaoLoginAcceccToken(String code, String kakaoAuthUrl, String kakaoApiKey, String redirectURI);
	
	public String getKakaoUniqueNo(String accessToken, String kakaoApiKey) throws Exception;
	
	public int validationOauthJoin(UserProfile userProfile) throws Exception;
}
