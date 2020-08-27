package com.cdac.iafralley.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.cdac.iafralley.util.StringListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="ralley_details")
public class RalleyDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ralley_id")
	private Long ralley_id;
	
	@NotNull
	@Column(name="state_id")
	private Long state_id;
	
	@NotNull
	@Column(name="city_id")
	private Long city_id;
	
	@NotEmpty
	@Column(name="state_name")
	private String state_name;
	
	@NotEmpty
	@Column(name="city_name")
	private String city_name;
	
	@Column(name="ralleyNo")
	private String ralley_cust_id;
	
	@NotNull
	@Column(name="ralley_details")
	private String ralley_details;
	
	@NotNull
	@Column(name="venue_details")
	private String venue_details;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="start_date")
	private Date start_date;
	
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="end_date")
	private Date end_date;
	
	
	@NotNull
	@Column(name="no_ofdays")
	private int no_OfDays;
	
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="min_dob")
	private Date min_dob;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="max_dob")
	private Date max_dob;
	
	@NotNull
	@Column(name="min_passing_percentage")
	private Long min_passing_percentage;
	
	@NotNull
	@Column(name="min_eng_percentage")
	private Long min_eng_percentage;
	
	@NotNull
	@Column(name="min_height")
	private Long min_height;
	
	@Convert(converter = StringListConverter.class)
	@Column(name="ralley_for_Groups")
	private List<String> ralleyForGroup;
	
	@Column(name="candidateRestrictFromStateId")
	private Long candidateRestrictFromStateId;
	
	@Convert(converter = StringListConverter.class)
	@Column(name="candidateRestrictFrom_district_ids")
	private List<String> candidateRestrictFromDistrictIds;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "ralleydetails",cascade = CascadeType.ALL)
	private List<RalleyDaywiseSlotDetails> ralleydaywiseSlot;
	
	
	public RalleyDetails() {
		// TODO Auto-generated constructor stub
	}



	public RalleyDetails(Long ralley_id,Long state_id, Long city_id, String ralley_details, String venue_details, Date start_date,
			Date end_date, int no_OfDays, Date min_dob, Date max_dob, Long min_passing_percentage,
			Long min_eng_percentage, Long min_height,String city_name,String state_name,String ralley_cust_id,Long candidateRestrictFromStateId) {
		super();
		this.ralley_id=ralley_id;
		this.state_id = state_id;
		this.city_id = city_id;
		this.ralley_details = ralley_details;
		this.venue_details = venue_details;
		this.start_date = start_date;
		this.end_date = end_date;
		this.no_OfDays = no_OfDays;
		this.min_dob = min_dob;
		this.max_dob = max_dob;
		this.min_passing_percentage = min_passing_percentage;
		this.min_eng_percentage = min_eng_percentage;
		this.min_height = min_height;
		this.city_name=city_name;
		this.state_name=state_name;
		this.ralley_cust_id=ralley_cust_id;
		this.candidateRestrictFromStateId =candidateRestrictFromStateId;
	}



	public Long getRalley_id() {
		return ralley_id;
	}



	public void setRalley_id(Long ralley_id) {
		this.ralley_id = ralley_id;
	}



	public Long getState_id() {
		return state_id;
	}



	public void setState_id(Long state_id) {
		this.state_id = state_id;
	}



	public Long getCity_id() {
		return city_id;
	}



	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}



	public String getRalley_details() {
		return ralley_details;
	}



	public void setRalley_details(String ralley_details) {
		this.ralley_details = ralley_details;
	}



	public String getVenue_details() {
		return venue_details;
	}



	public void setVenue_details(String venue_details) {
		this.venue_details = venue_details;
	}



	public Date getStart_date() {
		return start_date;
	}



	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}



	public Date getEnd_date() {
		return end_date;
	}



	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}



	public int getNo_OfDays() {
		return no_OfDays;
	}



	public void setNo_OfDays(int no_OfDays) {
		this.no_OfDays = no_OfDays;
	}



	public Date getMin_dob() {
		return min_dob;
	}



	public void setMin_dob(Date min_dob) {
		this.min_dob = min_dob;
	}



	public Date getMax_dob() {
		return max_dob;
	}



	public void setMax_dob(Date max_dob) {
		this.max_dob = max_dob;
	}



	public Long getMin_passing_percentage() {
		return min_passing_percentage;
	}



	public void setMin_passing_percentage(Long min_passing_percentage) {
		this.min_passing_percentage = min_passing_percentage;
	}



	public Long getMin_eng_percentage() {
		return min_eng_percentage;
	}



	public void setMin_eng_percentage(Long min_eng_percentage) {
		this.min_eng_percentage = min_eng_percentage;
	}



	public Long getMin_height() {
		return min_height;
	}



	public void setMin_height(Long min_height) {
		this.min_height = min_height;
	}

  

	public List<RalleyDaywiseSlotDetails> getRalleydaywiseSlot() {
		return ralleydaywiseSlot;
	}



	public void setRalleydaywiseSlot(List<RalleyDaywiseSlotDetails> ralleydaywiseSlot) {
		this.ralleydaywiseSlot = ralleydaywiseSlot;
	}

	


	public String getState_name() {
		return state_name;
	}



	public void setState_name(String state_name) {
		this.state_name = state_name;
	}



	public String getCity_name() {
		return city_name;
	}



	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}


	
	
	
	
	
	
	public List<String> getRalleyForGroup() {
		return ralleyForGroup;
	}



	public void setRalleyForGroup(List<String> ralleyForGroup) {
		this.ralleyForGroup = ralleyForGroup;
	}



	public String getRalley_cust_id() {
		return ralley_cust_id;
	}



	public void setRalley_cust_id(String ralley_cust_id) {
		this.ralley_cust_id = ralley_cust_id;
	}


	

	public Long getCandidateRestrictFromStateId() {
		return candidateRestrictFromStateId;
	}



	public void setCandidateRestrictFromStateId(Long candidateRestrictFromStateId) {
		this.candidateRestrictFromStateId = candidateRestrictFromStateId;
	}


	

	public List<String> getCandidateRestrictFromDistrictIds() {
		return candidateRestrictFromDistrictIds;
	}



	public void setCandidateRestrictFromDistrictIds(List<String> candidateRestrictFromDistrictIds) {
		this.candidateRestrictFromDistrictIds = candidateRestrictFromDistrictIds;
	}



	@Override
	public String toString() {
		return "RalleyDetails [ralley_id=" + ralley_id + ", state_id=" + state_id + ", city_id=" + city_id
				+ ", state_name=" + state_name + ", city_name=" + city_name + ", ralley_details=" + ralley_details
				+ ", venue_details=" + venue_details + ", start_date=" + start_date + ", end_date=" + end_date
				+ ", no_OfDays=" + no_OfDays + ", min_dob=" + min_dob + ", max_dob=" + max_dob
				+ ", min_passing_percentage=" + min_passing_percentage + ", min_eng_percentage=" + min_eng_percentage
				+ ", min_height=" + min_height + ", ralley_cust_id="+ ralley_cust_id +",ralleydaywiseSlot=" + ralleydaywiseSlot + "]";
	}



	//conventional method for bidirectional mapping
	public void add(RalleyDaywiseSlotDetails rds) {
		
		if(ralleydaywiseSlot == null)
		{
			ralleydaywiseSlot=new ArrayList<>();
		}
		
		ralleydaywiseSlot.add(rds);
		
		rds.setRalleydetails(this);
	}
	
	
	
	
	

}
