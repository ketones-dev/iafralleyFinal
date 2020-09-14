package com.cdac.iafralley.util;

public interface RalleyIdGenratorInterface {

	
	

	String RalleyRegistrationNumGenrator(String preFixValue,String asc_value);
	
	String RalleyRegistrationNumGenratorUpdate(String preFixValue,String asc_value,Long Count);
	
	String RalleyCustomId(String preFixValue);
}
