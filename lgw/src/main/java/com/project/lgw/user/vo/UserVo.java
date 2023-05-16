package com.project.lgw.user.vo;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data @Getter @Setter
public class UserVo extends AuthVo implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private String userId;
	private String userNm;
	private String userPw;
	private String pwExpireDate;
	private String email;
	private String phoneNo1;
	private String phoneNo2;
	private String phoneNo3;
	private String phoneNoFullNumber;	//phoneNo1+phoneNo2+phoneNo3
	private String conIp;
	private String deviceNmVer;
	private String loginDt;
	private String userStatusCd;
	private String userStatusCdNm;
	private String lastPwChangeDate;
	private String connectionYn;
	private String passwordChangeAlarm;
	private String userPassWordStatus;
	
	
	
	//관리자계정 휴면주기
	private String dormantInterval;
	
	//관리자계정 비밀번호 유지기간
	private String pwExpirationInterval;

	//관리자계정 비밀번호 변경 주기
	private String passwordChangeInterval;

	/** 마이페이지 **/
	private String olduserPassword;
	private String newuserPassword;
	private String pwChangeSeq;
	
	private int tryCnt;
	private String lastLoginDate;

	private String lastLoginDateYn;
	
	/** 검색 조건 **/
	private String oldSearchRegDtFrom;
	private String oldSearchRegDtTo;
	private String oldSearchAuthGroupId;
	private String oldSearchuserStatusCd;
	private String oldSearchType;
	private String oldSearchTypeValue;

	private String excelDownloadYn;
	
	private String loginProcessYn;
	
	/* 어드민 사용 이력  데이터 */
	private String actionCode;
	private String deviceInfo;
	private String actionValue;
	private String userUseDt;
	private String searchDt;
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		authorities.add(new SimpleGrantedAuthority( getAuthGroupId() ));

		return authorities;
	}


	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
