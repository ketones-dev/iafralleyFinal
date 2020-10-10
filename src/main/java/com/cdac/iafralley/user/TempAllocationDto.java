package com.cdac.iafralley.user;

import java.util.List;
import java.util.Map;

public class TempAllocationDto {
	
	private Long cityid;
	private Long slotid;
	private List<Long> ids;
	public Long getCityid() {
		return cityid;
	}
	public void setCityid(Long cityid) {
		this.cityid = cityid;
	}
	public Long getSlotid() {
		return slotid;
	}
	public void setSlotid(Long slotid) {
		this.slotid = slotid;
	}
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	 
	
	

}
