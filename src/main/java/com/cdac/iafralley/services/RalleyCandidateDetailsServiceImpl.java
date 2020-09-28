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

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
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
import com.cdac.iafralley.exception.CandidateInvalidInputAsPerCrietria;
import com.cdac.iafralley.exception.CandidateSelectedStateCitiesException;
import com.cdac.iafralley.exception.InvalidImageException;
import com.cdac.iafralley.util.ASingleton;
import com.cdac.iafralley.util.RalleyIdGenrator;


@Service
@Transactional(rollbackFor = {CandidateDuplicateEntry.class})
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
	public RalleyCandidateDetails save(RalleyCandidateDetails candidate,MultipartFile x,MultipartFile xii,MultipartFile cand_photo) throws CandidateSelectedStateCitiesException, CandidateDuplicateEntry, CandidateAllocationSlotAreFull, InvalidImageException {
		// TODO Auto-generated method stub
		//check for availability and get registration No id
		
		
		
		//before saving genrating and setting candidate ralley id
		
		
		//synchronized (ASingleton.getInstance()) {
		
		
		logger.info("converting and getting candidate selected state and city name");
		Map<String, String> values=getCandidateSelectedStateCityName(candidate.getState(),candidate.getCity());
		if(values.get("statename") != null && values.get("cityname") != null)
		{
			candidate.setState(values.get("statename"));
			candidate.setCity(values.get("cityname"));
			logger.info("setting values");
		}
		logger.info("before genrating id checking registring emailid and aadhar is already present in DB or not...");
		try {
		logger.info(candidate.getEmailid().toLowerCase().toString());
		String cemailid=candidate.getEmailid().toLowerCase();
		RalleyCandidateDetails result=ralleyCandidateDetailsRepo.findByEmailidAndRallyid(cemailid,candidate.getRally_id());
		RalleyCandidateDetails result2=ralleyCandidateDetailsRepo.findByAadhar_details(candidate.getAadhar_details(),candidate.getRally_id());
		//RalleyCandidateDetails result3=ralleyCandidateDetailsRepo.findByContact_no(candidate.getContactno(), candidate.getRally_id());
				if(result != null && result2 != null )
				{
					
					throw new CandidateDuplicateEntry("Registration with given details is already done.Cannot register with same  details.");
				}
				if(result != null && result2 != null )
				{
					
					throw new CandidateDuplicateEntry("Registration with given details is already done.Cannot register with same  details.");
				}
				if(result != null )
				{
					
					throw new CandidateDuplicateEntry("emailid:"+candidate.getEmailid()+" is already registered.Cannot register with same details.");
				}
				if(result2 != null)
				{
					
					throw new CandidateDuplicateEntry("Aadhar No:"+candidate.getAadhar_details()+" is already registered.Cannot register with same details.");
				}
		}
		catch (Exception e){
			
			throw new CandidateDuplicateEntry("Registration with given details is already done.Cannot register with same  details.");
			
		}
		
		
		/*
		 * if(result3 != null) {
		 * 
		 * throw new CandidateDuplicateEntry("Mobile no:"+candidate.getContactno()
		 * +" is already registered.Cannot register with same details."); }
		 */
		
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
		String ascvalue=ralleyDetailsRepo.findByRalley_cust_id(candidate.getRally_id()).getAscNumber();
		logger.info("Checking image validation and for corruption..");
	
		
		boolean imagevalid=storeimagefile.checkImageValidation(x,xii,cand_photo);
		if(imagevalid == false)
		{
			throw new InvalidImageException("Cant' read uploaded images please try again...");
		}
		
		RalleyDetails ForeignIdValue=ralleyDetailsRepo.findByRalley_cust_id(candidate.getRally_id());
		candidate.setRallyDetails(ForeignIdValue);
		
		
		logger.info("Before saving Candidate filled values are :"+candidate.toString());
		
		
		try {
		
		
		String regisrationid=ralleyIdGenrator.RalleyRegistrationNumGenrator(VenuCode,ascvalue,candidate);
		candidate.setRalleyregistrationNo(regisrationid);
		logger.info("for candidate with emailid:"+candidate.getEmailid()+" Genrated Candidate registration ID:"+regisrationid);
			RalleyCandidateDetails savedD=ralleyCandidateDetailsRepo.save(candidate);
		//String regisrationid=ralleyIdGenrator.RalleyRegistrationNumGenratorUpdate(VenuCode, ascvalue, savedD.getId());
		//savedD.setRalleyregistrationNo(regisrationid);
			logger.info("storing certificate paths in db and writing in disk");
		savedD=storeimagefile.storeImage(savedD, x, xii,cand_photo);
			
			return savedD;
			
			//return ralleyCandidateDetailsRepo.save(savedD);
			
		//}
		}
		catch(DataIntegrityViolationException e)
		{
			e.printStackTrace();
			logger.info(e.getMessage());
			throw new CandidateDuplicateEntry("Opps..Something went wrong please try again..");
			
		}
		catch (ConstraintViolationException tse) {
			tse.printStackTrace();

			logger.info(tse.getMessage());
			throw new CandidateDuplicateEntry("Opps..Something went wrong please try again..");

		}
		catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new CandidateDuplicateEntry("Opps..Something went wrong please try again..");
		}
		
		

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
		Boolean avail=true;
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
			details +="<h3 class='p-contents'>"+p.getRalley_details()+"-"+p.getCity_name().toUpperCase()+"</h3><h4 class='p-contents'>From "+DateFormatter(p.getStart_date()).toUpperCase()+" to "+DateFormatter(p.getEnd_date())+"</h4><!--<h4 class='p-contents'>AT "+p.getVenue_details()+"</h4>--><input type='hidden' id='ralleyid_cust' name='rally_id' value="+p.getRalley_cust_id()+">";
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
		Map<String,List<Object>> details=new HashMap<String, List<Object>>();
		List<Object> list = new ArrayList <>();
		RalleyDetails rd= ralleyDetailsRepo.findByRalley_cust_id(rallyid);
		Long sid=rd.getCandidateRestrictFromStateId();
		List<RalleyCities> utcities=new ArrayList<RalleyCities>();
		List<RalleyStates> utstate=new ArrayList<RalleyStates>();
		
		
		
		
		
		Optional<RalleyStates> rsid=conductingStates.findById(sid);
		RalleyStates s=null;
		if(rsid.isPresent())
		{
			s=rsid.get();
		}
	
		List<Long> cid=rd.getCandidateRestrictFromDistrictIds().stream().map(Long::parseLong).collect(Collectors.toList());
		List<RalleyCities> cities=conductingCities.getAllotCities(cid);
		cities.stream().forEach(System.out::println);
		//details.put("cities", cities);
		
		
		
		
		list.add(cities);
		list.add(s.getState_id());
		list.add(s.getState());
		
		if(!(rd.getUtStates() == null))
			  
		{
	List<RalleyCities> c=new ArrayList<RalleyCities>();
			
			for (String id : rd.getUtStates()) {
				
				Long i=Long.parseLong(id);
				//get state details
				Optional<RalleyStates> stateut=conductingStates.findById(i);
				if(stateut.isPresent())
				{
					logger.info("state"+stateut.get());
					utstate.add(stateut.get());
					list.add(stateut.get().getState_id());
					list.add(stateut.get().getState());
					
				}
				 c=conductingCities.getallCities(i);
				if (c == null) {
					
				} else {
					// get city details
					for (RalleyCities rc : c) {
						utcities.add(rc);
					}
					
				}
				
			}
			if (utcities == null) {
				logger.info("no cities");
			} else {
				list.add(utcities);
			}
			
			for(RalleyCities cc:utcities)
			{
				System.out.println(cc.toString());
			}
		}
		
		
		details.put("value", list);
		
		
		
		//details.put("state",  s);
		return details;
	}

	@Override
	public List<String> getDuplicateValidation(String email, String aadhar, String id) {
		// TODO Auto-generated method stub
		List<String> ls=new ArrayList<String>();
		RalleyCandidateDetails result=ralleyCandidateDetailsRepo.findByEmailidAndRallyid(email,id);
		RalleyCandidateDetails result2=ralleyCandidateDetailsRepo.findByAadhar_details(aadhar,id);
		if(result !=null && result2 !=null)
		{
			ls.add("both");
			return ls;
		}
		else if(result != null)
		{
			ls.add("email");
			return ls;
		}
		else if(result2 !=null)
		{
			ls.add("aadhar");
			return ls;
		}
		
		ls.add("");
		return ls;
	}
	
	
	
}
