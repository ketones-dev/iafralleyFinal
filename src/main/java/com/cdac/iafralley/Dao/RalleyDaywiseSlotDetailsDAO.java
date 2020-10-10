package com.cdac.iafralley.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cdac.iafralley.entity.RalleyDaywiseSlotDetails;
import com.cdac.iafralley.entity.RalleyDetails;
import com.cdac.iafralley.entity.RallySlotMaster;

@Repository
public interface RalleyDaywiseSlotDetailsDAO extends JpaRepository<RalleyDaywiseSlotDetails, Long> {
	
	public List<RalleyDaywiseSlotDetails> findByRalleydetails(RalleyDetails rd);
	
	@Query(nativeQuery = true,value="select * from ralley_daywise_details where ralley_id in (:ralleyids)")
	public List<RalleyDaywiseSlotDetails> getSlot(@Param("ralleyids")List<Long> ralleyids);
	
	

	
	

}
