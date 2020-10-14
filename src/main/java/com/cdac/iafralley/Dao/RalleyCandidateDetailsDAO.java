package com.cdac.iafralley.Dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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
	@Query(nativeQuery=true,value="select * from candidate_details where emailid= :email and rally_id= :rallyid")
	public RalleyCandidateDetails findByEmailidAndRallyid(@Param("email")String email,@Param("rallyid")Long rallyid);
	
	@Transactional
	@Query(nativeQuery=true,value="select * from candidate_details  where aadhar_details= :aadhar and rally_id= :rallyid")
	public RalleyCandidateDetails findByAadhar_details(@Param("aadhar")String aadhar_details,@Param("rallyid")Long rallyid);
	
	@Transactional
	@Query(nativeQuery = true, value="select max(right(ralley_regid,5)) from candidate_details where opt_state=:optstateid and opt_city=:optcityid")
	public String maxCount(@Param("optstateid") Long optstateid,@Param("optcityid") Long optcityid);
	
	
	@Query("select count(r) from RalleyCandidateDetails r where r.opt_city = :cityid")
	public Long RegisteredCandidateCount(@Param("cityid") Long cityid);
	
	
	@Query(value = "SELECT r FROM RalleyCandidateDetails r   WHERE r.opt_city = :cityid ",
		       countQuery = "SELECT count(r) FROM RalleyCandidateDetails r  WHERE r.opt_city = :cityid")	      
	public Page<RalleyCandidateDetails> findByCityid(@Param("cityid")Long cityid,Pageable pageable);

	
	@Query(nativeQuery = true,value ="select count(datetime_reporting) from candidate_details where datetime_reporting= :day_date")
	public Long countofscheduledStudent(@Param("day_date")Date string);
	
	@Query(nativeQuery = true,value ="select * from candidate_details where applicant_id=:id and emailid=:email and ralley_regid=:rallyregid and rally_id=:rallycustid and is_admit_card_genrated=true and is_allocated=true")
	public RalleyCandidateDetails findByEmailidAndRallyidFormail(@Param("id")Long id,@Param("email") String email,@Param("rallyregid")String rallyregid,@Param("rallycustid") Long rallycustid);
	
	
	@Query("select r from RalleyCandidateDetails r where r.contactno= :mobile and r.rally_id= :rallyid")
	public RalleyCandidateDetails findByContact_no(@Param("mobile")String mobile,@Param("rallyid")String rallyid);

	@Modifying(clearAutomatically = true)
	@Query(nativeQuery = true, value="update candidate_details set is_admit_card_genrated=:b,is_allocated=:c where applicant_id= :id")
	public void updateAllocationStatus(boolean b, boolean c,Long id);
	
	@Modifying(clearAutomatically = true)
	@Query(nativeQuery = true, value="update candidate_details set is_temp_allocated=true where applicant_id= :id")
	public void updateTempAllocationStatus(Long id);
	

	@Modifying(clearAutomatically = true)
	@Query(nativeQuery = true, value="update candidate_details set is_email_send =:b,allot_venu_details=:venue,datetime_reporting=(select rally_date from rally_slot_master where slot_id=:Slotid) where applicant_id= :id")
	public void updateMailSendStauts(boolean b, Long id,String venue,Long Slotid);

	@Query("select r from RalleyCandidateDetails r where r.id= :id and  r.emailid= :email  and r.ralleyregistrationNo=:rallyregno and r.isAdminCardGenrated=false and r.isAllocated=false")
	public RalleyCandidateDetails findByEmailidAndRallyidAdmitCard(@Param("id")Long id, @Param("email")String email,@Param("rallyregno")String ralleyregno);

	
	@Modifying(clearAutomatically = true)
	@Query(nativeQuery = true, value="update rally_applicant_allocation_mapping set admit_card_path =:name where applicant_id= :id and applicant_email=:emailid")
	public void updatePath(@Param("name")String name,@Param("id") Long id, @Param("emailid")String emailid);

	@Query(value="select * from candidate_details where opt_city=:cityid and passed_exam_percentage >= :per and is_temp_allocated=false and is_rejected=false  order by passed_exam_percentage asc LIMIT :intake",nativeQuery = true)
	public List<RalleyCandidateDetails> getintakebaseFilteredData(@Param("intake")int intake,@Param("per") Integer passPercentage,@Param("cityid") Long cityid);
	
	@Query(value="select * from candidate_details where opt_city=:cityid and passed_exam_percentage >= :per and is_temp_allocated=false and is_rejected=false",nativeQuery = true)
	public List<RalleyCandidateDetails> getFilteredData(@Param("per") Long passPercentage,@Param("cityid") Long cityid);
	
	

	@Query(value="SELECT * FROM (SELECT *, count(*) OVER (PARTITION BY name,father_name) AS count FROM candidate_details where opt_city=?1) tableWithCount"
			 +" WHERE tableWithCount.count > 1",nativeQuery = true)
	public List<RalleyCandidateDetails> getDuplicatesAsPerCheckedD(Long value);
	
	@Query(value="SELECT * FROM (SELECT *, count(*) OVER (PARTITION BY name,father_name,emailid) AS count FROM candidate_details where opt_city=?1) tableWithCount"
			 +" WHERE tableWithCount.count > 1",nativeQuery = true)
	public List<RalleyCandidateDetails> getDuplicatesAsPerCheckedEmailAndOther(Long value);
	
	@Query(value="SELECT * FROM (SELECT *, count(*) OVER (PARTITION BY emailid) AS count FROM candidate_details where opt_city=?1) tableWithCount"
			 +" WHERE tableWithCount.count > 1",nativeQuery = true)
	public List<RalleyCandidateDetails> getDuplicatesAsPerCheckedEmailOnly(Long value);
	
	@Query(value="SELECT * FROM (SELECT *, count(*) OVER (PARTITION BY name,father_name,email) AS count FROM candidate_details where opt_city=?1) tableWithCount"
			 +" WHERE tableWithCount.count > 1",nativeQuery = true)
	public List<RalleyCandidateDetails> getDuplicatesAsPerCheckedEmailAndMobile(Long value);
	
	@Query(value="SELECT * FROM (SELECT *, count(*) OVER (PARTITION BY name,father_name) AS count FROM candidate_details where opt_city=?1) tableWithCount"
			 +" WHERE tableWithCount.count > 1",nativeQuery = true)
	public List<RalleyCandidateDetails> getDuplicatesAsPerCheckedMobile(Long value);
	
	@Query(value="SELECT * FROM (SELECT *, count(*) OVER (PARTITION BY name,father_name,emailid,aadhar_details) AS count FROM candidate_details where opt_city=?1) tableWithCount"
			 +" WHERE tableWithCount.count > 1",nativeQuery = true)
	public List<RalleyCandidateDetails> getDuplicatesAsPerCheckedaadharandOther(Long value);
	
	@Query(value="SELECT * FROM (SELECT *, count(*) OVER (PARTITION BY name,father_name,aadhar_details) AS count FROM candidate_details where opt_city=?1) tableWithCount"
			 +" WHERE tableWithCount.count > 1",nativeQuery = true)
	public List<RalleyCandidateDetails> getDuplicatesAsPerCheckedAadharandName(Long value);
	
	@Query(value="SELECT * FROM (SELECT *, count(*) OVER (PARTITION BY emailid,aadhar_details) AS count FROM candidate_details where opt_city=?1) tableWithCount"
			 +" WHERE tableWithCount.count > 1",nativeQuery = true)
	public List<RalleyCandidateDetails> getDuplicatesAsPerCheckedEmailAndaadhar(Long cityid);
	
	@Query(value="select * from candidate_details where applicant_id in (:list)",nativeQuery = true)
	public List<RalleyCandidateDetails> getDetailsOnBasisOfIds(@Param("list")List<Long> list);

	@Modifying(clearAutomatically = true)
	@Query(value="update candidate_details set is_rejected=true where applicant_id=:id",nativeQuery = true)
	public void updateDuplicateFlag(@Param("id")Long id);

	
	

}
