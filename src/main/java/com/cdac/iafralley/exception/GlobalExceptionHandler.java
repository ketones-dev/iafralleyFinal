package com.cdac.iafralley.exception;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.cdac.iafralley.entity.ErrorDetails;



@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleError404(HttpServletRequest request, Exception e)   {
    	logger.info("inside");
    	ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getRequestURI());
    	ModelAndView modal= new ModelAndView("error/404");
    	modal.addObject("error", errorDetails);
    	e.printStackTrace();
        return modal;
    }
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest request, Exception e)   {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getRequestURI());
		logger.info("inside");
		ModelAndView modal= new ModelAndView("error");
    	modal.addObject("error", errorDetails);
    	e.printStackTrace();
        return modal;
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView RuntimehandleError(HttpServletRequest request, Exception e)   {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), request.getRequestURI());
		logger.info("inside");
		ModelAndView modal= new ModelAndView("error");
    	modal.addObject("error", errorDetails);
        return modal;
    }
    
    

}
