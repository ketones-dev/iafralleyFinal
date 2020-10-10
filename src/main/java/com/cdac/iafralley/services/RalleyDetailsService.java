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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.iafralley.Dao.RalleyAllocationDao;
import com.cdac.iafralley.Dao.RalleyCandidateDetailsDAO;
import com.cdac.iafralley.Dao.RalleyCitiesDAO;
import com.cdac.iafralley.Dao.RalleyDaywiseSlotDetailsDAO;
import com.cdac.iafralley.Dao.RalleyDetailsDAO;
import com.cdac.iafralley.Dao.RalleyGroupDAO;
import com.cdac.iafralley.Dao.RalleyStateDAO;
import com.cdac.iafralley.Dao.RallySlotMasterDao;
import com.cdac.iafralley.Dao.TempAllocation;
import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.entity.RalleyCities;
import com.cdac.iafralley.entity.RalleyDaywiseSlotDetails;
import com.cdac.iafralley.entity.RalleyDetails;
import com.cdac.iafralley.entity.RalleyGroup_trade;
import com.cdac.iafralley.entity.RalleyStates;
import com.cdac.iafralley.entity.RallyApplicantAllocation;
import com.cdac.iafralley.entity.RallySlotMaster;
import com.cdac.iafralley.entity.TempRallyApplicantAllocation;
import com.cdac.iafralley.mailConfig.MailingService;
import com.cdac.iafralley.util.RegisterdCandidatePDFReport;

