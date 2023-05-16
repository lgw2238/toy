package com.project.lgw.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.project.lgw.user.service.UserService;
import com.project.lgw.user.vo.UserVo;


public class CustomHttpSessionListener implements HttpSessionListener {
	
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// session
		HttpSession session = se.getSession();
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(se.getSession().getServletContext());
		SecurityContext securityContext = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		if (securityContext != null) {
			Authentication auth = securityContext.getAuthentication();
			UserVo UserVo = (UserVo)auth.getPrincipal();
			if(UserVo != null) {
				try {
					UserVo.setConnectionYn("N");
					UserService UserService = context.getBean(UserService.class);
					//UserService.updateAdminConnectionYn(UserVo);
				} catch (Exception e) {
				}
			}
		}		
		
		HttpSessionListener.super.sessionDestroyed(se);
	}
}
