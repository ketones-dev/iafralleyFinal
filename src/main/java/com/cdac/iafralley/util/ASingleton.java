package com.cdac.iafralley.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdac.iafralley.services.RalleyCandidateDetailsServiceImpl;

public class ASingleton {

	private static volatile ASingleton instance;
	private static Object mutex = new Object();
	
	private static final Logger logger = LoggerFactory.getLogger(RalleyCandidateDetailsServiceImpl.class);

	private ASingleton() {
	}

	public static ASingleton getInstance() {
		
		ASingleton result = instance;
		if (result == null) {
			synchronized (mutex) {
				result = instance;
				if (result == null) {
					instance = result = new ASingleton();
					logger.info("singleton object value:"+result);
				}
			}
		}
		return result;
	}

}
