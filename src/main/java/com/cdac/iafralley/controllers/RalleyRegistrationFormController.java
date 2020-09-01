package com.cdac.iafralley.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.entity.RalleyCities;
import com.cdac.iafralley.entity.RalleyDetails;
import com.cdac.iafralley.entity.RalleyGroup_trade;
import com.cdac.iafralley.entity.RalleyStates;
import com.cdac.iafralley.exception.AadharInvalid;
import com.cdac.iafralley.exception.CandidateInvalidInputAsPerCrietria;
import com.cdac.iafralley.mailConfig.ContentIdGenerator;
import com.cdac.iafralley.mailConfig.MailingService;
import com.cdac.iafralley.services.RalleyCandidateDetailsService;
import com.cdac.iafralley.services.RalleyDetailsService;
import com.cdac.iafralley.util.RegisterdCandidatePDFReport;
import com.cdac.iafralley.util.VerhoeffAlgorithm;



/**
 * 
 * @author Ketan Tank
 *
 */
@Controller
@PropertySource({"classpath:mailserver.properties"})
@PropertySource({"classpath:applicantfilepath.properties"})
public class RalleyRegistrationFormController {
	
	@Autowired
	RalleyCandidateDetailsService candidateService;
	@Autowired
	RalleyDetailsService ralleyService;
	
	
	
	
	@Value("${config.mailserver}")
    private String mailserver;
 
    @Value("${config.from}")
    private String from;
 
    @Value("${config.password}")
    private String password;
	
    @Value("${applicant.filepath}")
	private String FILE_PATH;
    
    final long FIFTY_KB_IN_BYTES = 51200;
	final long THIRTY_KB_IN_BYTES = 30720;

	private static final Logger logger = LoggerFactory.getLogger(RalleyRegistrationFormController.class);
	
	
	/**
	 * Dob binder in yyyy-MM-dd format for ralleycandidatedetails modal
	 * @param binder
	 */
	/*
	 * @InitBinder("ralleycandidatedetails") public void customizeBinding
	 * (WebDataBinder binder) { SimpleDateFormat dateFormatter = new
	 * SimpleDateFormat("yyyy-MM-dd"); dateFormatter.setLenient(false);
	 * binder.registerCustomEditor(Date.class, "dateOfBirth", new
	 * CustomDateEditor(dateFormatter, true)); }
	 */
	
	/**
	 * String trimmer for all fields in ralleycandidatedetails modal
	 * 
	 * @param dataBinder
	 */
	
	  @InitBinder public void initBinder(WebDataBinder dataBinder) {
	  
	  StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
	  
	  dataBinder.registerCustomEditor(String.class, stringTrimmerEditor); }
	 

	@GetMapping(value = "/showRegistrationForm")
	public ModelAndView createUserView(Model model) {
		
		
		
		
		ModelAndView modelAndView = new ModelAndView("CandidateRegistration");
		 if (!model.containsAttribute("ralleyCandidateDetails")) {
	        	modelAndView.addObject("ralleyCandidateDetails", new RalleyCandidateDetails());
	        	//modelAndView.addObject("allStates", candidateService.getallState());
	        	modelAndView.addObject("allStates", new RalleyStates());
	        	modelAndView.addObject("ralleyAllState", candidateService.getralleyAllState());
	        }
		
		 
		return modelAndView;
	}
	
	
	@GetMapping(value = "/RegistrationSuccess")
	public String showRegisterdCandidateDetails(Model model) {
		
		if(!model.containsAttribute("candidateDetails"))
		{
			return "redirect:/showRegistrationForm";
		}
		
		
		
		return "RegistrationSuccess";

	}
	 
