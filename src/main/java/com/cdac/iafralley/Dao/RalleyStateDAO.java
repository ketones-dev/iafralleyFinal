package com.cdac.iafralley.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cdac.iafralley.entity.RalleyStates;

@Repository
public interface RalleyStateDAO extends JpaRepository<RalleyStates, Long> {

	@Query(nativeQuery =true,value = "SELECT * FROM ralley_state_list as e WHERE e.id IN (:ids)") 
	public List<RalleyStates> getallStateonBasisOfList(@Param("ids")List<Long> ids);
}
