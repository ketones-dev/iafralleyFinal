package com.cdac.iafralley.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PropertySource({"classpath:applicantfilepath.properties"})
public class FileUploadAndDownloadController {
	
	@Value("${applicant.filepath}")
	private  String FILE_PATH;
	private static final Logger logger = LoggerFactory.getLogger(FileUploadAndDownloadController.class);
	@RequestMapping("/pdf")
    public void downloadPDFResource( HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     @RequestParam("fileName") String fileName,
                                     @RequestHeader String referer) 
    {
		/*
		 * //Check the renderer if(referer != null && !referer.isEmpty()) { //do nothing
		 * //or send error }
		 */
        //If user is not authorized - he should be thrown out from here itself
         
        //Authorized user will download the file
		
		
        
        Path file = Paths.get(FILE_PATH+fileName+".pdf");
        logger.info("in dowload with :"+FILE_PATH+"file name:"+file+"path exist:"+Files.exists(file));
        if (Files.exists(file)) 
        {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename="+fileName);
            try
            {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } 
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

}
	
	
	
}
