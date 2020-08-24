package com.cdac.iafralley.app.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PointcutLogTrace {
	
	/**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(com.cdac.iafralley.services..*)")
    public void applicationServicePackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
    
    @Pointcut("within(com.cdac.iafralley.controllers..*)")
        public void applicationControllerPackagePointcut() {
            // Method is empty as this is just a Pointcut, the implementations are in the advices.
        }

}
