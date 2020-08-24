package com.cdac.iafralley.Dao;

import  com.cdac.iafralley.entity.User;

public interface UserDao {

    public User findByUserName(String userName);
    
    public void save(User user);
    
}
