package com.cdac.iafralley.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.cdac.iafralley.user.validation.ValidEmail;


@Entity
@Table(name="candidate_details")
public class RalleyCandidateDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "applicant_id")
	private Long id;
	
	@NotBlank(message="is required")
	@Size(max = 50, min = 3, message = "Candidate Name must be  3 to 50 character long ")
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z ]+[a-zA-Z]$",message="Please provide valid Name values")
	@Column(name="name")
	private String name;
	
	@NotBlank(message="is required")
	@Size(max = 50, min = 3, message = "Candidate Name must be  3 to 50 character long ")
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z ]+[a-zA-Z]$",message="Please provide valid Father Name values")
	@Column(name="father_name")
	private String fathername;
	
	@ValidEmail
	@Column(name="emailid")
	private String emailid;
	
	//Mapping Date and Timestamp using java.util.Date
	@NotNull
	@Column(name = "DOB")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private java.util.Date dateOfBirth;
	
	@NotEmpty
	
	@Pattern(regexp="^[6-9]\\d{9}$",message ="Please provide valid mobile number")
	@Column(name="mobile")
	private String contactno;
	
	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z0-9]+$",message="invalid input data")
	@Column(name="passed_exam_detail")
	private String passed_exam_detail;
	
	@NotNull(message="is required")
	@Pattern(regexp="^[^\\s]+[-a-zA-Z\\s]+([-a-zA-Z]+)*$",message="invalid Degree Details only Characters allowed")
	@Column(name="degree_detail")
	private String otherDetailPassedDetail;
	
	
	
	@Column(name="aadhar_details")
	@Pattern(regexp="^$|^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}$",message="Please provide valid aadhar Number")
	private String aadhar_details;
	
	
	@NotNull
	@Min(value = 35,message="value must be positive value")
	@Column(name="passed_exam_percentage")
	private Integer passed_exam_percentage;
	
	@NotNull
	@Min(value = 35,message="value must be positive value")
	@Column(name="english_percentage")
	private Integer english_percentage;
	
	@Column(name="maritial_status")
	private boolean maritial_status=false;
	
	@NotEmpty
	@Pattern(regexp="^[1-4][0-9][0-9](?:\\.[0-9]{1,1})?$",message="Height input was invalid")
	@Column(name="height")
	private String height;
	
	@NotEmpty
	@Column(name="domicile_city")
	private String city;
	
	@NotEmpty
	@Column(name="domicile_state")
	private String state;
	
	@NotNull
	@Min(value=0 ,message="value should be positive number")
	@Column(name="opt_city")
	private Long opt_city;
	
	@NotNull
	@Min(value=0 ,message="value should be positive number")
	@Column(name="opt_state")
	private Long opt_state;
	
	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z ]+[a-zA-Z]$",message="input value should be string with spaces only")
	@Column(name="opt_city_name")
	private String opt_city_name;
	
	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z ]+[a-zA-Z]$",message="input value should be string with spaces only")
	@Column(name="opt_state_name")
	private String opt_state_name;
	
	
	@Column(name="opt_group_id")
	@Min(value = 0L , message ="group no should be positive")
	private Long opt_group_id;
	
	@Column(name="allot_venu_details")
	private String venu_details;
	
	
	@Column(name="x_imagepath")
	private String ximagePath;
	
	
	@Column(name="xii_imagepath")
	private String xiiimagePath;
	
	@Column(name="candidate_photograph")
	private String candidate_photograph;
	
	@Column(name="opted_group_trade")
	private String optedGroupTrade;
	
	@Column(name="is_allocated")
	private Boolean isAllocated=false;
	
	@Column(name="is_admit_card_genrated")
	private Boolean isAdminCardGenrated=false;
	
	@Column(name="is_email_send")
	private Boolean isEmailSend=false;
	
	
	@OneToOne(fetch= FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name="rally_id")
	private RalleyDetails rallyDetails;
	
	
	@NotEmpty
	@Column(name="nationality")
	private String nationality;
	
	
	
	@Column(name = "Datetime_reporting")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date dateTimeOfReporting;
	
	@Column(name = "subscribed_on")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private java.util.Date subscirbed_on;
	
	@NotEmpty
	//@Pattern(regexp = "^[a-zA-Z0-9]$",message="Ralley Id cannot contain special characters")
	@Column(name="rally_custom_id")
	private String rally_id;
	
	@NotEmpty
	private String captcha;
	

