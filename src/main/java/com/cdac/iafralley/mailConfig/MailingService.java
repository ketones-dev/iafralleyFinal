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
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.cdac.iafralley.controllers.RalleyRegistrationFormController;
import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.entity.RalleyDetails;


public class MailingService {
	
	
	
	
	

private static final String ALTERNATE_MAIL_SERVER1 = "alertnateMailServer";
private static final String ALTERNATE_MAIL_SERVER2= "personalMailServer";

private static final Logger logger = LoggerFactory.getLogger(MailingService.class);

	
	public static void sendMail(final String mailServer, final String from, final String password,RalleyCandidateDetails candidate, final String subject, String message,String FILE_PATH,RalleyDetails saverd) {
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
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setTo(candidate.getEmailid());
			//mimeMessageHelper.setText(message, message);for plane and html text
			mimeMessageHelper.setSubject(subject);
			String contentId = ContentIdGenerator.getContentId();
			 message=""+
				       "<html>\n" + 
				       "<body>\n" + 
				       "<div style=\"margin: 0 auto;width: 70%;border: 1px solid;padding: 5px 18px;\">\n" + 
				       "<p><h3>Dear Candidate,</h3>\n" + 
				       "<h4>1. You have successfully registered for "+saverd.getRalley_details()+"  at "+saverd.getCity_name().toUpperCase()+" and your registration number is "+ candidate.getRalleyregistrationNo() +"</h4>\n" + 
				       "<h4>2. Provisional Admit Card would be mailed to the shortlisted candidates on their registered e-mail ID based on merit (Aggregate percentage obtained in Intermediate/10+2/Equivalent Exam/Diploma Courses as applicable).</h4>\n" + 
				       "<h4>3. Only those candidates who would be issued with Provisional Admit Card will be allowed to appear in the said Recruitment Rally.</h4> \n" + 
				       "<div>\n" + 
				       "<div>\n" + 
				       "<h4 style=\"margin:0 auto;1\">Regards,</h4>\n" + 
				       "<h4 style=\"margin:0 auto;1\">CASB , IAF</h4>\n" + 
				       " </div>\n" + 
				       " </div>\n" + 
				       "  </p>\n" + 
				      
				       " </div>\n" + 
				       "</body>\n" + 
				       "</html>";
		// "<div><img src=\"cid:"+ contentId +"\" width=\"100%\" height=\"400\"></div>\n" + 	 
			mimeMessageHelper.setText(message,true);
			// ClassPathResource classPathResource = new ClassPathResource("static/vendor/image/banner.jpg");
			 //mimeMessageHelper.addInline(contentId, classPathResource);
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
					sender = (JavaMailSenderImpl) context.getBean(ALTERNATE_MAIL_SERVER1);
					sender.send(mimeMessage);
				}
			
		}
	}
	
	
	public static void sendMailWithAttachments(final String mailServer, final String from, final String password,String candidateEmail,String candidateRegno, final String subject,String FILE_PATH) {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(JavaMailConfiguration.class);
		
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		try {
			// Provided mail server.
			sender = (JavaMailSenderImpl) context.getBean(mailServer);
			logger.info("Data saved for candidate+"+candidateEmail+"prepraring for sending mail using server:"+sender.getHost().toString());
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
		
		logger.info("Setting message in mimeMessage for candidate:"+candidateEmail);
		
		MimeMessage mimeMessage = sender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setTo(candidateEmail);
			//mimeMessageHelper.setText(message, message);for plane and html text
			mimeMessageHelper.setSubject(subject);
			String contentId = ContentIdGenerator.getContentId();
			String message=""+
				       "<html>\n" + 
				       "<body>\n" + 
				       "<div style=\"margin: 0 auto;width: 70%;border: 1px solid;padding: 5px 18px;\">\n" + 
				       "<p><h3>Dear Candidate,</h3>\n" + 
				 //      "<h4>1. You have successfully registered for "+saverd.getRalley_details()+"  at "+saverd.getCity_name().toUpperCase()+" and your registration number is "+ candidate.getRalleyregistrationNo() +"</h4>\n" + 
				   //    "<h4>2. Provisional Admit Card would be mailed to the shortlisted candidates on their registered e-mail ID based on merit (Aggregate percentage obtained in Intermediate/10+2/Equivalent Exam/Diploma Courses as applicable).</h4>\n" + 
				    //   "<h4>3. Only those candidates who would be issued with Provisional Admit Card will be allowed to appear in the said Recruitment Rally.</h4> \n" + 
				     "<h4>Please find and download the provisional admit card as given below</h4>"+  
				    "<div>\n" + 
				       "<div>\n" + 
				       "<h4 style=\"margin:0 auto;1\">Regards,</h4>\n" + 
				       "<h4 style=\"margin:0 auto;1\">CASB , IAF</h4>\n" + 
				       " </div>\n" + 
				       " </div>\n" + 
				       "  </p>\n" + 
				      
				       " </div>\n" + 
				       "</body>\n" + 
				       "</html>";
		// "<div><img src=\"cid:"+ contentId +"\" width=\"100%\" height=\"400\"></div>\n" + 	 
			mimeMessageHelper.setText(message,true);
			String filepath = FILE_PATH+candidateRegno+".pdf";//change accordingly     
          DataSource source = new FileDataSource(filepath); 
			mimeMessageHelper.addAttachment(candidateRegno+".pdf", source);
			// ClassPathResource classPathResource = new ClassPathResource("static/vendor/image/banner.jpg");
			 //mimeMessageHelper.addInline(contentId, classPathResource);
			//3) create MimeBodyPart object and set your message text        
           // BodyPart messageBodyPart1 = new MimeBodyPart();     
          //  messageBodyPart1.set(message);          

            //4) create new MimeBodyPart object and set DataHandler object to this object        
//           
//			MimeBodyPart messageBodyPart2 = new MimeBodyPart();      
//          String filepath = FILE_PATH+candidateRegno+".pdf";//change accordingly     
//            DataSource source = new FileDataSource(filepath);    
//            messageBodyPart2.setDataHandler(new DataHandler(source));    
//            messageBodyPart2.setFileName(candidateRegno+".pdf");             

            //5) create Multipart object and add MimeBodyPart objects to this object        
      //      Multipart multipart = new MimeMultipart();    
           // multipart.addBodyPart(mimeMessageHelper);     
          //  multipart.addBodyPart(messageBodyPart2);     


            //6) set the multiplart object to the message object    
           
         //   mimeMessage.setContent(multipart );        
			
			
			
			
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
					sender = (JavaMailSenderImpl) context.getBean(ALTERNATE_MAIL_SERVER1);
					sender.send(mimeMessage);
				}
			
		}
	}
	
}
