package com.project.lgw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.lgw.user.service.UserService;
import com.project.lgw.user.vo.UserVo;


@Service
public class CustomUserDetailService implements UserDetailsService{
	
	@SuppressWarnings("unused")
	@Autowired
	private UserService userSerivce;
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserVo userVo = new UserVo();
		try {
			userVo.setUserId(userId);
			userVo.setLoginProcessYn("Y");
			//userVo = userService.selectuserInfo(userVo);
			if(userVo == null) {
				throw new UsernameNotFoundException(userId);
			}						
		} catch (Exception e) {
			// TODO: handle exception
		}

		return userVo;
	}

}
