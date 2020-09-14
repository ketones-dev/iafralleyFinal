package com.cdac.iafralley.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="rally_applicant_allocation_mapping")
public class RallyApplicantAllocation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="allocation_id")
	private Long allocation_id;
	
	@Column(name="rally_id")
	private Long rally_id;
	
	@Column(name="candidate_acknowledgement_no")
	private String candidate_acknowledgement_no;
	
	@Column(name="email")
	private String emailid;
	
	@Column(name="candidate_registration_no")
	private String candidate_registration_no;
	
	
	@Column(name="slot_id")
	private Long slot_id;
	
	
	public RallyApplicantAllocation() {
		// TODO Auto-generated constructor stub
	}


	

	public RallyApplicantAllocation(Long rally_id, String candidate_acknowledgement_no, String emailid,
			String candidate_registration_no, Long slot_id) {
		super();
		this.rally_id = rally_id;
		this.candidate_acknowledgement_no = candidate_acknowledgement_no;
		this.emailid = emailid;
		this.candidate_registration_no = candidate_registration_no;
		this.slot_id = slot_id;
	}




	public Long getAllocation_id() {
		return allocation_id;
	}


	public void setAllocation_id(Long allocation_id) {
		this.allocation_id = allocation_id;
	}


	public Long getRally_id() {
		return rally_id;
	}


	public void setRally_id(Long rally_id) {
		this.rally_id = rally_id;
	}


	public String getCandidate_acknowledgement_no() {
		return candidate_acknowledgement_no;
	}


	public void setCandidate_acknowledgement_no(String candidate_acknowledgement_no) {
		this.candidate_acknowledgement_no = candidate_acknowledgement_no;
	}


	public String getCandidate_registration_no() {
		return candidate_registration_no;
	}


	public void setCandidate_registration_no(String candidate_registration_no) {
		this.candidate_registration_no = candidate_registration_no;
	}


	public Long getSlot_id() {
		return slot_id;
	}


	public void setSlot_id(Long slot_id) {
		this.slot_id = slot_id;
	}

	

	public String getEmailid() {
		return emailid;
	}




	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}




	@Override
	public String toString() {
		return "RallyApplicantAllocation [allocation_id=" + allocation_id + ", rally_id=" + rally_id
				+ ", candidate_acknowledgement_no=" + candidate_acknowledgement_no + ", emailid=" + emailid
				+ ", candidate_registration_no=" + candidate_registration_no + ", slot_id=" + slot_id + "]";
	}
	
	
	

}
