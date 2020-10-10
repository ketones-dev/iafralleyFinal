package com.cdac.iafralley.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cdac.iafralley.entity.RallySlotMaster;

@Repository
public interface  RallySlotMasterDao extends JpaRepository<RallySlotMaster,Long> {
	
	@Query(nativeQuery = true,value="select * from rally_slot_master where rally_id= :ralleyid order by rally_date asc")
	public List<RallySlotMaster> findSlotOnBasisOfRallyIdFromSlotMaster(@Param("ralleyid")Long rallyid);
	
	@Query(nativeQuery = true,value="select * from rally_slot_master where rally_id=:ralleyid")
	public List<RallySlotMaster> getSlotOnSingleId(@Param("ralleyid")Long ralleyid);

	@Query(nativeQuery = true,value="select * from rally_slot_master where slot_id=:slotid")
	public RallySlotMaster getSlotDetails(@Param("slotid")Long slotid);

	

}
