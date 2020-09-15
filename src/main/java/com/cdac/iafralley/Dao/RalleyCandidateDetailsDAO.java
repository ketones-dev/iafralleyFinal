package com.cdac.iafralley.Dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.iafralley.entity.RalleyCandidateDetails;

@Repository
public interface RalleyCandidateDetailsDAO extends JpaRepository<RalleyCandidateDetails, Long> {

	@Transactional
	@Query("select r from RalleyCandidateDetails r where r.emailid= :email and r.rally_id= :rallyid")
	public RalleyCandidateDetails findByEmailidAndRallyid(@Param("email")String email,@Param("rallyid")String rallyid);
	
	@Transactional
	@Query("select r from RalleyCandidateDetails r where r.aadhar_details= :aadhar and r.rally_id= :rallyid")
	public RalleyCandidateDetails findByAadhar_details(@Param("aadhar")String aadhar_details,@Param("rallyid")String rallyid);
	
	@Transactional
	@Query(nativeQuery = true, value="select max(right(ralley_regid,5)) from candidate_details")
	public String maxCount();
	
	
	@Query("select count(r) from RalleyCandidateDetails r where r.opt_city = :cityid")
	public Long RegisteredCandidateCount(@Param("cityid") Long cityid);

	
	@Query(nativeQuery = true,value ="select count(datetime_reporting) from candidate_details where datetime_reporting= :day_date")
	public Long countofscheduledStudent(@Param("day_date")Date string);
	
	@Query(nativeQuery = true,value ="select * from candidate_details where emailid=:email and ralley_regid=:rallyregid and rally_id=:rallycustid and is_admit_card_genrated=true and is_allocated=true")
	public RalleyCandidateDetails findByEmailidAndRallyidFormail(@Param("email") String email,@Param("rallyregid")String rallyregid,@Param("rallycustid") String rallycustid);
	
	
	@Query("select r from RalleyCandidateDetails r where r.contactno= :mobile and r.rally_id= :rallyid")
	public RalleyCandidateDetails findByContact_no(@Param("mobile")String mobile,@Param("rallyid")String rallyid);

	@Modifying(clearAutomatically = true)
	@Query(nativeQuery = true, value="update candidate_details set is_admit_card_genrated=:b,is_allocated=:c where id= :id")
	public void updateAllocationStatus(boolean b, boolean c,Long id);

	@Modifying(clearAutomatically = true)
	@Query(nativeQuery = true, value="update candidate_details set is_email_send =:b where id= :id")
	public void updateMailSendStauts(boolean b, Long id);

	@Query("select r from RalleyCandidateDetails r where r.id= :id and  r.emailid= :email and r.rally_id= :rallyid and r.ralleyregistrationNo=:rallyregno and r.isAdminCardGenrated=false and r.isAllocated=false")
	public RalleyCandidateDetails findByEmailidAndRallyidAdmitCard(@Param("id")Long id, @Param("email")String email,@Param("rallyid")String rallyid,@Param("rallyregno")String ralleyregno);
	
	

}
