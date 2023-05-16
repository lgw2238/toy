package com.project.lgw.config;

import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.project.lgw.comm.CommonConstant;
import com.project.lgw.user.vo.UserVo;


@Component
public class SessionCheckInterceptor extends HandlerInterceptorAdapter{

	public Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");		
		
		String requestURI = request.getRequestURI();	//요청 URL 
		
		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()){
		    String name = (String)params.nextElement();
//		    System.out.println(name + " : " +request.getParameter(name));
		}
		
		HttpSession session = request.getSession();
		UserVo UserVo = (UserVo)session.getAttribute(CommonConstant.KEY_SESSIONUSER);
		
		if( requestURI.equals(request.getContextPath())
				|| requestURI.equals(request.getContextPath()+"/")
				|| requestURI.equals(request.getContextPath()+CommonConstant.ERROR_PATH)
				|| requestURI.equals(request.getContextPath()+CommonConstant.DUPLICATION_LOGIN_ERROR_PATH)
				|| requestURI.equals(request.getContextPath()+CommonConstant.SESSION_EXPIRED)
				|| requestURI.equals(request.getContextPath()+CommonConstant.RESERVATION_MMS_PATH)
				)  {
			if(UserVo != null) {
				if( requestURI.equals(request.getContextPath())
				|| requestURI.equals(request.getContextPath()+"/") ) {
					response.sendRedirect(request.getContextPath() + "/main");
					return false;		
				}else {
					return true;
				}
			}else {
				return true;
			}
		}else {
			
			String authGroupId = getAuthGroupId();

			//권한이 없거나 session 에 UserVo 객체 없을 경우 에러페이지 리턴
			if( authGroupId.equals("") ) {
				response.sendRedirect(request.getContextPath() + CommonConstant.ERROR_PATH);
				return false;
			}else if( UserVo == null ) {
				response.sendRedirect(request.getContextPath() + CommonConstant.SESSION_EXPIRED);
				return false;				
			}
			else {
		         
				//세션이 있으면서 메인페이지 이동시는 true
				if(requestURI.equals(request.getContextPath() + "/main") || requestURI.startsWith(request.getContextPath() + "/noti") || requestURI.endsWith("jsp")) {
		                 return true;
		              }
				else {
					//MenuVo menuVo = new MenuVo();
					//menuVo.setAuthGroupId(authGroupId);
					
					String level3MenuId = request.getParameter("level3MenuId");
//					System.out.println("level3MenuId =====================>>>> "+level3MenuId);
					if(level3MenuId == null || level3MenuId.isEmpty()) {
						response.sendRedirect(request.getContextPath() + CommonConstant.ERROR_PATH);
						return false;
					}
					
					/*
					//권한에 포함된 URL, 조회, 제어 리스트 조회
					List<MenuVo> authMenuMappingList = authService.selectAuthMenuMappingList(menuVo, false);
					List<MenuVo> ulList = authService.selectMenuListOfControl(menuVo);
					List<MenuVo> lastList = new ArrayList<MenuVo>();
					boolean checkAuth = false;
					
					if(authMenuMappingList != null && !authMenuMappingList.isEmpty()) {
						lastList.addAll(authMenuMappingList);
					}
					
					if(ulList != null && !ulList.isEmpty()) {
						lastList.addAll(ulList);
					}
					
					if(lastList != null && !lastList.isEmpty()) {
						for(MenuVo checkVo : lastList) {
							System.out.println("requestURI : " + requestURI);
							System.out.println("db uri     : " + request.getContextPath() + checkVo.getFullLinkUrl());
							if( requestURI.equals( request.getContextPath() + checkVo.getFullLinkUrl() ) ) {
								checkAuth = true;
								
								break;
							}
						}
					}
					
					if(!checkAuth) {
						response.sendRedirect(request.getContextPath() + CommonConstant.ERROR_PATH);
						return false;
					}
					*/
					
					return true;
				}
			}
		}
	}

	private String getAuthGroupId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String authGroupId = "";
		for (GrantedAuthority authority : authorities) {
			authGroupId = authority.getAuthority();
		}
		return authGroupId;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}
	
}
