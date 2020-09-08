package com.cdac.iafralley.Dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cdac.iafralley.entity.RalleyCandidateDetails;


public interface RalleyCandidateDetailsDAO extends JpaRepository<RalleyCandidateDetails, Long> {

	@Query("select r from RalleyCandidateDetails r where r.emailid= :email and r.rally_id= :rallyid")
	public RalleyCandidateDetails findByEmailidAndRallyid(@Param("email")String email,@Param("rallyid")String rallyid);
	
	@Query("select r from RalleyCandidateDetails r where r.aadhar_details= :aadhar and r.rally_id= :rallyid")
	public RalleyCandidateDetails findByAadhar_details(@Param("aadhar")String aadhar_details,@Param("rallyid")String rallyid);
	
	@Query(nativeQuery = true, value="select max(right(ralley_regid,5)) from candidate_details")
	public String maxCount();
	
	@Query("select count(r) from RalleyCandidateDetails r where r.opt_city = :cityid")
	public Long RegisteredCandidateCount(@Param("cityid") Long cityid);

	@Query(nativeQuery = true,value ="select count(datetime_reporting) from candidate_details where datetime_reporting= :day_date")
	public Long countofscheduledStudent(@Param("day_date")Date string);
	
	@Query("select r from RalleyCandidateDetails r where r.contactno= :mobile and r.rally_id= :rallyid")
	public RalleyCandidateDetails findByContact_no(@Param("mobile")String mobile,@Param("rallyid")String rallyid);
	
	

}
