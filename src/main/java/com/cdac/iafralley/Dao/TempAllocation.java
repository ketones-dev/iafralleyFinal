package com.cdac.iafralley.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cdac.iafralley.entity.TempRallyApplicantAllocation;

@Repository
public interface TempAllocation extends JpaRepository<TempRallyApplicantAllocation, Long> {

	@Query(value="select count(*) from temp_rally_allocation where tmp_slot_id=:slotid and is_duplicate=false and is_rejected=false",nativeQuery = true)
	Long getAllotedSlotCount(@Param("slotid")Long long1);

	@Query(value="select applicant_id from temp_rally_allocation where rally_id=:rallyid and is_duplicate=true and is_rejected=true",nativeQuery = true)
	List<Long> getApplicantIds(@Param("rallyid")Long ralley_id);
	
	@Query(value="select applicant_id from temp_rally_allocation where rally_id=:rallyid and is_duplicate=false and is_rejected=false",nativeQuery = true)
	List<Long> getAllAllocatedApplicantIds(@Param("rallyid")Long ralley_id);

	@Query(value="select applicant_id from temp_rally_allocation where rally_id=:rallyid and tmp_slot_id=:slotid and is_duplicate=false and is_rejected=false",nativeQuery = true)
	List<Long> getAllAllocatedApplicantIdsWithSlot(@Param("rallyid")Long ralley_id,@Param("slotid") Long slotid);

}
