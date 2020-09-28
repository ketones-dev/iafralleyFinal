package com.cdac.iafralley.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.iafralley.Dao.RalleyCandidateDetailsDAO;
import com.cdac.iafralley.Dao.RalleyDetailsDAO;
import com.cdac.iafralley.controllers.RalleyRegistrationFormController;
import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.exception.CandidateDuplicateEntry;

@Component
@Transactional
public class RalleyIdGenrator implements RalleyIdGenratorInterface{
	
	//starting registration number suffix
	public static final String R_NO_FIRST="00001";
	
	//auto wired admin ralley pref dao to get particular cities venu and id
	//auto wired candidatedetails doa to set value and get city pref
	@Autowired
	RalleyCandidateDetailsDAO ralleyCandidateDetailsDAO;
	
	
	@Autowired
	RalleyDetailsDAO ralleyDetailsDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(RalleyIdGenrator.class);
	
	
	private String year_cycle="20";
	private String asc_value="15";

	//add param as of candidate pref city for ralley and check if count for such city is there if 0 then send error message
	@Override
	public   String RalleyRegistrationNumGenrator(String preFixValue,String asc,RalleyCandidateDetails rcd) throws CandidateDuplicateEntry {
		String regno=null;
		String prefix_venu=preFixValue;
		try {
			
		synchronized (ASingleton.getInstance()) {
			
			String count=ralleyCandidateDetailsDAO.maxCount(rcd.getOpt_state(),rcd.getOpt_city());
			
			
			logger.info("Db Max count value:"+count);
			// checking it is first time registration number is genrated
			if(count == null)
			{
				regno=prefix_venu+year_cycle+asc+R_NO_FIRST;
			}
			
			// else get max count from db for such city and state of such day registerd count-acutal intake count
			// =0 then alloted next day registration count and add 1 to its id
			//FCFS
			else {
				String id= ConvertLongToStringwithPaddedzero(Long.parseLong(count)+1,5);
				regno=prefix_venu+year_cycle+asc+id;
			}
			
		}
		//get count of value
		
		
		
		
		}catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Problem in creating reg number of candidate with email id:"+rcd.getEmailid());
			throw new CandidateDuplicateEntry("registration duplicay error");
		}
		
		return regno;
		
		
		
		
	}
	
	public static String ConvertLongToStringwithPaddedzero(Long num, int digits) {
	    String output = Long.toString(num);
	    while (output.length() < digits) output = "0" + output;
	    return output;
	}

	@Override
	public   String RalleyCustomId(String preFixValue) {
		String rregno=null;
		
		//get count of value
		String rcount=ralleyDetailsDAO.maxCount();
		
		// checking it is first time registration number is genrated
		if(rcount == null)
		{
			rregno=preFixValue+R_NO_FIRST;
		}
		
		// else get max count from db for such city and state of such day registerd count-acutal intake count
		// =0 then alloted next day registration count and add 1 to its id
		//FCFS
		else {
			String rid= ConvertLongToStringwithPaddedzero(Long.parseLong(rcount)+1,5);
			rregno=preFixValue+rid;
		}
		
		
		return rregno;
	}

	@Override
	public String RalleyRegistrationNumGenratorUpdate(String preFixValue, String asc_value, Long Count) {
		// TODO Auto-generated method stub
		String id= ConvertLongToStringwithPaddedzero(Count,5);
		String rregno=preFixValue+year_cycle+asc_value+id;
		
		
		
		
		return rregno;
	}
	
	
	

}
