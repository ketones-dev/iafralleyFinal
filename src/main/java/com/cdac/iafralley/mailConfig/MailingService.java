package com.cdac.iafralley.mailConfig;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.cdac.iafralley.controllers.RalleyRegistrationFormController;
import com.cdac.iafralley.entity.RalleyCandidateDetails;


public class MailingService {
	
	
	
	
	

private static final String ALTERNATE_MAIL_SERVER1 = "alertnateMailServer";
private static final String ALTERNATE_MAIL_SERVER2= "personalMailServer";

private static final Logger logger = LoggerFactory.getLogger(MailingService.class);

	
	public static void sendMail(final String mailServer, final String from, final String password,RalleyCandidateDetails candidate, final String subject, String message,String FILE_PATH) {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(JavaMailConfiguration.class);
		
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		try {
			// Provided mail server.
			sender = (JavaMailSenderImpl) context.getBean(mailServer);
			logger.info("Data saved for candidate+"+candidate.getEmailid()+"prepraring for sending mail using server:"+sender.getHost().toString());
			sender.setUsername(from);
			sender.setPassword(password);
		} catch (BeansException e) {
			e.printStackTrace();
			try {
			// Alternative mail server.
			sender = (JavaMailSenderImpl) context.getBean(ALTERNATE_MAIL_SERVER1);
			}
			catch(BeansException ex)
			{
				ex.printStackTrace();
				sender = (JavaMailSenderImpl) context.getBean(ALTERNATE_MAIL_SERVER2);
			}
		}
		
		logger.info("Setting message in mimeMessage for candidate:"+candidate.getEmailid());
		
		MimeMessage mimeMessage = sender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setTo(candidate.getEmailid());
			//mimeMessageHelper.setText(message, message);for plane and html text
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(message);
			//3) create MimeBodyPart object and set your message text        
//            BodyPart messageBodyPart1 = new MimeBodyPart();     
//            messageBodyPart1.setText(message);          

            //4) create new MimeBodyPart object and set DataHandler object to this object        
//            MimeBodyPart messageBodyPart2 = new MimeBodyPart();      
//            String filepath = FILE_PATH+candidate.getRalleyregistrationNo()+".pdf";//change accordingly     
//            DataSource source = new FileDataSource(filepath);    
//            messageBodyPart2.setDataHandler(new DataHandler(source));    
//            messageBodyPart2.setFileName(candidate.getRalleyregistrationNo()+".pdf");             

            //5) create Multipart object and add MimeBodyPart objects to this object        
//            Multipart multipart = new MimeMultipart();    
//            multipart.addBodyPart(messageBodyPart1);     
//            multipart.addBodyPart(messageBodyPart2);      

            //6) set the multiplart object to the message object    
//            mimeMessage.setContent(multipart );        
			
			
			
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		try {
			logger.info("sending mail via server with host:"+sender.getHost().toString());
			sender.send(mimeMessage);
		} catch (MailException e) {
			
			logger.info("error in sending mail via server with host:"+sender.getHost().toString()+"\n pepare alternate server to send mail");
			e.printStackTrace();
			try {
				// Alternative mail server.
				sender = (JavaMailSenderImpl) context.getBean(ALTERNATE_MAIL_SERVER1);
				sender.send(mimeMessage);
				}
				catch(MailException ex)
				{
					logger.info("error in sending mail via server with host:"+sender.getHost().toString()+"\n pepare alternate server to send mail");
					ex.printStackTrace();
					sender = (JavaMailSenderImpl) context.getBean(ALTERNATE_MAIL_SERVER2);
					sender.send(mimeMessage);
				}
			
		}
	}
	
}