	@PostMapping("/registerCandidate")
	public ModelAndView createUser(@ModelAttribute("ralleyCandidateDetails") @Valid RalleyCandidateDetails ralleyCandidateDetails, BindingResult result,@RequestParam("XMarksheet") MultipartFile  XMarksheet,@RequestParam("XIIMarksheet") MultipartFile  XIIMarksheet,RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
		
		
		try {
			logger.info(ralleyCandidateDetails.toString());
			logger.info(ralleyCandidateDetails.getState()+""+ralleyCandidateDetails.getCity());
			RalleyDetails rd =ralleyService.findByCustomId(ralleyCandidateDetails.getRally_id());
			if(rd == null)
			{
				throw new CandidateInvalidInputAsPerCrietria("Invalid input...!");
			}
			
			if(ralleyCandidateDetails.getAadhar_details() !=null)
			{
				if(VerhoeffAlgorithm.validateAadharNumber(ralleyCandidateDetails.getAadhar_details()))
				{}
				else {
					throw new AadharInvalid("Aadhar number is invalid");
				}
			}
			
			if(XMarksheet.isEmpty() || XIIMarksheet.isEmpty())
			{
				throw new CandidateInvalidInputAsPerCrietria("Uploaded Certificate or Markheet is not Valid");
			}
			else if(XMarksheet.getSize() < THIRTY_KB_IN_BYTES || XMarksheet.getSize() > FIFTY_KB_IN_BYTES)
			{
				throw new CandidateInvalidInputAsPerCrietria("Uploaded 10th Certificate size should in between be 30kb to 50kb ");
			}
			else if(XIIMarksheet.getSize() < THIRTY_KB_IN_BYTES || XIIMarksheet.getSize() > FIFTY_KB_IN_BYTES)
			{
				throw new CandidateInvalidInputAsPerCrietria("Uploaded 12th/Voctional/Diploma Markseet size should in between be 30kb to 50kb ");
			}
			
			
			//check Dob range within
			logger.info("candidate dob "+ralleyCandidateDetails.getDateOfBirth() +"admin dob "+rd.getMin_dob() +" "+ (ralleyCandidateDetails.getDateOfBirth().compareTo(rd.getMin_dob()) >= 0) +""
					+(ralleyCandidateDetails.getDateOfBirth().compareTo(rd.getMax_dob()) <= 0)+"exma:"+ralleyCandidateDetails.getPassed_exam_percentage());
			
			if(!(ralleyCandidateDetails.getDateOfBirth().compareTo(rd.getMin_dob()) >= 0 && ralleyCandidateDetails.getDateOfBirth().compareTo(rd.getMax_dob()) <= 0)){
				throw new CandidateInvalidInputAsPerCrietria("Dob birth are Invalid as per Eligibilty Criteria");
			}
			if(!(ralleyCandidateDetails.getPassed_exam_percentage() < 0 || (ralleyCandidateDetails.getPassed_exam_percentage() >= rd.getMin_passing_percentage()   && ralleyCandidateDetails.getPassed_exam_percentage() <= 100)))
			{
				throw new CandidateInvalidInputAsPerCrietria("Given Aggregate percentage are Invalid as per Eligibilty Criteria");
			}
			if(!(ralleyCandidateDetails.getEnglish_percentage() < 0 || (ralleyCandidateDetails.getEnglish_percentage() >= rd.getMin_eng_percentage()   && ralleyCandidateDetails.getEnglish_percentage() <= 100)))
			{
				throw new CandidateInvalidInputAsPerCrietria("Given English percentage are Invalid as per Eligibilty Criteria");
			}
			
		}catch(Exception e)
		{
			 modelAndView.setViewName("redirect:/showRegistrationForm");
			  redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ralleyCandidateDetails", result);
			  redirectAttributes.addFlashAttribute("ralleyCandidateDetails", ralleyCandidateDetails);
			  redirectAttributes.addFlashAttribute("error", e.getMessage()); 
			  
			  redirectAttributes.addFlashAttribute("optcity",ralleyCandidateDetails.getOpt_city());
			
			  redirectAttributes.addFlashAttribute("allStates", new RalleyStates());
			  redirectAttributes.addFlashAttribute("ralleyAllState", candidateService.getralleyAllState());
			  return modelAndView;
		}
		
		logger.info("error:"+result.hasErrors());
		if (result.hasErrors()) {
			
			  logger.info("Validation errors while submitting form."); result
			  .getFieldErrors() .stream() .forEach(f -> System.out.println(f.getField() +
			  ": " + f.getDefaultMessage()));
			
			  modelAndView.setViewName("redirect:/showRegistrationForm");
			  redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ralleyCandidateDetails", result);
			  redirectAttributes.addFlashAttribute("ralleyCandidateDetails", ralleyCandidateDetails);
			  redirectAttributes.addFlashAttribute("error", "error");
			  
			 // redirectAttributes.addFlashAttribute("optcity",ralleyCandidateDetails.getOpt_city());
			 // modelAndView.addObject("ralleyCandidateDetails", ralleyCandidateDetails);
			 // modelAndView.addObject("allStates", candidateService.getallState());
			 // redirectAttributes.addFlashAttribute("allStates", candidateService.getallState());
			  redirectAttributes.addFlashAttribute("allStates", new RalleyStates());
			  redirectAttributes.addFlashAttribute("ralleyAllState", candidateService.getralleyAllState());
			 
			 
			return modelAndView;
		}
		
		
		
		
		
		  try {
			RalleyCandidateDetails candidateDetails = candidateService.save(ralleyCandidateDetails, XMarksheet,XIIMarksheet);
			//PDF and mail genration code
			// modelAndView.addObject("allUsers", userService.getAllUsers());
		  //modelAndView.addObject("candidateDetails",candidateDetails );
			
		  //RegisterdCandidatePDFReport.createPDF(candidateDetails,FILE_PATH);
			logger.info(""+candidateDetails.getRally_id());
			RalleyDetails saverd =ralleyService.findByCustomId(candidateDetails.getRally_id());
			logger.info(""+saverd.getCity_name()+" "+saverd.getRalley_details());
			  String to=candidateDetails.getEmailid(); 
			  String subject="IAF Rally Recruitment Conformation"; 
			  String message="";
			      
			  
			  
			  try {
			  
			  MailingService.sendMail(mailserver, from, password, candidateDetails,
			  subject, message,FILE_PATH,saverd); } catch(Exception e) { e.printStackTrace();
			  
			  }
			 
		
		 redirectAttributes.addFlashAttribute("candidateDetails", candidateDetails);
		 redirectAttributes.addFlashAttribute("ralleydetails",saverd.getRalley_details()+"-"+saverd.getCity_name().toUpperCase());
		  modelAndView.setViewName("redirect:/RegistrationSuccess");
		  logger.info("Form submitted successfully.");
		  
		  } catch (Exception e) { // TODO Auto-generated catch block
		  
			  modelAndView.setViewName("redirect:/showRegistrationForm");
			  redirectAttributes.addFlashAttribute("ralleyCandidateDetails", new RalleyCandidateDetails());
		 // redirectAttributes.addFlashAttribute("allStates", candidateService.getallState());
			  redirectAttributes.addFlashAttribute("allStates", new RalleyStates());
		  redirectAttributes.addFlashAttribute("ralleyAllState", candidateService.getralleyAllState());
		  redirectAttributes.addFlashAttribute("msgsavingerror",e.getMessage());
		  
		  
		  }
		 
		return modelAndView;
		
	}


