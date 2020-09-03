package com.cdac.iafralley;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.cdac.iafralley.captcha.CaptchaGenerator;


@SpringBootApplication
public class IafralleyApplication  extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(IafralleyApplication.class, args);
		
		
	}
	
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(IafralleyApplication.class);
    }
	
	@Bean
	public CaptchaGenerator getCaptchaGenerator() {
		return new CaptchaGenerator();
	}

}
