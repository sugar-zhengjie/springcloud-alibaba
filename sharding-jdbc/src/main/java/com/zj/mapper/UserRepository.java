package com.zj.mapper;


import com.zj.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserRepository {

    Integer addUser(User user);
    List<User> list();
    void deleteAll();
}