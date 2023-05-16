package  com.project.lgw.config;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.javassist.compiler.ast.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.project.lgw.exception.DateExpireException;
import com.project.lgw.exception.DuplicationException;
import com.project.lgw.user.vo.UserVo;
import com.project.lgw.user.service.UserService;
import com.project.lgw.util.SHA256Util;


@Configuration
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private CustomUserDetailService customUserDetailService;

//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	
	@Autowired
	private UserService UserService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		SHA256Util sha256 = new SHA256Util();
		
		String UserId = authentication.getName();
		String UserPw = (String) authentication.getCredentials();
		String sha256Pw = null;
				try {
					sha256Pw = sha256.encrypt(UserPw);
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}
		UserVo UserVo = (UserVo) customUserDetailService.loadUserByUsername(UserId);

		if (UserVo == null || UserVo.getUserId() == null || UserVo.getUserStatusCd() == null) {
			throw new UsernameNotFoundException(UserId);
		}

		//운영자 계정의 상태가 사용 이 아닌 경우
		if( !UserVo.getUserStatusCd().equals("U") ) {
			throw new DisabledException(UserId);
		}
		
		//30일 이상 미접속 시 휴면
		// null->계정생성 후 미접속, N->30일 이상 미접속, Y->접속이력이 있을경우
		if(UserVo.getLastLoginDateYn() != null && UserVo.getLastLoginDateYn().equals("N")) {
			throw new AccountExpiredException(UserId);
		}
		
		//로그인 시도 횟수 만료 시 예외처리
		if(UserVo.getTryCnt() >= 5 || UserVo.getUserStatusCd().equals("Z")) {
			throw new LockedException(UserId);
		}
		

		/* password 비교 로직 
		 * #1. sha256 비교
		 * 	   true -> pass 
		 *     false -> bcrypt
		 * #2. bcrypt 비교 
		 *     true -> 상태값 제어, 비밀번호 변경유도
		 *     false -> 로그인 실패.
		 *  */
//		if(sha256Pw.equals(UserVo.getUserPw())){
//			// adinPw = sha변환값으로 세팅
//			UserPw = sha256Pw;
//		} else {
//				// sha256 : 비밀번호 불일치				
//			try {
//				if( !passwordEncoder.matches(UserPw, UserVo.getUserPw())) {			
//			        throw new BadCredentialsException(UserId);
//			    }else {
//			    //bcrypt : 비밀번호 일치	
//			    	UserVo.setUserPassWordStatus("Y");	        				    	
//			    }
//			} catch (IllegalArgumentException e) { 	
////				e.printStackTrace();
//				//bcrypt : 비밀번호 불일치 exception 처리 (암호화방식 다른 케이스 비교하는 경우 error 대응)
//				throw new BadCredentialsException(UserId);
//			}  catch (Exception e) {
//				e.printStackTrace();
//				throw e;
//			}
//			
//			
//		}		
//	
		String expireDate = UserVo.getPwExpireDate();
		if(expireDate != null) {
			expireDate = expireDate.replaceAll("-", "");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String today = dateFormat.format(new Date());
			if(Integer.compare(Integer.parseInt(expireDate),Integer.parseInt(today)) == -1) {
				throw new DateExpireException(UserId);
			}
		}
		
		

		//현재 로그인 상태를 확인
		if(UserVo.getConnectionYn() != null && UserVo.getConnectionYn().equals("Y")) {
			//기존 관리자가 로그인한 상태에서 다른 사용자가 중복로그인을 시도 할 경우 duplicationLoginYn 값 은 'Y'
			//Y가 아닐 경우 -> 최초 로그인 시도 시
			if( request.getParameter("duplicationLoginYn") == null || !request.getParameter("duplicationLoginYn").equals("Y") ) {
				throw new DuplicationException(UserId);
			}else {
				//중복로그인을 수행 할 경우 중복로그인 시도여부 파라미터 제거
				request.removeAttribute("duplicationLoginYn");				
			}
		}
		
		//관리자 계정의 로그인상태(CONNECTION_YN -> 'Y' 로 변경)
		UserVo vo = new UserVo();
		vo.setUserId(UserId);
//		vo.setRegUserId(UserId);
//		vo.setUpdUserId(UserId);
		vo.setConnectionYn("Y");
		try {
			//UserService.updateUserConnectionYn(vo);
		} catch (Exception e) {
			
		}
		
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) UserVo.getAuthorities();

		return new UsernamePasswordAuthenticationToken(UserVo, UserPw, authorities);

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
