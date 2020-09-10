package com.cdac.iafralley.services;

import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.exception.InvalidImageException;

@Service
@PropertySource({"classpath:applicantfilepath.properties"})
public class StoreImageFiles {
	
	@Value("${applicant.certificate}")
	private  String FILE_PATH;
	
	private boolean validtoCheck=false;
	
	private static final Logger logger = LoggerFactory.getLogger(StoreImageFiles.class);
	
	//function to change,filename,store in disk and file 
	public RalleyCandidateDetails storeImage(RalleyCandidateDetails c,MultipartFile XMarksheet,MultipartFile XIIMarksheet) throws InvalidImageException {
		
		try {
			
		if (XMarksheet.isEmpty() || XIIMarksheet.isEmpty()) {
	           logger.info("Please select a file to upload.");
	       
	        }
			else {
				logger.info("checking X and XII  marksheet");
			
				
				String mime1 = XMarksheet.getContentType();
				String mime2 = XIIMarksheet.getContentType();
				if ((mime1 != null && mime1.split("/")[0].equals("image"))
						|| (mime2 != null && mime2.split("/")[0].equals("image"))) {
					System.out.println("it is an image");
					validtoCheck=true;
				}
				else
				{
					throw new InvalidImageException("Please upload Image file only...");
				}
				if (mime1 != null && ((mime1.split("/")[1].equals("jpeg")) || (mime1.split("/")[1].equals("jpg")))
						|| (mime1.split("/")[1].equals("png"))) {
					System.out.println("it is valid an image");
					validtoCheck=true;
				}
				else {
					throw new InvalidImageException(" X marksheet should be jpg,jpeg or png  file only...");
					
				}
				if (mime2 != null && ((mime2.split("/")[1].equals("jpeg")) || (mime2.split("/")[1].equals("jpg")))
						|| (mime2.split("/")[1].equals("png"))) {
					System.out.println("it is an valid image");
					validtoCheck=true;
				}
				else {
					throw new InvalidImageException(" XII marksheet should be jpg,jpeg or png  file only...");
				}
				
				try {
					logger.info("Analyzing image for malicious code");
					analyzeImage(XMarksheet.getInputStream());
					analyzeImage(XIIMarksheet.getInputStream());
					validtoCheck = true;
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					validtoCheck = false;
					e.printStackTrace();
				}
				
				if(validtoCheck == true)
				{
					Files.createDirectories(Paths.get(FILE_PATH + "/"+c.getRalleyregistrationNo()));
					logger.info("in after checking for image validation  created file dir..on disk");
				
				String fileName1 = StringUtils.cleanPath(XMarksheet.getOriginalFilename());
				
				String fileName2 = StringUtils.cleanPath(XIIMarksheet.getOriginalFilename());
				logger.info("Renaming marksheet");
				fileName1=c.getRalleyregistrationNo()+"_X_marksheet_"+c.getEmailid()+"."+fileName1.substring(XMarksheet.getOriginalFilename().lastIndexOf(".")+1);
				
				fileName2=c.getRalleyregistrationNo()+"_XII_marksheet"+c.getEmailid()+"."+fileName2.substring(XIIMarksheet.getOriginalFilename().lastIndexOf(".")+1);
				//set ralleycandidate path in object and sanitize it also...
				//logger.info(XMarksheet.getContentType());
				//boolean validimage=isValidImage(XMarksheet.getInputStream());
				
				logger.info("file1:"+fileName1+" file2:"+fileName2);
				c.setXimagePath(c.getRalleyregistrationNo()+"/"+fileName1);
				c.setXiiimagePath(c.getRalleyregistrationNo()+"/"+fileName2);
				logger.info("copying to Path...");
				Files.copy(XMarksheet.getInputStream(), Paths.get(FILE_PATH + "/"+c.getRalleyregistrationNo()).resolve(fileName1) , StandardCopyOption.REPLACE_EXISTING);
				Files.copy(XIIMarksheet.getInputStream(), Paths.get(FILE_PATH + "/"+c.getRalleyregistrationNo()).resolve(fileName2) , StandardCopyOption.REPLACE_EXISTING);
				}
				else {
					logger.info("Image is invalid ");
					 throw new InvalidImageException(" Uploaded image is either invalid or corrupted...");
				}
				
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			throw new InvalidImageException(" can't Uploaded image is either invalid or corrupted...");
		}
		
		
		
		return c;
		
	}
	
	public boolean isValidImage(InputStream inputStream) {
		  boolean isValid = true;
		  try {
		    ImageIO.read(inputStream).flush();
		  } catch (Exception e) {
		    isValid = false;
		  }
		  return isValid;
		}
	
	public  void analyzeImage(InputStream inputStream)
	        throws NoSuchAlgorithmException, IOException, InvalidImageException {
	   

	    final InputStream digestInputStream = inputStream;
	    try {
	        final ImageInputStream imageInputStream = ImageIO
	                .createImageInputStream(digestInputStream);
	        final Iterator<ImageReader> imageReaders = ImageIO
	                .getImageReaders(imageInputStream);
	        if (!imageReaders.hasNext()) {
	            logger.info("false image cant read");
	            throw new InvalidImageException(" Uploaded image is corrupted...");
	           
	        }
	        final ImageReader imageReader = imageReaders.next();
	        imageReader.setInput(imageInputStream);
	        final BufferedImage image = imageReader.read(0);
	        if (image == null) {
	        	 logger.info("false image cant read");
	        	 throw new InvalidImageException(" Uploaded image is corrupted...");
	        }
	        image.flush();
	        if (imageReader.getFormatName().equals("JPEG") || imageReader.getFormatName().equals("PNG") || imageReader.getFormatName().equals("JPG")) {
	            imageInputStream.seek(imageInputStream.getStreamPosition() - 2);
	            final byte[] lastTwoBytes = new byte[2];
	            imageInputStream.read(lastTwoBytes);
	            if (lastTwoBytes[0] != (byte)0xff || lastTwoBytes[1] != (byte)0xd9) {
	            	 logger.info("set truncated t");
	            	 throw new InvalidImageException(" Uploaded image is corrupted...");
	            } else {
	            	 logger.info("set truncated false valid image");
	            }
	        }
	        logger.info("Image file is a non coruppted and vaid file");
	    } catch (final IndexOutOfBoundsException e) {
	    	 logger.info("set truncated t");
	    	 throw new InvalidImageException(" Uploaded image is corrupted...");
	    } catch (final IIOException e) {
	        if (e.getCause() instanceof EOFException) {
	        	 logger.info("set truncated t");
	        	 throw new InvalidImageException(" Uploaded image is corrupted...");
	        }
	    } finally {
	        digestInputStream.close();
	    }
	   
	}

}
