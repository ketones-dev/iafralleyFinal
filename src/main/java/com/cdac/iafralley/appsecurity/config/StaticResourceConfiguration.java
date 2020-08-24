package com.cdac.iafralley.appsecurity.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cdac.iafralley.services.StoreImageFiles;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(StaticResourceConfiguration.class);
	
	@Value("${applicant.certificate}")
	private  String FILE_PATH;
	

 @Override
 public void addResourceHandlers(ResourceHandlerRegistry registry) {

    if(FILE_PATH != null) {
        logger.info("Serving static content from " + FILE_PATH);
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + FILE_PATH);
    }
 }

 // see https://stackoverflow.com/questions/27381781/java-spring-boot-how-to-map-my-my-app-root-to-index-html
 @Override
 public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/showRegistrationForm");
 }
}
