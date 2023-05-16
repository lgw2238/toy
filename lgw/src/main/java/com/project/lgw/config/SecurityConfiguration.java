package com.project.lgw.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.project.lgw.comm.CommonConstant;
import com.project.lgw.handler.CustomAccessDeniedHandler;
import com.project.lgw.handler.CustomAuthenticationEntryPoint;
import com.project.lgw.handler.CustomLoginFailureHandler;
import com.project.lgw.handler.CustomLoginSuccessHandler;
import com.project.lgw.handler.CustomLogoutSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	/************************************************
	 *  OAuth	
	@Autowired
    @Qualifier("oauth2ClientContext")
    private OAuth2ClientContext oAuth2ClientContext;	
	 *************************************************/

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	/**
	 * AuthenticationManager 
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthenticationProvider);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/************************************************
		 *  OAuth 	
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
       	*************************************************/		
		
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter,CsrfFilter.class);		
		
        http
        	.authorizeRequests()
        		.antMatchers("/**").permitAll()
        		.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//        		.antMatchers("/TX_BO", "/google/complete", "/board**", "/sampleErrorTest","/sampleTest").permitAll()
//        		.anyRequest().access("@authorizationChecker.check(request, authentication)")
			.and()
				.headers().frameOptions().disable()
			.and()
				.formLogin()
				.loginPage("/index")
				.loginProcessingUrl("/loginProc")				
				.usernameParameter("userId")
				.passwordParameter("password")
				.successHandler(customAuthenticationSuccessHandler())
				.failureHandler(customAuthenticationFailureHandler())
				.permitAll()
			.and()
				.csrf().disable()
				.sessionManagement()
					.sessionAuthenticationStrategy(concurrentSession())
					.maximumSessions(1)
					.maxSessionsPreventsLogin(false)
					.expiredUrl("/")
					.sessionRegistry(sessionRegistry());
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		http
			.exceptionHandling()
			.accessDeniedHandler(customAccessDeniedHandler())
			.authenticationEntryPoint(customAuthenticationEntryPoint());
			/************************************************
			 *  OAuth
			.and()
				.addFilterBefore(filter, CsrfFilter.class)
				.addFilterBefore(oauth2Filter(), BasicAuthenticationFilter.class);
        	*************************************************/
    	http.logout()
    			.logoutSuccessHandler(customLogoutSuccessHandler())
	        	.invalidateHttpSession(true)
	        	.clearAuthentication(true)
	        	.deleteCookies("JSESSIONID")
	        	.logoutUrl("/logoutProc")
//	        	.logoutSuccessUrl("/")
	        ;
    	
    	http.addFilterBefore(concurrentSessionFilter(), ConcurrentSessionFilter.class);
	}

	//
	@Bean
	public CompositeSessionAuthenticationStrategy concurrentSession() {
		ConcurrentSessionControlAuthenticationStrategy concurrentAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
		concurrentAuthenticationStrategy.setMaximumSessions(1);					
		concurrentAuthenticationStrategy.setExceptionIfMaximumExceeded(false);	
        
		List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<SessionAuthenticationStrategy>();
        delegateStrategies.add(concurrentAuthenticationStrategy);
        
        SessionFixationProtectionStrategy sfps = new SessionFixationProtectionStrategy();
        sfps.setMigrateSessionAttributes(false);
        delegateStrategies.add(sfps);
        delegateStrategies.add(new RegisterSessionAuthenticationStrategy(sessionRegistry()));

        CompositeSessionAuthenticationStrategy authenticationStrategy =  new CompositeSessionAuthenticationStrategy(delegateStrategies);
        return authenticationStrategy;
	}
	
	@Bean
	public ConcurrentSessionFilter concurrentSessionFilter() {
        CustomSessionInformationExpiredStrategy redirectStrategy = new CustomSessionInformationExpiredStrategy(CommonConstant.DUPLICATION_LOGIN_ERROR_PATH);
        CustomConcurrentSessionFilter concurrentSessionFilter = new CustomConcurrentSessionFilter(sessionRegistry(), redirectStrategy);
        return concurrentSessionFilter;
	}	
	
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }	
	
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new CustomHttpSessionListener());
    }    
    
	@Bean
	public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new CustomLoginSuccessHandler("/");
	}
	
    @Bean
	public AuthenticationFailureHandler customAuthenticationFailureHandler() {
		return new CustomLoginFailureHandler("/loginPage?error");
	}

	
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

    @Bean
	public AuthenticationEntryPoint customAuthenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}	

    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}    
//    
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	}
//	    
}
