package com.cdac.iafralley.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ralley_cities_list")
public class RalleyCities {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long city_id;

	@Column(name = "city")
	private String city;
	
	@ManyToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL)
	@JoinColumn(name="state_id")
	@JsonIgnore
	private RalleyStates state;
	
	public RalleyCities() {
		// TODO Auto-generated constructor stub
	}

	public RalleyCities(String city, RalleyStates state) {
		super();
		this.city = city;
		this.state = state;
	}

	
	public Long getCity_id() {
		return city_id;
	}

	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public RalleyStates getState() {
		return state;
	}

	public void setState(RalleyStates state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "RalleyCities [city_id=" + city_id + ", city=" + city +  "]";
	}
	
	
	
	
	
	
	
	
	

}
