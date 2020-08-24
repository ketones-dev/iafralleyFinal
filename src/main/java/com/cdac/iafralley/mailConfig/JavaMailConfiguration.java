package com.cdac.iafralley.mailConfig;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class JavaMailConfiguration {
	
	@Bean("personalMailServer")
	public JavaMailSenderImpl getAlertnatePersonalMailServer() {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    
	    mailSender.setUsername("atktprvp@gmail.com");
	    mailSender.setPassword("rakhdekuchbhi");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");//ttls
	    props.put("mail.debug", "true");
	    
	    return mailSender;
		
		
	}
	
	
	
	
	@Bean("alertnateMailServer")
	public JavaMailSenderImpl getAlertnateMailServer() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		//sender.setHost("smtp.pune.cdac.in");
		sender.setHost("smtp.cdac.in");
		sender.setPort(587);
		//sender.setHost("smtpb.pune.cdac.in");
		//sender.setPort(25);
		sender.setUsername("afcat@cdac.in");
		sender.setPassword("afcat123@@");

		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");//for smtp it must be true
		//props.put("mail.smtp.starttls.enable", "false");//for smtpb it must be false
		props.put("mail.debug", "false");
		props.put("mail.smtp.socketFactory.port", "587");
		
		sender.setJavaMailProperties(props);

		return sender;
	}
	
	@Bean("cdac")
	public JavaMailSenderImpl getJavaMailSender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		//sender.setHost("smtp.pune.cdac.in");
		
		//sender.setHost("smtp.cdac.in");
		//sender.setPort(587);
		
		//sender.setHost("10.10.34.7");//for smtpdr server
		//sender.setPort(587);
		
		sender.setHost("smtpb.pune.cdac.in");
		sender.setPort(25);
		
		
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "false");
		
		//props.put("mail.smtp.starttls.enable", "true");//for smtp it must be true
		
		props.put("mail.smtp.starttls.enable", "false");//for smtpb it must be false
		
		props.put("mail.debug", "false");
		props.put("mail.smtp.socketFactory.port", "25");
		//props.put("mail.smtp.socketFactory.port", "587");
		
		sender.setJavaMailProperties(props);
		return sender;
	}
}



