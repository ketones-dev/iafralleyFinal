package com.cdac.iafralley.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cdac.iafralley.entity.TempRallyApplicantAllocation;

@Repository
public interface TempAllocation extends JpaRepository<TempRallyApplicantAllocation, Long> {

}