	@RequestMapping(value="/getCitiesonbasisofStateSeclected", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public  ResponseEntity<List<RalleyCities>> getcitiesAll(@RequestBody Map<String, Long>  stateid) {
		
		System.out.println("in getcities"+stateid.get("stateid"));
	   List<RalleyCities> entityList = candidateService.getallCitesByState(stateid.get("stateid"));
		//List<RalleyCities> entityList=Collections.EMPTY_LIST;
	    
	   
	    return new ResponseEntity<List<RalleyCities>>(entityList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getralleyallotCitiesonbasisofStateSeclected", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public  ResponseEntity<List<RalleyCities>> getRalleyAllotcitiesByAdmin(@RequestBody Map<String, Long>  stateid) {
		
		System.out.println("in getcities"+stateid.get("stateid"));
	   List<RalleyCities> entityList = candidateService.getallCitesByStateAdminAlloted(stateid.get("stateid"));
	   
	   
		//List<RalleyCities> entityList=Collections.EMPTY_LIST;
	    
	   
	    return new ResponseEntity<List<RalleyCities>>(entityList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getralleyStateandCities", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public  ResponseEntity<Map<String,List<Object>>> getralleyStateandCities(@RequestBody Map<String, String>  rallyid) {
		
		System.out.println("in getid:"+rallyid.get("rallyid"));
		Map<String,List<Object>> entityList = candidateService.getralleyStateandCities(rallyid.get("rallyid"));
	   
		//List<RalleyCities> entityList=Collections.EMPTY_LIST;
	    
	   
	    return new ResponseEntity<Map<String,List<Object>>>(entityList, HttpStatus.OK);
	}

	@RequestMapping(value="/getralleyFormOnBasisOfAdminCities", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public  ResponseEntity<Boolean> getralleyFormOnBasisOfAdminCities(@RequestBody Map<String, Long>  cityid) {
		
		System.out.println("in getcities"+cityid.get("cityid"));
	   Boolean entityList=candidateService.getregisteredCount(cityid.get("cityid"));
	  // Boolean entityList=false;
		//List<RalleyCities> entityList=Collections.EMPTY_LIST;
	    
	   
	    return new ResponseEntity<Boolean>(entityList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getralleyDetailsOnBasisOfAdminCities", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public  ResponseEntity<String> getralleyDetailsOnBasisOfAdminCities(@RequestBody Map<String, Long>  cityid) {
		
		System.out.println("in getcities"+cityid.get("cityid"));
	   String entityList=candidateService.showOptRalleyDetailstoCandidate(cityid.get("cityid"));
	  // Boolean entityList=false;
		//List<RalleyCities> entityList=Collections.EMPTY_LIST;
	    
	   
	    return new ResponseEntity<String>(entityList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getralleyValidationDetailsOnBasisOfAdminCities", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public  ResponseEntity<Map<String, String>> getralleyValidationDetailsOnBasisOfAdminCities(@RequestBody Map<String, Long>  cityid) {
		
		System.out.println("in getcities"+cityid.get("cityid"));
	   Map<String, String> entityList=candidateService.showOptValidRalleyDetailstoCandidate(cityid.get("cityid"));
	  // Boolean entityList=false;
		//List<RalleyCities> entityList=Collections.EMPTY_LIST;
	    
	   
	    return new ResponseEntity<Map<String, String>>(entityList, HttpStatus.OK);
	}

	@RequestMapping(value="/getralleyCreatedGroups", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public  ResponseEntity<List<RalleyGroup_trade>> getralleyCreatedGroups(@RequestBody Map<String, Long>  cityid) {
		
		System.out.println("in getcities"+cityid.get("cityid"));
	   List<RalleyGroup_trade> entityList=candidateService.getralleyCreatedGroups(cityid.get("cityid"));
	  // Boolean entityList=false;
		//List<RalleyCities> entityList=Collections.EMPTY_LIST;
	    
	   
	    return new ResponseEntity<List<RalleyGroup_trade>>(entityList, HttpStatus.OK);
	}

	

}
