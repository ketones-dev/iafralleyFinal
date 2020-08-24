package com.cdac.iafralley.app.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PointCutLogTraceImplServiceLayer {
	
	private static final Logger logger = LoggerFactory.getLogger(PointCutLogTraceImplServiceLayer.class);
	
	@AfterThrowing(pointcut = "com.cdac.iafralley.app.aop.PointcutLogTrace.applicationServicePackagePointcut()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
		
		logger.info("Exception in class:{} ::method:{}: with cause:{}",joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(),ex.getMessage());
		ex.printStackTrace();
		
	}

}
