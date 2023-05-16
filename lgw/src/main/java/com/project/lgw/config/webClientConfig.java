package com.project.lgw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class webClientConfig {
	
		@Bean
	    public WebClient webClient(){
	        // REST 통신을 위한 API 등록
			 return WebClient.create();
	    }
}
