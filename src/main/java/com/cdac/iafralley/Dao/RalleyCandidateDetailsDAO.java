package com.cdac.iafralley.Dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cdac.iafralley.entity.RalleyCandidateDetails;


public interface RalleyCandidateDetailsDAO extends JpaRepository<RalleyCandidateDetails, Long> {

	public RalleyCandidateDetails findByEmailid(String email);
	
	@Query("select r from RalleyCandidateDetails r where r.aadhar_details= :aadhar")
	public RalleyCandidateDetails findByAadhar_details(@Param("aadhar")String aadhar_details);
	
	@Query("select max(SUBSTRING(a.ralleyregistrationNo,10,5)) from RalleyCandidateDetails a")
	public String maxCount();
	
	@Query("select count(r) from RalleyCandidateDetails r where r.opt_city = :cityid")
	public Long RegisteredCandidateCount(@Param("cityid") Long cityid);

	@Query(nativeQuery = true,value ="select count(datetime_reporting) from candidate_details where datetime_reporting= :day_date")
	public Long countofscheduledStudent(@Param("day_date")Date string);
	
	

}
