package com.cdac.iafralley.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.entity.RalleyCities;
import com.cdac.iafralley.entity.RalleyDaywiseSlotDetails;
import com.cdac.iafralley.entity.RalleyDetails;
import com.cdac.iafralley.entity.RallySlotMaster;
import com.cdac.iafralley.services.RalleyCandidateDetailsService;
import com.cdac.iafralley.services.RalleyCandidateDetailsServiceImpl;
import com.cdac.iafralley.services.RalleyDetailsService;
import com.cdac.iafralley.user.RalleyDetailsDTO;
import com.cdac.iafralley.user.TempAllocationDto;
import com.cdac.iafralley.util.RalleyIdGenrator;

@Controller
@RequestMapping("/Dashboard")
public class AdminController {
	
	@Autowired
	RalleyDetailsService rdservice;
	@Autowired
	RalleyCandidateDetailsService candidateService;
	@Autowired
	private RalleyIdGenrator ralleyIdGenrator;
	
	private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("hh:mm:ss");
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	
	private java.util.Date parseTimestamp(String timestamp) {
	    try {
	        return DATE_TIME_FORMAT.parse(timestamp);
	    } catch (ParseException e) {
	        throw new IllegalArgumentException(e);
	    }
	}
	
	
	@GetMapping("/home")
	public String showDashboard(Model m) {
		System.out.println("in dashboard");
		m.addAttribute("ListCountOfCities", rdservice.getAllRalleyCount());
		
		
		
		return "Dashboard";
		
	}
	
	@GetMapping("/ShowRalleyDetails")
	public String showRalleyDetails(Model model) {
		System.out.println("in dashboard");
		model.addAttribute("ralleyDetails", rdservice.getAllRalleyDetails());
		return "ShowRalleyDetails";
		
	}
	
	@GetMapping("/ShowCreateRalley")
	public ModelAndView showCreateRalleyForm(RalleyDetails rd) {
		ModelAndView mv=new ModelAndView("CreateRalley");
		System.out.println("in dashboard");
		
		mv.addObject("ralleyDetails", rd);
		mv.addObject("slotlist", rd.getRalleydaywiseSlot());
		mv.addObject("allStates", rdservice.getallState());
		mv.addObject("candidateRestrictAllStates",rdservice.getallState());
		mv.addObject("allgroups",rdservice.getAllgroups());
		return mv;
		
	}
	
	@PostMapping("/CreateRalley")
	public String showCreateRalley(@Valid  RalleyDetailsDTO rd,BindingResult result,Model model ) {
		System.out.println("in create ralley");

		if (result.hasErrors()) {
			result
			  .getFieldErrors() .stream() .forEach(f -> System.out.println(f.getField() +
			  ": " + f.getDefaultMessage()));
			model.addAttribute("ralleyDetails", rd);
			return "CreateRalley";
		}

		//rd.setCity_id((long) 1);
		//rd.setState_id((long) 1);
		System.out.println(rd.toString());
		LocalDate std=LocalDate.parse(convertDateToString(rd.getStart_date()));
		String prefixvalue=String.valueOf(std.getMonth()).substring(0, 3).toUpperCase()+String.valueOf(std.getDayOfMonth())+String.valueOf(std.getYear()).substring(2)+rd.getCity_name().substring(0, 2).toUpperCase();
		String custid=ralleyIdGenrator.RalleyCustomId(prefixvalue);
		
		rd.getCandidateRestrictFromDistrictIds().stream().forEach(System.out::println);
		
		RalleyDetails ralleyDetails = new RalleyDetails(rd.getRalley_id(),rd.getState_id(), rd.getCity_id(), rd.getRalley_details(),
				rd.getVenue_details(), rd.getStart_date(), rd.getEnd_date(), rd.getNo_OfDays(), rd.getMin_dob(),
				rd.getMax_dob(), rd.getMin_passing_percentage(), rd.getMin_eng_percentage(), rd.getMin_height(),rd.getCity_name(),rd.getState_name(),custid,rd.getCandidateRestrictFromStateId());

		ralleyDetails.setRalleyForGroup(rd.getRalleyForGroup());
		ralleyDetails.setCandidateRestrictFromDistrictIds(rd.getCandidateRestrictFromDistrictIds());
		ralleyDetails.setActive(rd.getActive());
		ralleyDetails.setAscNumber(rd.getAscNumber());
		
		List<RalleyDaywiseSlotDetails> slotlist = rd.getRalleydaywiseSlot();

		List<RalleyDaywiseSlotDetails> rds = rd.getRalleydaywiseSlot();
		rds.forEach(System.out::println);

		slotlist.forEach(p -> {
			RalleyDaywiseSlotDetails newobj = new RalleyDaywiseSlotDetails(p.getSlot_id(),p.getNo_of_intake(), p.getDay_date(),
					p.getTime_of_reporting());
			ralleyDetails.add(newobj);
		});

		rdservice.saveRalleyDetails(ralleyDetails);

		return "redirect:/Dashboard/ShowRalleyDetails";
		
	}
	
