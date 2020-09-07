package com.cdac.iafralley.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.entity.RalleyDetails;

@Repository
public interface RalleyDetailsDAO extends JpaRepository<RalleyDetails, Long> {
	
	
	
	
	@Query("select distinct r.state_id from RalleyDetails r where r.active=true")
	public List<Long> findDistinctAllotStates();
	
	@Query("select distinct r.city_id from RalleyDetails r where r.state_id = :stateid and r.active=true")
	public List<Long> findDistinctAllotCities(@Param("stateid") Long stateid);
	
	@Query("select r.ralley_id from RalleyDetails r where r.city_id= :cityid")
	public List<Long> getRalleyByCitySelected(@Param("cityid") Long cityid);
	
	@Query("from RalleyDetails r where r.city_id= :cityid")
	public List<RalleyDetails> getRalleyDetailsByCitySelected(@Param("cityid") Long cityid);
	
	@Query(nativeQuery = true,value = "select distinct on(city_id) * from ralley_details")
	public List<RalleyDetails> findDistinctCity_id();
	
	@Query("select max(SUBSTRING(a.ralley_cust_id,10,5)) from RalleyDetails a")
	public String maxCount();
	
	@Query("from RalleyDetails r where r.ralley_cust_id= :id")
	public RalleyDetails findByRalley_cust_id(@Param("id") String id);
	
	@Query(nativeQuery = true,value = "select distinct on(city_id) * from ralley_details where active=true")
	public List<RalleyDetails> findDistinctHeadingDetails();

	@Query(nativeQuery = true,value = "select opt_state_name,count(opt_state_name) total from candidate_details group by opt_state_name;")
	public List<String> getAllRalleyCount();
	
	

}
