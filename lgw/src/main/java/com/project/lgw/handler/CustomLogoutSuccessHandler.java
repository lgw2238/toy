package com.project.lgw.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.project.lgw.user.service.UserService;
import com.project.lgw.user.vo.UserVo;


public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	@Autowired
	private UserService userService;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
        if (authentication != null) {
//        	UserVo adminVo = (UserVo) authentication.getPrincipal();
//        	adminVo.setRegUserId(adminVo.getAdminId());
//        	adminVo.setUpdUserId(adminVo.getAdminId());
//        	adminVo.setConnectionYn("N");
//        	try {
//        		userService.updateAdminConnectionYn(adminVo);
//			} catch (Exception e) {
//				
//			}
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(request.getContextPath()+"/");        
	}

}