@Service
@Transactional
@PropertySource({"classpath:mailserver.properties"})
@PropertySource({"classpath:applicantfilepath.properties"})
public class RalleyDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(RalleyDetailsService.class);
	
	@Value("${config.mailserver}")
    private String mailserver;
 
    @Value("${config.from}")
    private String from;
 
    @Value("${config.password}")
    private String password;
    
	 @Value("${applicant.filepath}")
		private String FILE_PATH;
	 
	 private String subject="RALLY RECRUITMENT PROVISIONAL ADMIT-CARD";
	
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
	
	@Autowired
	private RallySlotMasterDao rally_slot_master;
	
	@Autowired
	private RalleyAllocationDao rally_allocate;
	
	@Autowired
	private RalleyCandidateDetailsDAO rcdDao;
	
	@Autowired
	private TempAllocation tempAllocationDao;
	
	
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

	public List<RallySlotMaster> findSlotOnBasisOfRallyId(Long rallyid) {
		// TODO Auto-generated method stub
		List<RallySlotMaster> rsm=rally_slot_master.findSlotOnBasisOfRallyIdFromSlotMaster(rallyid);
		return rsm;
	}

	public void findDataInAllocationMapping(Long rallyid, Long slotid) {
		// TODO Auto-generated method stu
		try {
		List<RallyApplicantAllocation> r=rally_allocate.findByRally_idAndSlot_id(rallyid, slotid);
		RallySlotMaster slotd=rally_slot_master.findById(slotid).orElseThrow(() -> new IllegalArgumentException("Not found"));
		if(r != null) {
		int count=0;
		for(RallyApplicantAllocation rdata : r)
		{
			RalleyDetails rd=ralleydetaildao.findById(rdata.getRally_id()).orElseThrow(() -> new IllegalArgumentException("Not found"));
			
			RalleyCandidateDetails candidate=rcdDao.findByEmailidAndRallyidAdmitCard(rdata.getApplicant_id(),rdata.getEmailid(),rdata.getCandidate_registration_no());
			if(candidate != null) {
			RegisterdCandidatePDFReport.createPDF(candidate,rd,rdata,slotd,FILE_PATH);
			logger.info("Admit card for candidate email:"+candidate.getEmailid()+"has been created successfull");
			logger.info("setting value of allocation and admit card flag to true in db");
			rcdDao.updateAllocationStatus(true,true,candidate.getId());
			rcdDao.updatePath(rdata.getCandidate_registration_no()+"_"+candidate.getId()+".pdf",candidate.getId(),candidate.getEmailid());
			count++;
			logger.info("Candidate's admit card genrated Count:"+count);
			}
			else
			{
				logger.info("No candidate detail email id or rallyregNo mismatch in tables"+rdata.getEmailid());
			}
			
		}
		}
		else
		{
			logger.info("No data to create pdf");
		}
		}catch(Exception e)
		{
			logger.info("error"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public List<RalleyCandidateDetails> getCandidateDataOnBasisOfCity(Long cityid)
	{
		return null;
		
	}

	public void findDataForEmailAllocationMapping(Long rallyid, Long slotid) {
		// TODO Auto-generated method stub
		try {
			List<RallyApplicantAllocation> r=rally_allocate.findByRally_idAndSlot_id(rallyid, slotid);
			
			if(r != null)
			{
				int count=0;
				for(RallyApplicantAllocation rdata : r)
				{
					RalleyDetails rd=ralleydetaildao.findById(rdata.getRally_id()).orElseThrow(() -> new IllegalArgumentException("Not found"));
					RalleyCandidateDetails mailcandidate=rcdDao.findByEmailidAndRallyidFormail(rdata.getApplicant_id(),rdata.getEmailid(),rdata.getCandidate_registration_no(),rd.getRalley_id());

					if(mailcandidate !=null)
					{
					logger.info("Preparing to send to mail to emailid:"+rdata.getEmailid()+"with regno:"+rdata.getCandidate_registration_no());
					
					  try {
						  
						  MailingService.sendMailWithAttachments(mailserver, from, password, rdata.getEmailid(),rdata.getCandidate_registration_no(),rdata.getApplicant_id(),
						 rd.getCity_name(),rd.getState_name(),subject,FILE_PATH); 
						  
						  logger.info("Mail successfully send to emailid:"+rdata.getEmailid());
						  logger.info("updating send mail status for emailid:"+rdata.getEmailid());
						  rcdDao.updateMailSendStauts(true,mailcandidate.getId(),rd.getVenue_details(),rdata.getSlot_id());
						  count++;
						  logger.info("Total mail sent for City(Rally)"+rdata.getRally_id()+" is:"+count);
						  
					  } catch(Exception e) { 
						  e.printStackTrace();
						  
						  }
					}
					else
					{
						logger.info("No Candidate seems to allocate for rallyid"+rd.getCity_name() +"with id:"+rd.getRalley_cust_id());
					}
				}
			}
			
			
		}
		catch(Exception e)
		{
			logger.error("mail sending issue");
			e.getMessage();
			e.printStackTrace();
		}
		
	}

	public void addDuplicateEntry(List<Long> list) {
		// TODO Auto-generated method stub
		List<RalleyCandidateDetails> r=rcdDao.getDetailsOnBasisOfIds(list);
		
		//insert in temp allocation table
		for(RalleyCandidateDetails details : r)
		{
			TempRallyApplicantAllocation tempdata=new TempRallyApplicantAllocation(details.getId(),details.getRallyDetails().getRalley_id(), details.getEmailid(),details.getRalleyregistrationNo(),true,true,false,(long)1);
			tempAllocationDao.save(tempdata);
			rcdDao.updateDuplicateFlag(details.getId());
		}
		
		//update in candidate details table
		
		
	}
	
	public void addOnlyRejectEntry(List<Long> list) {
		// TODO Auto-generated method stub
		List<RalleyCandidateDetails> r=rcdDao.getDetailsOnBasisOfIds(list);
		
		//insert in temp allocation table
		for(RalleyCandidateDetails details : r)
		{
			TempRallyApplicantAllocation tempdata=new TempRallyApplicantAllocation(details.getId(),details.getRallyDetails().getRalley_id(), details.getEmailid(),details.getRalleyregistrationNo(),false,true,false,(long)1);
			tempAllocationDao.save(tempdata);
			rcdDao.updateDuplicateFlag(details.getId());
		}
		
		//update in candidate details table
		
		
	}

	public List<RallySlotMaster> getSlotOnBasisOfId(Long cityid) {
		// TODO Auto-generated method stub
		List<Long> rds=ralleydetaildao.getRalleyByCitySelected(cityid);
		List<RallySlotMaster> slotdetails=new ArrayList<RallySlotMaster>();
		if(!rds.isEmpty())
		{
			slotdetails=rally_slot_master.getSlotOnSingleId(rds.get(0));
		}
		return slotdetails;
	}

	public RallySlotMaster getSlotdata(Long cityid, Long slotid) {
		// TODO Auto-generated method stub
		return rally_slot_master.getSlotDetails(slotid);
	}

	
}
