package com.cdac.iafralley.services;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.entity.RalleyCities;
import com.cdac.iafralley.entity.RalleyDetails;
import com.cdac.iafralley.entity.RalleyGroup_trade;
import com.cdac.iafralley.entity.RalleyStates;
import com.cdac.iafralley.exception.CandidateAllocationSlotAreFull;
import com.cdac.iafralley.exception.CandidateDuplicateEntry;
import com.cdac.iafralley.exception.CandidateSelectedStateCitiesException;
import com.cdac.iafralley.exception.InvalidImageException;

public interface RalleyCandidateDetailsService {
	
	public List<RalleyCandidateDetails> findAll();
	
	public RalleyCandidateDetails save(RalleyCandidateDetails candidate, MultipartFile xMarksheet, MultipartFile xIIMarksheet) throws CandidateSelectedStateCitiesException, CandidateDuplicateEntry, CandidateAllocationSlotAreFull, InvalidImageException;
	
	public RalleyCandidateDetails findById(Long id);
	
	public List<RalleyCities> getallCites(Long stateid);
	
	public List<RalleyStates> getallState();

	public List<RalleyCities> getallCitesByState(Long stateid);

	public List<RalleyCities> getallCitesByStateAdminAlloted(Long long1);

	public List<RalleyStates> getralleyAllState();

	public Boolean getregisteredCount(Long cityid);

	public String showOptRalleyDetailstoCandidate(Long long1);

	public Map<String, String> showOptValidRalleyDetailstoCandidate(Long long1);

	public List<RalleyGroup_trade> getralleyCreatedGroups(Long long1);
	
	
	

}
