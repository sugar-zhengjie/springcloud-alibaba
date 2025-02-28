package com.zj.service;

import com.zj.entity.User;

import java.util.List;
public interface UserService {
    Integer addUser(User user);
    List<User> list();
    void deleteAll();
}