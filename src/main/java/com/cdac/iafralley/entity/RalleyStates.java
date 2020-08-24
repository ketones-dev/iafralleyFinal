package com.cdac.iafralley.entity;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ralley_state_list")
public class RalleyStates {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long state_id;

	@Column(name = "state")
	private String state;
	
	@OneToMany(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,mappedBy="state")
	private List<RalleyCities> cities;
	
	 public RalleyStates() {
		// TODO Auto-generated constructor stub
	}

	public RalleyStates(String state, List<RalleyCities> cities) {
		super();
		this.state = state;
		this.cities = cities;
	}

	
	
	public Long getState_id() {
		return state_id;
	}

	public void setState_id(Long state_id) {
		this.state_id = state_id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<RalleyCities> getCities() {
		return cities;
	}

	public void setCities(List<RalleyCities> cities) {
		this.cities = cities;
	}

	@Override
	public String toString() {
		return "RalleyStates [state_id=" + state_id + ", state=" + state +"]";/*.stream()
	      .map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}")) + "]";*/
	}
	

	
	
}
