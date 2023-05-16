package com.project.lgw.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.project.lgw.comm.CommonConstant;
import com.project.lgw.exception.DateExpireException;
import com.project.lgw.exception.DuplicationException;
import com.project.lgw.user.mapper.UserMapper;
import com.project.lgw.user.service.UserService;
import com.project.lgw.user.vo.UserVo;


public class CustomLoginFailureHandler implements AuthenticationFailureHandler{

	@Autowired
	private UserService UserService;
	@Autowired
	private UserMapper UserMapper;
	
	private String defaultFailureUrl;
	private String loginErrorMessage;
	
	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}

	public String getLoginErrorMessage() {
		return loginErrorMessage;
	}

	public void setLoginErrorMessage(String loginErrorMessage) {
		this.loginErrorMessage = loginErrorMessage;
	}

	public CustomLoginFailureHandler(String defaultFailureUrl) {
		setDefaultFailureUrl(defaultFailureUrl);
	}
		
	/**
	 * 로그인 실패 후 메세지 설정 및 url 리턴
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failException) throws IOException, ServletException {
		String UserId =  request.getParameter("userId");
		try {
			request.setAttribute("loginErrorMessage", getLoginErrMsg(failException, UserId, request));
		} catch (Exception e) {
		}
		request.getRequestDispatcher("/").forward(request, response);
	}

	private String getLoginErrMsg(AuthenticationException failException, String UserId, HttpServletRequest request)throws Exception {
		String errMessage = CommonConstant.LOGIN_FAIL_MESSAGE;
		UserVo UserVo = new UserVo();
		UserVo.setUserId(UserId);
	
		if (failException instanceof UsernameNotFoundException ) {
			errMessage = "";
			errMessage += CommonConstant.INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION;
			return errMessage;
		}		
		
		//계정 상태가 사용중지, 휴면, 삭제 인 경우
		if (failException instanceof DisabledException ) {
			//UserVo = UserService.selectUserInfo(UserVo);
			errMessage = "";
//			errMessage += CommonConstant.User_NOT_LOGIN_STATUS_PREFIX;
//			errMessage += UserVo.getUserStatusCdNm();
//			errMessage += CommonConstant.User_NOT_LOGIN_STATUS_SUFFIX;
//			errMessage += CommonConstant.CONTACT_SYSTEM_User;
			return errMessage;
		}
		
		//30일동안 미접속으로 인한 휴면 상태
		if (failException instanceof AccountExpiredException) {
			errMessage = "";
			errMessage += CommonConstant.ADMIN_LOGIN_DORMANCY_MESSAGE_PREFIX; 
			errMessage += CommonConstant.CONTACT_SYSTEM_ADMIN;
			UserVo.setUserStatusCd("D");
			//UserMapper.updateUserForLogin(UserVo);	//�쑕硫댁쿂由�
			return errMessage;
		} 
		
		//비밀번호 5회 입력 실패로 계정 잠금 메세지
		if (failException instanceof LockedException) {
			errMessage = CommonConstant.LOGIN_TRY_COUNT_EXCEEDED;
			UserVo.setUserStatusCd("Z"); //�궗�슜 以묒� 泥섎━
			//UserMapper.updateUserForLogin(UserVo);
		} 		
		
		//비밀번호 불일치, 불일치 경우 카운트 증가 (없으면 insert 있으면 update)
		if (failException instanceof BadCredentialsException) {
			//UserService.updateLoginTryCnt(UserVo);
			//UserVo = UserService.selectUserInfo(UserVo);
			int loginTryCnt = UserVo.getTryCnt();
			if( loginTryCnt >= 5 ) {
				UserVo.setUserStatusCd("Z");//사용중지(로그인에러)
				//UserMapper.updateUserForLogin(UserVo);				
				errMessage = CommonConstant.LOGIN_TRY_COUNT_EXCEEDED; 
			}else {
				errMessage = "";
				errMessage += CommonConstant.BAD_CREDENTIALS_EXCEPTION_MESSAGE_PRFIX; 
				errMessage += UserVo.getTryCnt(); 
				errMessage += CommonConstant.BAD_CREDENTIALS_EXCEPTION_MESSAGE_SUFFIX;
				errMessage += CommonConstant.CHECK_INFO_MESSAGE;
			}
			return errMessage;
		}
		
		if (failException instanceof DuplicationException ) {
			errMessage = CommonConstant.DUPLICATIONEXCEPTION_MESSAGE;
			request.setAttribute("loginErrorType", "DuplicationException");
			request.setAttribute("returnUserId", UserId);
			request.setAttribute("returnUserPw", request.getParameter("password"));
			return errMessage;
		} 				
		
		if (failException instanceof DateExpireException ) {
			errMessage = CommonConstant.LOGIN_EXPIRE_DATE_MESSAGE;
			return errMessage;
		}
		
		return errMessage;	
	}

}