	@GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        RalleyDetails rs = rdservice.findById(id);
       // List<RalleyDaywiseSlotDetails> sdetails=rd
        List<RalleyDaywiseSlotDetails> rds=rdservice.getAllSlot(rs) ;
        System.out.println(rs.toString());
            if(rs == null)
            {
            	model.addAttribute("error", "id does exist any more");
            	return "ShowRalleyDetails";
            }
           if( rds.isEmpty())
           {
        	   System.out.println("true");
           }
            rds.forEach(System.out::println);
           // model.addAttribute("slotlist", rds);    
        model.addAttribute("ralleyDetails", rs);
        model.addAttribute("allStates", rdservice.getallState());
        model.addAttribute("allgroups",rdservice.getAllgroups());
        model.addAttribute("candidateRestrictAllStates",rdservice.getallState());
        model.addAttribute("districtlist",rdservice.getMultipleCitesByState(id));
        return "CreateRalley";
    }

	
	@PostMapping("/update/{id}")
    public String updateRalley(@PathVariable("id") Long id, @Valid RalleyDetails ralleydetails, BindingResult result,
        Model model) {
        if (result.hasErrors()) {
        	
            ralleydetails.setRalley_id(id);
            return "update-ralley";
        }

        rdservice.saveRalleyDetails(ralleydetails);
        model.addAttribute("ralleyDetailsshow", rdservice.getAllRalleyDetails());
        return "redirect:/Dashboard/ShowRalleyDetails";
    }
	
	 @GetMapping("/delete/{id}")
	    public String deleteRalleyDetail(@PathVariable("id") Long id, Model model) {
	        
	          
	        rdservice.deleteRalleyDetails(id);
	        model.addAttribute("ralleyDetails", rdservice.getAllRalleyDetails());
	        return "redirect:/Dashboard/ShowRalleyDetails";
	    }
	 
	 @GetMapping("/ShowRegisteredStudentData")
	 public ModelAndView ShowRegisteredStudentData()
	 {
		 ModelAndView m= new ModelAndView("RegisteredStudentData");
		 List<RalleyCandidateDetails> rd=candidateService.findAll();
		 m.addObject("studentdata", rd);
		 return m;
	 }
	 
	 
	 @GetMapping("/ShowRegisteredStudentDataForAllocation")
	 public String ShowRegisteredStudentDataForAllocation(Model m)
	 {
		// ModelAndView m= new ModelAndView("RegisteredStudentDataForAllocation");
		 
		// List<RalleyCandidateDetails> rd=candidateService.findAll();
		m.addAttribute("cityid", (long)0);
		return findPaginated(1, "name", "asc",(long) 0, m);
		
		
		
	 }
	 
	 @GetMapping("/getralleyallotCitiesOnSelect/{cityid}/{pageNo}")
	 public String ShowRegisteredStudentDataForAllocationCityWise(Model m,@PathVariable (value = "cityid") Long cityid,@PathVariable (value = "pageNo") int pageNo)
	 {
		// ModelAndView m= new ModelAndView("RegisteredStudentDataForAllocation");
		 
		// List<RalleyCandidateDetails> rd=candidateService.findAll();
		// m.addAttribute("cityid", cityid);
		 
		return findPaginated(pageNo, "name", "asc",cityid, m);
		
		
		
	 }
	 
	 
	 @GetMapping("/page/{pageNo}")
		public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
				@RequestParam("sortField") String sortField,
				@RequestParam("sortDir") String sortDir,@RequestParam("cityid") Long cityid,
				Model model) {
			int pageSize = 200;
			Page<RalleyCandidateDetails> page= null;
			logger.info("cityid value="+cityid);
			if (cityid != 0) {
			 page = candidateService.findPaginated(pageNo, pageSize, sortField, sortDir,cityid);}
			else {
				 page = candidateService.findPaginated(pageNo, pageSize, sortField, sortDir,(long) 0);
				 }
			List<RalleyCandidateDetails> listEmployees = page.getContent();
			logger.info(""+listEmployees.size());
			
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			
			
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		//	List<RalleyDetails> entityList= rdservice.findDistinctCity_id();
			
		//	model.addAttribute("cities",entityList);
			
			model.addAttribute("cityid", cityid);
			model.addAttribute("studentdata", listEmployees);
			return "RegisteredStudentDataForAllocation";
		}
	 
	 @GetMapping("/getFilteredData/{intake}/{minpercentage}/{cityid}")
	 public String getFilteredData(Model model,@PathVariable (value = "intake") int intake,@PathVariable (value = "minpercentage") int passPercentage,@PathVariable (value = "cityid") Long cityid)
	 {
		
		 logger.info("intake count to fileter:"+intake+" for min percentage:"+passPercentage+" city:"+cityid);
		 //getlist of intake wise filtered data
		 List<RalleyCandidateDetails> studentdata=candidateService.getintakebaseFilteredData(intake,passPercentage,cityid);
		 List<RallySlotMaster> slotdetails =rdservice.getSlotOnBasisOfId(cityid);
		List<RalleyCandidateDetails> result = studentdata.stream().filter(sd -> (sd.getIsRejected() == false )).filter(sd -> sd.getIsTempAllocated() == false).collect(Collectors.toList());
//		/*
//		 * for(RalleyCandidateDetails s: studentdata) {
//		 * System.out.println(s.toString()); }
//		 */
		// System.out.println(m.getAttribute("totalItems"));
		 model.addAttribute("cityid", cityid);
		 model.addAttribute("slotdetails", slotdetails);
		 model.addAttribute("intake", intake);
		 model.addAttribute("minpercentage",passPercentage);
		 model.addAttribute("studentdata", result);
		 return "TempRegisteredStudentDataForAllocation";
	 }
	 
	 
	 @RequestMapping(value="/getSlotData", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public  ResponseEntity<List<Long>> getSlotData(@RequestBody Map<String, Long>  ids) {
			
			
			
		 RallySlotMaster details= rdservice.getSlotdata(ids.get("cid"),ids.get("slotid"));
		 Long allocatedSlotCount=rdservice.getAlreadyAllocatedSlotCount(ids.get("slotid"));
			//List<RalleyCities> entityList=Collections.EMPTY_LIST;
		 List<Long> data=new ArrayList<Long>();
		 data.add(details.getRallyCount());
		 data.add(allocatedSlotCount);
		 
		    
		   
		    return new ResponseEntity<List<Long>>(data, HttpStatus.OK);
		}
	 
	 
	 @RequestMapping(value="/updateDuplicate", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public  ResponseEntity<String> updateDuplicate(@RequestBody Map<String, List<Long>>  ids) {
			
			ids.get("ids").stream().forEach(System.out::println);
			//insert in tempallocation and update in candidate details
			rdservice.addDuplicateEntry(ids.get("ids"));
		   String entityList = "Updated";
			//List<RalleyCities> entityList=Collections.EMPTY_LIST;
		    
		   
		    return new ResponseEntity<String>(entityList, HttpStatus.OK);
		}
	 
	 @RequestMapping(value="/rejected", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public  ResponseEntity<String> updateRejected(@RequestBody Map<String, List<Long>>  ids) {
			
			ids.get("ids").stream().forEach(System.out::println);
			rdservice.addOnlyRejectEntry(ids.get("ids"));
		   String entityList = "rejected";
			//List<RalleyCities> entityList=Collections.EMPTY_LIST;
		    
		   
		    return new ResponseEntity<String>(entityList, HttpStatus.OK);
		}
	 
	 
	 @RequestMapping(value="/tempAllocation", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public  ResponseEntity<String> tempAllocation(@RequestBody TempAllocationDto object) {
			System.out.println(object.getCityid()+":"+object.getSlotid());
			//log size of allocation list
			logger.info("For Slot id:"+object.getSlotid()+"and cityid:"+object.getCityid()+" Total Allocation count:"+object.getIds().size());
			//save data of allocation id in temp allocation table with candidate details updation
		 rdservice.allocateTempData(object.getCityid(),object.getSlotid(),object.getIds());
		//send response to reload the page
		   String entityList = "temp allocation successful";
			
		    
		   
		    return new ResponseEntity<String>(entityList, HttpStatus.OK);
		}
	 
	 @GetMapping("/ShowAdmitCardCreationPage")
	 public ModelAndView ShowAdmitCardCreationPage()
	 {
		 ModelAndView m= new ModelAndView("AdmitCard");
		 //get distinct rally id details from slot 
		 //m.addObject("studentdata", rd);
		 return m;
	 }
	 
	 @GetMapping("/Allocation")
	 public ModelAndView Allocation()
	 {
		 ModelAndView m= new ModelAndView("Allocation");
		 //get distinct rally id details from slot 
		 m.addObject("ralleyDetails", rdservice.getAllRalleyDetails());
		 //m.addObject("studentdata", rd);
		 return m;
	 }
	 
	 @RequestMapping(value="/getDifferenceCount/{percent}/{cityid}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
	 public ResponseEntity<String> getDifferenceCount(@PathVariable("percent") Long value,@PathVariable("cityid") Long cityid,Model m)
	 {
		
		Long difference=rdservice.getDifferenceCount(value,cityid); 
		logger.info("count:"+difference);
		 
		 return new ResponseEntity<String>(String.valueOf(difference),HttpStatus.OK);
	 }
	 
	 @GetMapping("/getDuplicateList/{value}/{cityid}")
	 public String getDuplicateList(@PathVariable("value") String value,@PathVariable("cityid") Long cityid,Model m)
	 {
		 
		 //page it with filter
		List<RalleyCandidateDetails> c=candidateService.getDuplicationList(value,cityid);
		System.out.println(c.size());
		 m.addAttribute("studentdata",c);
		 m.addAttribute("checkedvalues", value);
		 
		 return "RegisteredStudentDataForAllocation";
	 }
	 
	 
	 
	 @GetMapping("/showDuplicateData")
	 public ModelAndView showDuplicateData()
	 {
		 ModelAndView m= new ModelAndView("ShowDuplicateStudentData");
		 //get distinct rally id details from slot 
		 //m.addObject("studentdata", rd);
		 
		 return m;
	 }
	 
	 
	 
	 
	 
	 
	 @GetMapping("/DuplicateData/{cityid}")
	 public String getDuplicateData(Model model,@PathVariable (value = "cityid") Long cityid)
	 {
		 List<RalleyCandidateDetails> result = rdservice.getDuplicateCandidateDetails(cityid);
		 model.addAttribute("cityid", cityid);
		
		 model.addAttribute("studentdata", result);
		 return "ShowDuplicateStudentData";
	 }
	 
	 @GetMapping("/AllocateCityData/{cityid}")
	 public String AllocateCityData(Model model,@PathVariable (value = "cityid") Long cityid)
	 {
		 List<RalleyCandidateDetails> result = rdservice.getAllocateCandidateDetails(cityid);
		 List<RallySlotMaster> slotdetails =rdservice.getSlotOnBasisOfId(cityid);
		 model.addAttribute("cityid", cityid);
		model.addAttribute("slotdetails", slotdetails);
		 model.addAttribute("studentdata", result);
		 return "AllocatedStudentData";
	 }
	 
	 @GetMapping("/AllocateCityDataWithSlot/{cityid}/{slotid}")
	 public String AllocateCityDataWithSlot(Model model,@PathVariable (value = "cityid") Long cityid,@PathVariable (value = "slotid") Long slotid)
	 {
		 List<RalleyCandidateDetails> result = rdservice.getAllocateCandidateDetailsWithSlot(cityid,slotid);
		 List<RallySlotMaster> slotdetails =rdservice.getSlotOnBasisOfId(cityid);
		 model.addAttribute("cityid", cityid);
		 model.addAttribute("slotid",slotid);
		model.addAttribute("slotdetails", slotdetails);
		 model.addAttribute("studentdata", result);
		 return "AllocatedStudentData";
	 }
	 
	 
	 @GetMapping("/showAlreadyAllocatedData")
	 public ModelAndView showAlreadyAllocatedData()
	 {
		 ModelAndView m= new ModelAndView("AllocatedStudentData");
		 //get distinct rally id details from slot 
		 //m.addObject("studentdata", rd);
		 
		 return m;
	 }
	 
	 
	 
	 @RequestMapping(value="/edit/getCitiesonbasisofStateSeclected", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public  ResponseEntity<List<RalleyCities>> getcitiesAll(@RequestBody Map<String, Long>  stateid) {
			
			System.out.println("in getcities"+stateid.get("stateid"));
		   List<RalleyCities> entityList = candidateService.getallCitesByState(stateid.get("stateid"));
			//List<RalleyCities> entityList=Collections.EMPTY_LIST;
		    
		   
		    return new ResponseEntity<List<RalleyCities>>(entityList, HttpStatus.OK);
		}
	 
	 @RequestMapping(value="/getCitiesonbasisofStateSeclected", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public  ResponseEntity<List<RalleyCities>> getCitiesonbasisofStateSeclected(@RequestBody Map<String, Long>  stateid) {
			
			System.out.println("in getcities"+stateid.get("stateid"));
		   List<RalleyCities> entityList = candidateService.getallCitesByState(stateid.get("stateid"));
			//List<RalleyCities> entityList=Collections.EMPTY_LIST;
		    
		   
		    return new ResponseEntity<List<RalleyCities>>(entityList, HttpStatus.OK);
		}
		
		
		
	 
	 @RequestMapping(value="/getralleyallotCities", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public  ResponseEntity<List<RalleyDetails>> getralleyallotCities() {
			List<RalleyDetails> entityList= rdservice.findDistinctCity_id();
			
			
			//System.out.println("in getcities"+stateid.get("stateid"));
		 //  List<RalleyCities> entityList = candidateService.getallCitesByState(stateid.get("stateid"));
			//List<RalleyCities> entityList=Collections.EMPTY_LIST;
		    
		   
		    return new ResponseEntity<List<RalleyDetails>>(entityList, HttpStatus.OK);
		}
	 
	
	 
	 @RequestMapping(value="/getralleyallotCitiesSlotDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public  ResponseEntity<List<RallySlotMaster>>  getralleyallotCitiesSlotDetails(@RequestBody Map<String, Long>  rallyid) {
		 
			List<RallySlotMaster> entityList= rdservice.findSlotOnBasisOfRallyId(rallyid.get("rallyid"));
			
			
			
		    
		   
		    return new ResponseEntity<List<RallySlotMaster>>(entityList, HttpStatus.OK);
		}
	 
	 
	 @RequestMapping(value="/genrateAdmitCard", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public  ResponseEntity<String>  genrateAdmitCard(@RequestBody Map<String, Long>  value) {
		 
		 rdservice.findDataInAllocationMapping(value.get("rallyid"),value.get("slotid"));
			
			
			
		    
		   
		    return new ResponseEntity<String>("Compeleted", HttpStatus.OK);
		}
	 
	 
	 @RequestMapping(value="/sendEmail", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public  ResponseEntity<String>  sendEmail(@RequestBody Map<String, Long>  value) {
		 
		 rdservice.findDataForEmailAllocationMapping(value.get("rallyid"),value.get("slotid"));
			
			
			
		    
		   
		    return new ResponseEntity<String>("Compeleted", HttpStatus.OK);
		}
	 
	 
	 
	
	 
	 
	 
	 public static String convertDateToString(Date strDate) {
		    try {
		      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		       // you can change format of date
		      String date = formatter.format(strDate);
		      //Timestamp timeStampDate = new Timestamp(date.getTime());
		    //  logger.info("in string"+date);
		      return date;
		    } catch (Exception e) {
		      System.out.println("Exception :" + e);
		      return null;
		    }
		  }

}
