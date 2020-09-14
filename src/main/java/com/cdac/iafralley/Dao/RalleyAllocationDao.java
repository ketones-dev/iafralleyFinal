package com.cdac.iafralley.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cdac.iafralley.entity.RallyApplicantAllocation;

public interface RalleyAllocationDao extends JpaRepository<RallyApplicantAllocation, Long> {
	
	
	@Query(nativeQuery = true,value="select * from rally_applicant_allocation_mapping where rally_id= :rallyid and slot_id =:slotid")
	public List<RallyApplicantAllocation> findByRally_idAndSlot_id(@Param("rallyid") Long rallyid,@Param("slotid") Long slotid);
	
	

}
