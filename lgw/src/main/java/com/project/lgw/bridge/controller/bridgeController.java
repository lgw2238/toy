package com.project.lgw.bridge.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.project.lgw.user.vo.UserVo;

@Controller
public class bridgeController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "/")
    public String login(HttpServletRequest request, Model model) {
		logger.debug("==============================================================");
		logger.info("session id : {} ", request.getSession().getId());
		logger.debug("==============================================================");
		System.out.println("login controller start");

        LocalDate now = LocalDate.now();
 
        // 현재 날짜 구하기(지역:한국-서울 기준)
        LocalDate seoulNow = LocalDate.now(ZoneId.of("Asia/Seoul"));
        int year = seoulNow.getYear();
        String month = seoulNow.getMonth().toString();
        int dayOfMonth = seoulNow.getDayOfMonth();
       
       
        
        String dateString = month+" "+Integer.toString(dayOfMonth)+","+Integer.toString(year);

        logger.info("dateString:", dateString);
		model.addAttribute("date", dateString);
        return "index";
        
    }
	
	@RequestMapping(value = "/signUp")
    public String goSignUp(HttpServletRequest request, Model model) {
		logger.debug("==============================================================");
		logger.info("session id : {} ", request.getSession().getId());
		logger.debug("==============================================================");
		System.out.println("signUp start");
		LocalDate now = LocalDate.now();
	  
        // 현재 날짜 구하기(지역:한국-서울 기준)
        LocalDate seoulNow = LocalDate.now(ZoneId.of("Asia/Seoul"));
        int year = seoulNow.getYear();
        String month = seoulNow.getMonth().toString();
        int dayOfMonth = seoulNow.getDayOfMonth();
       	       
        
        String dateString = month+" "+Integer.toString(dayOfMonth)+","+Integer.toString(year);

        logger.info("dateString:", dateString);
		model.addAttribute("date", dateString);
        return "signUp";
        
    }
	
	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request, ModelAndView mav, HttpSession session)throws Exception{
		if( session.getAttribute("loginHistory") != null ) {
			mav.addObject("loginHistory", (UserVo)session.getAttribute("loginHistory") );
			session.removeAttribute("loginHistory");
		}
		
		if( session.getAttribute("passwordInfo") != null ) {
			mav.addObject("passwordInfo", (UserVo)session.getAttribute("passwordInfo") );
			session.removeAttribute("passwordInfo");
		}
		
		//mav.addObject("xboxServerStatus", XmlUtil.xmlParsingForXboxServer());
		mav.setViewName("main");
		
		return mav;
	};
}