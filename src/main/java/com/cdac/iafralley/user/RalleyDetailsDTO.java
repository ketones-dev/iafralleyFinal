package com.cdac.iafralley.user;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.cdac.iafralley.entity.RalleyDaywiseSlotDetails;

public class RalleyDetailsDTO {
	
	
	private Long ralley_id;
	
	private Long slot_id;
	
	
	private Long state_id;
	
	
	private Long city_id;
	
	private String state_name;
	private String city_name;
	
	@NotNull
	private String ralley_details;
	
	@NotNull
	private String venue_details;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date start_date;
	
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date end_date;
	
	
	
	private int no_OfDays;
	
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date min_dob;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date max_dob;
	
	@NotNull
	private Long min_passing_percentage;
	
	@NotNull
	private Long min_eng_percentage;
	
	@NotNull
	private Long min_height;
	
	@NotNull
	private List<String> ralleyForGroup;
	
	private List<RalleyDaywiseSlotDetails> ralleydaywiseSlot;
	
	
	private Long candidateRestrictFromStateId;
	
	private List<String> candidateRestrictFromDistrictIds;

	

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

	public Long getRalley_id() {
		return ralley_id;
	}

	public void setRalley_id(Long ralley_id) {
		this.ralley_id = ralley_id;
	}

	public Long getSlot_id() {
		return slot_id;
	}

	public void setSlot_id(Long slot_id) {
		this.slot_id = slot_id;
	}

	public List<String> getRalleyForGroup() {
		return ralleyForGroup;
	}

	public void setRalleyForGroup(List<String> ralleyForGroup) {
		this.ralleyForGroup = ralleyForGroup;
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
	
	

}
