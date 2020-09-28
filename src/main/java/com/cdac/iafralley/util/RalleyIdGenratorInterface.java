package com.cdac.iafralley.util;

import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.exception.CandidateDuplicateEntry;

public interface RalleyIdGenratorInterface {

	
	

	String RalleyRegistrationNumGenrator(String preFixValue,String asc_value,RalleyCandidateDetails rcd) throws CandidateDuplicateEntry;
	
	String RalleyRegistrationNumGenratorUpdate(String preFixValue,String asc_value,Long Count);
	
	String RalleyCustomId(String preFixValue);
}
