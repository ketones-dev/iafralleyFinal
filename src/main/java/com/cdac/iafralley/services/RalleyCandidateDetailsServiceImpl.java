package com.cdac.iafralley.services;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
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
import org.springframework.web.multipart.MultipartFile;

import com.cdac.iafralley.Dao.RalleyCandidateDetailsDAO;
import com.cdac.iafralley.Dao.RalleyCitiesDAO;
import com.cdac.iafralley.Dao.RalleyDaywiseSlotDetailsDAO;
import com.cdac.iafralley.Dao.RalleyDetailsDAO;
import com.cdac.iafralley.Dao.RalleyGroupDAO;
import com.cdac.iafralley.Dao.RalleyStateDAO;
import com.cdac.iafralley.controllers.RalleyRegistrationFormController;
import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.entity.RalleyCities;
import com.cdac.iafralley.entity.RalleyDaywiseSlotDetails;
import com.cdac.iafralley.entity.RalleyDetails;
import com.cdac.iafralley.entity.RalleyGroup_trade;
import com.cdac.iafralley.entity.RalleyStates;
import com.cdac.iafralley.exception.CandidateAllocationSlotAreFull;
import com.cdac.iafralley.exception.CandidateDuplicateEntry;
import com.cdac.iafralley.exception.CandidateSelectedStateCitiesException;
import com.cdac.iafralley.exception.InvalidImageException;
import com.cdac.iafralley.util.RalleyIdGenrator;


