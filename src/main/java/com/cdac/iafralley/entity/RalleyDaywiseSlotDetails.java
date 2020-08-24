package com.cdac.iafralley.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="ralley_daywise_details")
public class RalleyDaywiseSlotDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="slot_id")
	private Long slot_id;
	
	@NotNull
	@Column(name="no_of_intake")
	private Long no_of_intake;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="day_date")
	private Date day_date;
	
	@NotNull
	@NotEmpty
	@Column(name="time_of_reporting")
	private  String time_of_reporting;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL)
	@JoinColumn(name="ralley_id")
	private RalleyDetails ralleydetails;

	public RalleyDaywiseSlotDetails() {
		// TODO Auto-generated constructor stub
	}

	public RalleyDaywiseSlotDetails(@NotNull Long slot_id,@NotNull Long no_of_intake, @NotNull Date day_date,
			@NotNull @NotNull @NotNull String time_of_reporting) {
		super();
		this.slot_id=slot_id;
		this.no_of_intake = no_of_intake;
		this.day_date = day_date;
		this.time_of_reporting = time_of_reporting;
	}
	
	

	public Long getSlot_id() {
		return slot_id;
	}

	public void setSlot_id(Long slot_id) {
		this.slot_id = slot_id;
	}

	public Long getNo_of_intake() {
		return no_of_intake;
	}

	public void setNo_of_intake(Long no_of_intake) {
		this.no_of_intake = no_of_intake;
	}

	public Date getDay_date() {
		return day_date;
	}

	public void setDay_date(Date day_date) {
		this.day_date = day_date;
	}

	public String getTime_of_reporting() {
		return time_of_reporting;
	}

	public void setTime_of_reporting(@NotNull @NotNull String time_of_reporting) {
		this.time_of_reporting = time_of_reporting;
	}

	public RalleyDetails getRalleydetails() {
		return ralleydetails;
	}

	public void setRalleydetails(RalleyDetails ralleydetails) {
		this.ralleydetails = ralleydetails;
	}

	@Override
	public String toString() {
		return "RalleyDaywiseSlotDetails [slot_id=" + slot_id + ", no_of_intake=" + no_of_intake + ", day_date="
				+ day_date + ", time_of_reporting=" + time_of_reporting + "]";
	}
	
	
	
	
	

}
