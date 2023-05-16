package com.project.lgw.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.project.lgw.user.vo.UserVo;
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		UserVo userVo = (UserVo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		request.setAttribute("errorCode", HttpStatus.UNAUTHORIZED);
		request.setAttribute("errorMessage", com.project.lgw.comm.CommonConstant.LOGIN_ACCESSDENY_EXCEPTION);
		request.setAttribute("adminVo", userVo);
		
		/* 2020.01.06 hjs 예외처리 시 테이블 등록(미사용으로 인한 주석처리)
		ExceptionLogEntity exceptionLogEntity = new ExceptionLogEntity();
		exceptionLogEntity.setLogName("99999");
		exceptionLogEntity.setLogMessage(Keywords.LOGIN_ACCESSDENY_EXCEPTION);
		exceptionLogEntity.setUserId(adminVo.getUserId());			
		SqlSession.insert("exceptionLog.insertExceptionLog", exceptionLogEntity);
		*/
		
		request.getRequestDispatcher("/WEB-INF/views/common/error/errorPage.jsp").forward(request, response);
		
	}


}
