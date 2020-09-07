package com.cdac.iafralley.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdac.iafralley.Dao.RalleyCitiesDAO;
import com.cdac.iafralley.Dao.RalleyDaywiseSlotDetailsDAO;
import com.cdac.iafralley.Dao.RalleyDetailsDAO;
import com.cdac.iafralley.Dao.RalleyGroupDAO;
import com.cdac.iafralley.Dao.RalleyStateDAO;
import com.cdac.iafralley.entity.RalleyCities;
import com.cdac.iafralley.entity.RalleyDaywiseSlotDetails;
import com.cdac.iafralley.entity.RalleyDetails;
import com.cdac.iafralley.entity.RalleyGroup_trade;
import com.cdac.iafralley.entity.RalleyStates;

@Service
public class RalleyDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(RalleyDetailsService.class);
	
	@Autowired
	RalleyDetailsDAO ralleydetaildao;
	
	@Autowired
	RalleyDaywiseSlotDetailsDAO ralleyslotdetailsdao;
	
	@Autowired
	private RalleyCitiesDAO conductingCities; 
	
	@Autowired
	private RalleyStateDAO conductingStates; 
	
	@Autowired
	private RalleyGroupDAO ralley_grp;
	
	
	public String DateFormatter(Date d)
	{
		
		Date convertDate=d;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");

	   String date =simpleDateFormat.format(convertDate).toUpperCase()+" ";

	    simpleDateFormat = new SimpleDateFormat("MMM");
	    date +=simpleDateFormat.format(convertDate).toUpperCase()+" ";

	    simpleDateFormat = new SimpleDateFormat("YYYY");
	    date +=simpleDateFormat.format(convertDate).toUpperCase();
		return date;
	}
	
	public RalleyDetails findById(Long id)
	{
		Optional<RalleyDetails> ralleyDetail=ralleydetaildao.findById(id); 
		RalleyDetails rs=null;
		if(ralleyDetail.isPresent())
		{
			rs=ralleyDetail.get();
		}
		return rs;
		
	}
	
	public RalleyDetails findByCustomId(String id)
	{
		RalleyDetails ralleyDetail=ralleydetaildao.findByRalley_cust_id(id); 
		
		if(ralleyDetail != null)
		{
			return ralleyDetail;
		}
		return ralleyDetail;
		
	}
	
	public List<RalleyDaywiseSlotDetails> getAllSlot(RalleyDetails rd){
		List<RalleyDaywiseSlotDetails> rds=ralleyslotdetailsdao.findByRalleydetails(rd);
		
		return rds;
		
	}
	
	
	public List<RalleyDetails> getAllRalleyDetails()
	{
		List<RalleyDetails> rs=ralleydetaildao.findAll();
		return rs;
		
	}
	
	public void saveRalleyDetails(RalleyDetails r)
	{
		System.out.println(r.getRalley_id());
		ralleydetaildao.save(r);
	}
	
	/*
	 * public void updateRalleyDetails(RalleyDetails r) { Optional<RalleyDetails>
	 * rds=ralleydetaildao.findById(r.getRalley_id());
	 * 
	 * if(rds.isPresent()) {
	 * 
	 * } ralleydetaildao.savean; }
	 */
	
	public void deleteRalleyDetails(Long id)
	{
		RalleyDetails ralleyDetails=ralleydetaildao.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
	ralleydetaildao.delete(ralleyDetails);
	}

	
	public List<RalleyStates> getallState() {
		// TODO Auto-generated method stub
		List<RalleyStates> ralleystates=conductingStates.findAll();
		if(ralleystates == null)
		{
			ralleystates=Collections.emptyList();
			logger.warn("No states entries in database....");
		}
		logger.info("showing all conducting ralley states:"+ralleystates.toString());
		return ralleystates;
	}

	
	public List<RalleyCities> getallCitesByState(Long stateid) {
		// TODO Auto-generated method stub
		List<RalleyCities> ralleycities=conductingCities.getallCities(stateid);
		if(ralleycities == null)
		{
			
			logger.warn("No cities entries in database....");
		}
		logger.info("showing all conducting ralley cities:"+ralleycities.toString());
		return ralleycities;
		
		
		
	}
	
	public List<RalleyDetails> findDistinctCity_id(){
		List<RalleyDetails> ralleydc=ralleydetaildao.findDistinctCity_id();
		return ralleydc;
 	}
	
	public List<RalleyGroup_trade> getAllgroups(){
		List<RalleyGroup_trade> groups=ralley_grp.findAll();
		return groups;
	}

	public List<RalleyCities> getMultipleCitesByState(Long long1) {
		// TODO Auto-generated method stub
		Optional<RalleyDetails> details =ralleydetaildao.findById(long1);
		List<String> citiesid=new ArrayList<String>();
		if(details.isPresent())
		{
		   citiesid=details.get().getCandidateRestrictFromDistrictIds();
		}
		
List<Long> intList = citiesid.stream()
                               .map(Long::valueOf)
                               .collect(Collectors.toList());


		
		List<RalleyCities> lists=conductingCities.getAllotCities(intList);
		return lists;
	}
	
	public List<String> RalleyDetailsHeading(){
		List<RalleyDetails> rd=ralleydetaildao.findDistinctHeadingDetails();
		List<String> conductingrallyDetails=new ArrayList<String>();
		if(rd == null || rd.isEmpty())
		{
			logger.info("No rallies are active for registration");
			conductingrallyDetails.add("Seem Like there is No OnGoing Rally Recruitment ..");
			return conductingrallyDetails;
		}
		
		for(int i=0;i<rd.size();i++)
		{
			conductingrallyDetails.add((i+1)+". "+rd.get(i).getRalley_details()+"-"+rd.get(i).getCity_name().toUpperCase()+" (FROM "+
					DateFormatter(rd.get(i).getStart_date()).toUpperCase()+" to "+ DateFormatter(rd.get(i).getEnd_date())+")");
		}
		
		return conductingrallyDetails;
		
	}

	public Map<String,String> getAllRalleyCount() {
		// TODO Auto-generated method stub
		List<String> ls=ralleydetaildao.getAllRalleyCount();
		
		Map<String,String> m=new HashMap<String, String>();
		logger.info("Dashboard show Records are empty:"+ls.isEmpty());
		
		for(String l:ls)
		{
			logger.info("v"+l);
			String row[] =l.split(",");
			m.put(row[0], row[1]);
			
		}
		
		
		
		return m;
	}

	
}
