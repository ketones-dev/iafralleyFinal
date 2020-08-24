package com.cdac.iafralley.Dao;

import  com.cdac.iafralley.entity.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}