@Service
public class RalleyCandidateDetailsServiceImpl implements RalleyCandidateDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(RalleyCandidateDetailsServiceImpl.class);
	
	@Autowired
	private RalleyCandidateDetailsDAO ralleyCandidateDetailsRepo;
	
	@Autowired
	private RalleyCitiesDAO conductingCities; 
	
	@Autowired
	private StoreImageFiles storeimagefile;
	
	@Autowired
	private RalleyStateDAO conductingStates; 
	
	@Autowired
	private RalleyIdGenrator ralleyIdGenrator;
	
	@Autowired
	private RalleyDetailsDAO ralleyDetailsRepo;
	
	@Autowired
	private RalleyDaywiseSlotDetailsDAO ralleyDaywiseSlotDetailsRepo;
	
	@Autowired
	private RalleyGroupDAO ralley_grp;

	@Override
	public List<RalleyCandidateDetails> findAll() {
		// TODO Auto-generated method stub
		return ralleyCandidateDetailsRepo.findAll();
	}

	@Override
	public RalleyCandidateDetails save(RalleyCandidateDetails candidate,MultipartFile x,MultipartFile xii) throws CandidateSelectedStateCitiesException, CandidateDuplicateEntry, CandidateAllocationSlotAreFull, InvalidImageException {
		// TODO Auto-generated method stub
		//check for availability and get registration No id
		
		
		
		//before saving genrating and setting candidate ralley id
		
		
		
		
		logger.info("converting and getting candidate selected state and city name");
		Map<String, String> values=getCandidateSelectedStateCityName(candidate.getState(),candidate.getCity());
		if(values.get("statename") != null && values.get("cityname") != null)
		{
			candidate.setState(values.get("statename"));
			candidate.setCity(values.get("cityname"));
			logger.info("setting values");
		}
		logger.info("before genrating id checking registring emailid is already present in DB or not...");
		RalleyCandidateDetails result=ralleyCandidateDetailsRepo.findByEmailid(candidate.getEmailid());
		RalleyCandidateDetails result2=ralleyCandidateDetailsRepo.findByAadhar_details(candidate.getAadhar_details());
		if(result != null || result2 != null)
		{
			throw new CandidateDuplicateEntry("Either emailid:"+candidate.getEmailid()+"or aadhar"+candidate.getAadhar_details()+" is already registered on this portal");
		}
		
		logger.info("Registering emailid is not present in DB so proceeding further...");
		
		
		//logger.info("checking for availability");
		//candidate=getandsetTimeVenuForCandidate(candidate);
		//logger.info("Genrating registration id and alloting daywise time to candidate");
		Optional<RalleyCities> opt_cityname = conductingCities.findById(candidate.getOpt_city());
		String VenuCode=null;
		if(opt_cityname.isPresent())
		{
			RalleyCities cityname=opt_cityname.get();
			char first= cityname.getCity().charAt(0);
			char last = cityname.getCity().charAt(cityname.getCity().length()-1);
			 VenuCode= (Character.toString(first) + Character.toString(last)).toUpperCase();
		}
		String regisrationid=ralleyIdGenrator.RalleyRegistrationNumGenrator(VenuCode);
		candidate.setRalleyregistrationNo(regisrationid);
		logger.info("for candidate with emailid:"+candidate.getEmailid()+" Genrated Candidate registration ID:"+regisrationid);
		logger.info("storing certificate paths in db and writing in disk");
		candidate=storeimagefile.storeImage(candidate, x, xii);
		
		logger.info("Before saving Candidate filled values are :"+candidate.toString());
		return ralleyCandidateDetailsRepo.save(candidate);

	}

	@Override
	public RalleyCandidateDetails findById(Long id) {
		// TODO Auto-generated method stub
		Optional<RalleyCandidateDetails> result= ralleyCandidateDetailsRepo.findById(id);
		RalleyCandidateDetails theCandidate=null;
		if(result.isPresent())
		{
			theCandidate=result.get();
		}
		return theCandidate;
	}
	
	@Override
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

	@Override
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

	

	@Override
	public List<RalleyCities> getallCites(Long stateid) {
		// TODO Auto-generated method stub
				List<RalleyCities> ralleycities=conductingCities.findAll();
				if(ralleycities == null)
				{
					ralleycities=Collections.emptyList();
					logger.warn("No cities entries in database....");
				}
				logger.info("showing all conducting ralley cities:"+ralleycities.toString());
				return ralleycities;
				
	}

	public Map<String, String> getCandidateSelectedStateCityName(String state, String city) throws CandidateSelectedStateCitiesException {
		// TODO Auto-generated method stub
		Optional<RalleyStates> selectedState = conductingStates.findById(Long.valueOf(state));
		Optional<RalleyCities> selectedCities = conductingCities.findById(Long.valueOf(city));
		if(!selectedState.isPresent() || !selectedCities.isPresent())
		{
			
			logger.warn("Either Selected State or City is not present in database");
			throw new CandidateSelectedStateCitiesException("Either Selected State or City is not present in database");
		}
		RalleyStates statename = selectedState.get();
		RalleyCities cityname = selectedCities.get();
		logger.info("state:"+statename.getState()+" city:"+cityname.getCity());
		Map<String,String> names=new HashMap<String, String>();
		names.put("statename",statename.getState());
		names.put("cityname",cityname.getCity());
		
		return names;
	}

	@Override
	public List<RalleyCities> getallCitesByStateAdminAlloted(Long stateid) {
		// TODO Auto-generated method stub
		List<Long> allocatecities= ralleyDetailsRepo.findDistinctAllotCities(stateid);
		List<RalleyCities> c=conductingCities.getAllotCities(allocatecities);
		
		return c;
	}

	@Override
	public List<RalleyStates> getralleyAllState() {
		// TODO Auto-generated method stub
		List<Long> allocatestates= ralleyDetailsRepo.findDistinctAllotStates();
		List<RalleyStates> s=conductingStates.getallStateonBasisOfList(allocatestates);
		return s;
	}

	@Override
	public Boolean getregisteredCount(Long cityid) {
		// TODO Auto-generated method stub
		Boolean avail=false;
		//get ralley id list
		List<Long> ralleyidsList=ralleyDetailsRepo.getRalleyByCitySelected(cityid);
		//get  count of each slot on basis to ralleyid
		List<RalleyDaywiseSlotDetails> eachSlotcount=ralleyDaywiseSlotDetailsRepo.getSlot(ralleyidsList);
		//get count of registered city selected by candidate
		Long countofStudentRegisteredForSelectedCity=ralleyCandidateDetailsRepo.RegisteredCandidateCount(cityid);
		
		Long sumOfTotalSlot= eachSlotcount.stream().map(slotcount -> slotcount.getNo_of_intake()).reduce((long) 0,Long::sum);
		logger.info("total slots sum:"+sumOfTotalSlot+"and registred students:"+countofStudentRegisteredForSelectedCity);
		if(sumOfTotalSlot > countofStudentRegisteredForSelectedCity)
		{
			avail=true;
		}
		
		
		
		return avail;
	}
	
	
	
	public RalleyCandidateDetails getandsetTimeVenuForCandidate(RalleyCandidateDetails c) throws CandidateAllocationSlotAreFull
	{
		//get ralley id list
				List<Long> ralleyidsList=ralleyDetailsRepo.getRalleyByCitySelected(c.getOpt_city());
				//get  count of each slot on basis to ralleyid
				List<RalleyDaywiseSlotDetails> eachSlotcount=ralleyDaywiseSlotDetailsRepo.getSlot(ralleyidsList);
				//get count of registered city selected by candidate
				Long countofStudentRegisteredForSelectedCity=ralleyCandidateDetailsRepo.RegisteredCandidateCount(c.getOpt_city());
				
				
				
				Long sumOfTotalSlot= eachSlotcount.stream().map(slotcount -> slotcount.getNo_of_intake()).reduce((long) 0,Long::sum);
				logger.info("total slots sum:"+sumOfTotalSlot+"and registred students:"+countofStudentRegisteredForSelectedCity);
				if(sumOfTotalSlot > countofStudentRegisteredForSelectedCity)
				{
					for(int i=0;i<eachSlotcount.size();i++)
					{
						String ss=convertDateToString(eachSlotcount.get(i).getDay_date());
						logger.info("count in ralley:"+eachSlotcount.get(i).getNo_of_intake());
						logger.info(""+ralleyCandidateDetailsRepo.countofscheduledStudent(convertStringToTimestamp(ss+" "+eachSlotcount.get(i).getTime_of_reporting()+":00")));
						Long slotcount=eachSlotcount.get(i).getNo_of_intake();
						Long studentcount=ralleyCandidateDetailsRepo.countofscheduledStudent(convertStringToTimestamp(ss+" "+eachSlotcount.get(i).getTime_of_reporting()+":00"));
						if(slotcount > studentcount)
						{
							String d=convertDateToString(eachSlotcount.get(i).getDay_date());
							logger.info("converted string"+d);
							
							c.setDateTimeOfReporting(convertStringToTimestamp(d+" "+eachSlotcount.get(i).getTime_of_reporting()+":00"));
							c.setVenu_details(eachSlotcount.get(i).getRalleydetails().getVenue_details());
							break;
						}
					}
					
				}
				else
				{
					throw new CandidateAllocationSlotAreFull("Slots are full");
				}
				
				return c;
				
	}

	
	
	public static Date convertStringToTimestamp(String strDate) {
	    try {
	    	
	    	
	    	
	    	
	    	
	    	SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	       // you can change format of date
	    	logger.info(strDate);
	      Date date = formatter.parse(strDate);
	      Timestamp timeStampDate = new Timestamp(date.getTime());
	      logger.info(""+timeStampDate);
	      logger.info(""+date);
	      return date;
	    } catch (ParseException e) {
	      System.out.println("Exception :" + e);
	      return null;
	    }
	  }
	
	
	public static String convertDateToString(Date strDate) {
	    try {
	      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	       // you can change format of date
	      String date = formatter.format(strDate);
	      //Timestamp timeStampDate = new Timestamp(date.getTime());
	      logger.info("in string"+date);
	      return date;
	    } catch (Exception e) {
	      System.out.println("Exception :" + e);
	      return null;
	    }
	  }
	public static String ampmFormat(String dt)
	{
		SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm:ss");
        Date date;
		try {
			date = inFormat.parse(dt);
			SimpleDateFormat outFormat = new SimpleDateFormat("hh:mm a");
	        String goal = outFormat.format(date);
	        logger.info("goal"+goal);
	        return goal;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
        
	}

	@Override
	public String showOptRalleyDetailstoCandidate(Long cityid) {
		String details="";
		// TODO Auto-generated method stub
		List<Long> ralleyidsList=ralleyDetailsRepo.getRalleyByCitySelected(cityid);
		List<RalleyDetails> rd=new ArrayList<RalleyDetails>();
		
		for(int i=0;i< ralleyidsList.size();i++)
		{
		Optional<RalleyDetails>	r=ralleyDetailsRepo.findById(ralleyidsList.get(i));
		if(r.isPresent())
		{
			rd.add(i,r.get());
		}
		else
		{
			return null;
		}
			
		}
		
		
		
		for(RalleyDetails p:rd)
		{
			details +="<h3 class='p-contents'>"+p.getRalley_details()+"</h3><h4 class='p-contents'>From "+DateFormatter(p.getStart_date()).toUpperCase()+" to "+DateFormatter(p.getEnd_date())+"</h4><h4 class='p-contents'>AT "+p.getVenue_details()+"</h4><input type='hidden' id='ralleyid_cust' name='rally_id' value="+p.getRalley_cust_id()+">";
		}
		
		logger.info(details);
		
		return details;
	}

	@Override
	public Map<String,String> showOptValidRalleyDetailstoCandidate(Long long1) {
		// TODO Auto-generated method stub
		Map<String,String> details=new HashMap<String, String>();
		List<Long> ralleyidsList=ralleyDetailsRepo.getRalleyByCitySelected(long1);
		//List<RalleyDetails> rd=new ArrayList<RalleyDetails>();
		
		for(int i=0;i< ralleyidsList.size();i++)
		{
		Optional<RalleyDetails>	r=ralleyDetailsRepo.findById(ralleyidsList.get(i));
		if(r.isPresent())
		{
			details.put("mindob", convertDateToString(r.get().getMin_dob()));
			details.put("maxdob", convertDateToString(r.get().getMax_dob()));
			details.put("height", r.get().getMin_height().toString());
			details.put("minpassing", r.get().getMin_passing_percentage().toString());
			details.put("engpassing", r.get().getMin_eng_percentage().toString());
		}
		else
		{
			return null;
		}
			
		}
		
		
		
		return details;
	}

	@Override
	public List<RalleyGroup_trade> getralleyCreatedGroups(Long long1) {
		// TODO Auto-generated method stub
		List<RalleyDetails> rds=ralleyDetailsRepo.getRalleyDetailsByCitySelected(long1);
		rds.stream().forEach(System.out::print);
		if(rds == null || rds.isEmpty())
		{
			return null;
		}
		else if( rds.get(0).getRalleyForGroup() == null)
		{
			return null;
		}
		else {
			rds.stream().forEach(System.out::print);
			logger.info(rds.get(0).getRalleyForGroup().toString());
		List<String> ids=rds.get(0).getRalleyForGroup();
		ids.stream().forEach(System.out::print);
		List<Long> grpid=ids.stream().map(Long::parseLong).collect(Collectors.toList());
		List<RalleyGroup_trade> rgt=ralley_grp.getDetails(grpid);
		rgt.stream().forEach(System.out::print);
		return rgt;
	}
	}
	
	public String DateFormatter(Date d)
	{
		
		Date convertDate=d;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");

	   String date =simpleDateFormat.format(convertDate).toUpperCase()+" ";

	    simpleDateFormat = new SimpleDateFormat("MMMM");
	    date +=simpleDateFormat.format(convertDate).toUpperCase()+" ";

	    simpleDateFormat = new SimpleDateFormat("YYYY");
	    date +=simpleDateFormat.format(convertDate).toUpperCase();
		return date;
	}

	@Override
	public Map<String, List<Object>> getralleyStateandCities(String rallyid) {
		// TODO Auto-generated method stub
		logger.info("obtained rallyid="+rallyid);
		RalleyDetails rd= ralleyDetailsRepo.findByRalley_cust_id(rallyid);
		Long sid=rd.getCandidateRestrictFromStateId();
		Optional<RalleyStates> rsid=conductingStates.findById(sid);
		RalleyStates s=null;
		if(rsid.isPresent())
		{
			s=rsid.get();
		}
		Map<String,List<Object>> details=new HashMap<String, List<Object>>();
		List<Long> cid=rd.getCandidateRestrictFromDistrictIds().stream().map(Long::parseLong).collect(Collectors.toList());
		List<RalleyCities> cities=conductingCities.getAllotCities(cid);
		cities.stream().forEach(System.out::println);
		//details.put("cities", cities);
		List<Object> list = new ArrayList <>();
		list.add(cities);
		list.add(s.getState_id());
		list.add(s.getState());
		details.put("value", list);
		
		//details.put("state",  s);
		return details;
	}
}
