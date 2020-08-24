package com.cdac.iafralley.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cdac.iafralley.entity.RalleyGroup_trade;


@Repository
public interface RalleyGroupDAO extends JpaRepository<RalleyGroup_trade, Long> {

	@Query(nativeQuery =true,value = "SELECT * FROM ralley_group_trade as e WHERE e.group_id IN (:ids)") 
	public List<RalleyGroup_trade> getDetails(@Param("ids")List<Long> ids);
}
