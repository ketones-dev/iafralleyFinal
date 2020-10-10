package com.cdac.iafralley.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="rally_slot_master")
public class RallySlotMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="slot_id")
	private Long slotId;
	
	@Column(name="rally_id")
	private Long rallyId;
	
	
	@Column(name="slot_name")
	private String slotName;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="rally_date")
	private Date rallyDate;
	
	@Column(name="rally_report_time")
	private String rallyReportTime;
	
	@Column(name="intake_count")
	private Long rallyCount;
	
	@Column(name="allocation_count")
	private Long allocationCount;
	
	@Column(name = "record_tracking")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date recordTracking;

	
	public RallySlotMaster() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	 

	public RallySlotMaster(Long slotId, Long rallyId, String slotName, Date rallyDate, String rallyReportTime,
			Long rallyCount, Long allocationCount, Date recordTracking) {
		super();
		this.slotId = slotId;
		this.rallyId = rallyId;
		this.slotName = slotName;
		this.rallyDate = rallyDate;
		this.rallyReportTime = rallyReportTime;
		this.rallyCount = rallyCount;
		this.allocationCount = allocationCount;
		this.recordTracking = recordTracking;
	}





	public Long getSlotId() {
		return slotId;
	}

	public void setSlotId(Long slotId) {
		this.slotId = slotId;
	}

	public Long getRallyId() {
		return rallyId;
	}

	public void setRallyId(Long rallyId) {
		this.rallyId = rallyId;
	}

	public String getSlotName() {
		return slotName;
	}

	public void setSlotName(String slotName) {
		this.slotName = slotName;
	}

	public Date getRallyDate() {
		return rallyDate;
	}

	public void setRallyDate(Date rallyDate) {
		this.rallyDate = rallyDate;
	}

	public String getRallyReportTime() {
		return rallyReportTime;
	}

	public void setRallyReportTime(String rallyReportTime) {
		this.rallyReportTime = rallyReportTime;
	}

	public Long getRallyCount() {
		return rallyCount;
	}

	public void setRallyCount(Long rallyCount) {
		this.rallyCount = rallyCount;
	}

	public Long getAllocationCount() {
		return allocationCount;
	}

	public void setAllocationCount(Long allocationCount) {
		this.allocationCount = allocationCount;
	}

	public Date getRecordTracking() {
		return recordTracking;
	}

	public void setRecordTracking(Date recordTracking) {
		this.recordTracking = recordTracking;
	}

	@Override
	public String toString() {
		return "RallySlotMaster [slotId=" + slotId + ", rallyId=" + rallyId + ", slotName=" + slotName + ", rallyDate="
				+ rallyDate + ", rallyReportTime=" + rallyReportTime + ", rallyCount=" + rallyCount
				+ ", allocationCount=" + allocationCount + ", recordTracking=" + recordTracking + "]";
	}
	
	
	
	
	
	
	
	

}
