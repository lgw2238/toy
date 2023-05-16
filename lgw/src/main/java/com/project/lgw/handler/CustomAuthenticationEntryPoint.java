package com.project.lgw.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		request.setAttribute("errorCode", HttpStatus.UNAUTHORIZED);
		request.setAttribute("errorMessage", com.project.lgw.comm.CommonConstant.LOGIN_ACCESSDENY_EXCEPTION);
	
		request.getRequestDispatcher("/WEB-INF/views/common/error/errorPage.jsp").forward(request, response);

	}

}