//	 @Id
//	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registrationid_seq")
//	    @GenericGenerator(
//	        name = "registrationid_seq", 
//	        strategy = "com.cdac.iafralley.entity.StringPrefixedSequenceIdGenerator", 
//	        parameters = {
//	            @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "50"),
//	            @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "B_"),
//	            @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d") })
	 @Column(name="ralley_regid",unique = true)
	private String ralleyregistrationNo;
	 
	 public RalleyCandidateDetails() {
		// TODO Auto-generated constructor stub
	}	

	

	public RalleyCandidateDetails(@NotBlank(message = "is required") String name,
			@NotBlank(message = "is required") String fathername, String emailid, Date dateOfBirth,
			@NotEmpty String contactno, @NotEmpty String passed_exam_detail,
			@NotNull(message = "is required") String otherDetailPassedDetail, @NotNull Integer passed_exam_percentage,
			@NotNull Integer english_percentage, boolean maritial_status, @NotEmpty String height,
			@NotEmpty String city, @NotEmpty String state, @NotEmpty @NotEmpty Long opt_city, @NotEmpty Long opt_state,
			Date dateTimeOfReporting, Date subscirbed_on, String ralleyregistrationNo) {
		super();
		this.name = name;
		this.fathername = fathername;
		this.emailid = emailid;
		this.dateOfBirth = dateOfBirth;
		this.contactno = contactno;
		this.passed_exam_detail = passed_exam_detail;
		this.otherDetailPassedDetail = otherDetailPassedDetail;
		this.passed_exam_percentage = passed_exam_percentage;
		this.english_percentage = english_percentage;
		this.maritial_status = maritial_status;
		this.height = height;
		this.city = city;
		this.state = state;
		this.opt_city = opt_city;
		this.opt_state = opt_state;
		this.dateTimeOfReporting = dateTimeOfReporting;
		this.subscirbed_on = subscirbed_on;
		this.ralleyregistrationNo = ralleyregistrationNo;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFathername() {
		return fathername;
	}

	public void setFathername(String fathername) {
		this.fathername = fathername;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public java.util.Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(java.util.Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getContactno() {
		return contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	public String getPassed_exam_detail() {
		return passed_exam_detail;
	}

	public void setPassed_exam_detail(String passed_exam_detail) {
		this.passed_exam_detail = passed_exam_detail;
	}

	public Integer getPassed_exam_percentage() {
		return passed_exam_percentage;
	}

	public void setPassed_exam_percentage(Integer passed_exam_percentage) {
		this.passed_exam_percentage = passed_exam_percentage;
	}

	public Integer getEnglish_percentage() {
		return english_percentage;
	}

	public void setEnglish_percentage(Integer english_percentage) {
		this.english_percentage = english_percentage;
	}

	public boolean isMaritial_status() {
		return maritial_status;
	}

	public void setMaritial_status(boolean maritial_status) {
		this.maritial_status = maritial_status;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public java.util.Date getDateTimeOfReporting() {
		return dateTimeOfReporting;
	}

	public void setDateTimeOfReporting(java.util.Date dateTimeOfReporting) {
		this.dateTimeOfReporting = dateTimeOfReporting;
	}


	
	
	public String getRalleyregistrationNo() {
		return ralleyregistrationNo;
	}

	public void setRalleyregistrationNo(String ralleyregistrationNo) {
		this.ralleyregistrationNo = ralleyregistrationNo;
	}

	
	
	

	public java.util.Date getSubscirbed_on() {
		return subscirbed_on;
	}

	public void setSubscirbed_on(java.util.Date subscirbed_on) {
		this.subscirbed_on = subscirbed_on;
	}
	
	

	public String getOtherDetailPassedDetail() {
		return otherDetailPassedDetail;
	}

	public void setOtherDetailPassedDetail(String otherDetailPassedDetail) {
		this.otherDetailPassedDetail = otherDetailPassedDetail;
	}
	
	

	



	public Long getOpt_city() {
		return opt_city;
	}



	public void setOpt_city(Long opt_city) {
		this.opt_city = opt_city;
	}



	public Long getOpt_state() {
		return opt_state;
	}



	public void setOpt_state(Long opt_state) {
		this.opt_state = opt_state;
	}
	
	



	public String getAadhar_details() {
		return aadhar_details;
	}



	public void setAadhar_details(String aadhar_details) {
		this.aadhar_details = aadhar_details;
	}


	

	public String getOpt_city_name() {
		return opt_city_name;
	}



	public void setOpt_city_name(String opt_city_name) {
		this.opt_city_name = opt_city_name;
	}



	public String getOpt_state_name() {
		return opt_state_name;
	}



	public void setOpt_state_name(String opt_state_name) {
		this.opt_state_name = opt_state_name;
	}
	
	



	public String getVenu_details() {
		return venu_details;
	}



	public void setVenu_details(String venu_details) {
		this.venu_details = venu_details;
	}


	

	public String getXimagePath() {
		return ximagePath;
	}



	public void setXimagePath(String ximagePath) {
		this.ximagePath = ximagePath;
	}



	public String getXiiimagePath() {
		return xiiimagePath;
	}



	public void setXiiimagePath(String xiiimagePath) {
		this.xiiimagePath = xiiimagePath;
	}
	
	



	public String getOptedGroupTrade() {
		return optedGroupTrade;
	}



	public void setOptedGroupTrade(String optedGroupTrade) {
		this.optedGroupTrade = optedGroupTrade;
	}



	


	


	public Boolean getIsAllocated() {
		return isAllocated;
	}



	public void setIsAllocated(Boolean isAllocated) {
		this.isAllocated = isAllocated;
	}



	public Boolean getIsAdminCardGenrated() {
		return isAdminCardGenrated;
	}



	public void setIsAdminCardGenrated(Boolean isAdminCardGenrated) {
		this.isAdminCardGenrated = isAdminCardGenrated;
	}



	public Boolean getIsEmailSend() {
		return isEmailSend;
	}



	public void setIsEmailSend(Boolean isEmailSend) {
		this.isEmailSend = isEmailSend;
	}
	
	



	public Long getOpt_group_id() {
		return opt_group_id;
	}



	public void setOpt_group_id(Long opt_group_id) {
		this.opt_group_id = opt_group_id;
	}



	public String getRally_id() {
		return rally_id;
	}



	public void setRally_id(String rally_id) {
		this.rally_id = rally_id;
	}


	

	public String getCaptcha() {
		return captcha;
	}



	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}


	

	public String getCandidate_photograph() {
		return candidate_photograph;
	}



	public void setCandidate_photograph(String candidate_photograph) {
		this.candidate_photograph = candidate_photograph;
	}


	

	public RalleyDetails getRallyDetails() {
		return rallyDetails;
	}



	public void setRallyDetails(RalleyDetails rallyDetails) {
		this.rallyDetails = rallyDetails;
	}


	

	public String getNationality() {
		return nationality;
	}



	public void setNationality(String nationality) {
		this.nationality = nationality;
	}



	@Override
	public String toString() {
		return "RalleyCandidateDetails [id=" + id + ", name=" + name + ", fathername=" + fathername + ", emailid="
				+ emailid + ", dateOfBirth=" + dateOfBirth + ", contactno=" + contactno + ", passed_exam_detail="
				+ passed_exam_detail + ", otherDetailPassedDetail=" + otherDetailPassedDetail + ", aadhar_details="
				+ aadhar_details + ", passed_exam_percentage=" + passed_exam_percentage + ", english_percentage="
				+ english_percentage + ", maritial_status=" + maritial_status + ", height=" + height + ", city=" + city
				+ ", state=" + state + ", opt_city=" + opt_city + ", opt_state=" + opt_state + ", opt_city_name="
				+ opt_city_name + ", opt_state_name=" + opt_state_name + ",opt_group_id="+opt_group_id+", venu_details=" + venu_details
				+ ", ximagePath=" + ximagePath + ", xiiimagePath=" + xiiimagePath + ", dateTimeOfReporting="
				+ dateTimeOfReporting + ", subscirbed_on=" + subscirbed_on + ", ralleyregistrationNo="
				+ ralleyregistrationNo + "]";
	}







	



	
	
	
	



	


	
	

	

	
	
	
	
	

}
