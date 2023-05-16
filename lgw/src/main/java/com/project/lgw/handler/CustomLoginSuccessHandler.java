package com.project.lgw.handler;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.project.lgw.comm.CommonConstant;
import com.project.lgw.user.service.UserService;
import com.project.lgw.user.vo.UserVo;


public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private RequestCache requestCache = new HttpSessionRequestCache();
	private RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();
	
	@Autowired
	private UserService userService;

	
	public CustomLoginSuccessHandler(String defaultSuccessUrl) {
		setDefaultTargetUrl(defaultSuccessUrl);
	}

	/**
	 * @date  2023.05.10
	 * 로그인 성공 시 수행 메서드
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		
		// 에러 세션 지우기
		clearAuthenticationAttributes(request, session);

		//세션 세팅하기
		session = settingSessionAttributes(request);
		
		//redirect url 설정
		resultRedirectStrategy(request, response, authentication);		
		
		String userAgentInfo = request.getHeader("User-Agent");
		request.setAttribute("User-Agent", userAgentInfo);
		
		String UserId = authentication.getName();
		UserVo UserVo = new UserVo();
		UserVo.setUserId(UserId);
//		UserVo.setUpduserId(UserId);
//		UserVo.setReguserId(UserId);
		String passwordStatus = null;
		try {
			//로그인 전 가장 최근접속이력 조회
			UserVo loginHistory = new UserVo();
			loginHistory.setUserId(UserId);
//			loginHistory.setRegUserId(UserId);
//			loginHistory.setUpdUserId(UserId);
//			loginHistory = userService.selectLoginHistoryInfo(loginHistory);
			session.setAttribute("loginHistory", loginHistory);
			UserVo passwordInfo = new UserVo();
//			passwordInfo.setUserId(UserId);
//			passwordInfo.setRegUserId(UserId);
//			passwordInfo.setUpdUserId(UserId);			
//			passwordInfo = userService.selectUserInfo(passwordInfo);
			session.setAttribute("passwordInfo", passwordInfo);
							
			// 로그인 시 SHA256 체크하여 비밀번호 변경 권유 status 설정 
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if ( principal instanceof UserVo ) {
				UserVo statusInfo  = (UserVo)principal;
				if(statusInfo != null) {
					passwordStatus = statusInfo.getUserPassWordStatus();
				}
			}
		
			session.setAttribute("passwordStatus", passwordStatus);
			
			//최근접속이력 등록
			//userService.insertLoginHistory(UserVo, request);
			
			//로그인 카운트 초기화
			//userService.setLoginTryCntInit(UserVo);

		} catch (Exception e) {
			e.printStackTrace();
		}	
		

	}

	/**
	  * @description SecurityContextHolder 를 이용하여 세션 생성
	  * @date 2023.05.10
	  * @author 임건우
	  * @param request
	  * @param session void
	  */
	protected HttpSession settingSessionAttributes(HttpServletRequest request) {
		UserVo UserVo = null;
		HttpSession session = request.getSession(false);
		
//		ConfVo confVo = new ConfVo();
//		confVo.setSearchConfigId("SESSION_TIME_OUT_FOR_User");
//		try {
////			confVo = confService.selectConfInfo(confVo);
////			
//		} catch (Exception e2) {
//			// TODO: handle exception
//		}
//		
//		int sessionTime = 120;
//		if( confVo.getConfigValue() != null ) {
//			sessionTime = Integer.parseInt(confVo.getConfigValue()) * 60;
//		}else {
//			sessionTime = sessionTime*60;
//		}		

		if ( SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null ) {
			request.getSession(false).setMaxInactiveInterval(120); //15분*60초
			
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if ( principal instanceof UserVo )
				UserVo = (UserVo)principal;
			
			Collection<? extends GrantedAuthority> authorities = UserVo.getAuthorities();
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(UserVo, UserVo.getUserPw(), authorities);
			
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(authToken);
			
			if(session!=null) {
				session.setAttribute("SPRING_SECURITY_CONTEXT", context);
				session.setAttribute(CommonConstant.KEY_SESSIONUSER, SecurityContextHolder.getContext().getAuthentication().getPrincipal());				
			}
		}
		return session;
	}	
	
	/**
	  * @description 에러세션 제거
	  * @date 2023.05.11
	  * @author lgw
	  * @param request
	  * @param session void
	  */
	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpSession session) {
		session = request.getSession(false);
		if (session == null)
			return;
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}	
	
	protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest != null) {
			useSessionUrl(request, response);
		} else {
			useDefaultUrl(request, response);
		}
	}
	
	protected void useSessionUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		String targetUrl = savedRequest.getRedirectUrl();
		redirectStratgy.sendRedirect(request, response, targetUrl);
	}	

	protected void useDefaultUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		redirectStratgy.sendRedirect(request, response, this.getDefaultTargetUrl());
	}
		
}
