package com.cdac.iafralley.services;

import com.cdac.iafralley.entity.User;
import com.cdac.iafralley.user.CrmUser;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	public User findByUserName(String userName);

	public void save(CrmUser crmUser);
}
