package com.cdac.iafralley.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="temp_rally_allocation")
public class TempRallyApplicantAllocation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="temp_allocation_id")
	private Long allocation_id;
	
	@Column(name="applicant_id")
	private Long applicant_id;
	
	
	@Column(name="rally_id")
	private Long rally_id;
	
	
	@Column(name="applicant_email")
	private String emailid;
	
	@Column(name="applicant_registration_no")
	private String candidate_registration_no;
	
	@Column(name="is_duplicate")
	private Boolean isDuplicate;
	
	@Column(name="is_rejected")
	private Boolean isRejected;
	
	@Column(name="is_final_allocated")
	private Boolean isFinalAllocated=false;
	
	
	@Column(name="tmp_slot_id")
	private Long tmpslot_id;
	
	@Column(name = "record_tracking")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private java.util.Date record_tracking;

	public TempRallyApplicantAllocation()
	{
		
	}

	

	public TempRallyApplicantAllocation( Long applicant_id, Long rally_id, String emailid,
			String candidate_registration_no, Boolean isDuplicate, Boolean isRejected, Boolean isFinalAllocated,
			Long tmpslot_id) {
		super();
		this.applicant_id = applicant_id;
		this.rally_id = rally_id;
		this.emailid = emailid;
		this.candidate_registration_no = candidate_registration_no;
		this.isDuplicate = isDuplicate;
		this.isRejected = isRejected;
		this.isFinalAllocated = isFinalAllocated;
		this.tmpslot_id = tmpslot_id;
	}



	public Long getAllocation_id() {
		return allocation_id;
	}



	public void setAllocation_id(Long allocation_id) {
		this.allocation_id = allocation_id;
	}



	public Long getApplicant_id() {
		return applicant_id;
	}



	public void setApplicant_id(Long applicant_id) {
		this.applicant_id = applicant_id;
	}



	public Long getRally_id() {
		return rally_id;
	}



	public void setRally_id(Long rally_id) {
		this.rally_id = rally_id;
	}



	public String getEmailid() {
		return emailid;
	}



	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}



	public String getCandidate_registration_no() {
		return candidate_registration_no;
	}



	public void setCandidate_registration_no(String candidate_registration_no) {
		this.candidate_registration_no = candidate_registration_no;
	}



	public Boolean getIsDuplicate() {
		return isDuplicate;
	}



	public void setIsDuplicate(Boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}



	public Boolean getIsRejected() {
		return isRejected;
	}



	public void setIsRejected(Boolean isRejected) {
		this.isRejected = isRejected;
	}



	public Boolean getIsFinalAllocated() {
		return isFinalAllocated;
	}



	public void setIsFinalAllocated(Boolean isFinalAllocated) {
		this.isFinalAllocated = isFinalAllocated;
	}



	public Long getTmpslot_id() {
		return tmpslot_id;
	}



	public void setTmpslot_id(Long tmpslot_id) {
		this.tmpslot_id = tmpslot_id;
	}


	

	public java.util.Date getRecord_tracking() {
		return record_tracking;
	}



	public void setRecord_tracking(java.util.Date record_tracking) {
		this.record_tracking = record_tracking;
	}



	@Override
	public String toString() {
		return "TempRallyApplicantAllocation [allocation_id=" + allocation_id + ", applicant_id=" + applicant_id
				+ ", rally_id=" + rally_id + ", emailid=" + emailid + ", candidate_registration_no="
				+ candidate_registration_no + ", isDuplicate=" + isDuplicate + ", isRejected=" + isRejected
				+ ", isFinalAllocated=" + isFinalAllocated + ", tmpslot_id=" + tmpslot_id + "]";
	}
	
	
	

}
