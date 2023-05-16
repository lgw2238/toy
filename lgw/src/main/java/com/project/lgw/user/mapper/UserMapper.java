package com.project.lgw.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.project.lgw.user.vo.UserProfile;
import com.project.lgw.user.vo.UserVo;

@Mapper
public interface UserMapper {
	
	/**
	 * @description 유저 아이디 체크
	 * @date 2023.05.11 
	 * @author lgw
	 * @return
	 * @throws Exception
	 */
	public int idChk(UserVo userVo) throws Exception;
	
	/**
	 * @description Oauth 유저 아이디 체크
	 * @date 2023.05.15 
	 * @author lgw
	 * @return
	 * @throws Exception
	 */
	public int findOauthUserInfo(UserProfile userProfile) throws Exception;
	
}
