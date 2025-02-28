package com.zj.service.impl;

import com.zj.entity.User;
import com.zj.mapper.UserRepository;
import com.zj.service.UserService;
import io.shardingsphere.api.HintManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository userRepository;

    @Override
    public Integer addUser(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public List<User> list() {
        // 强制路由主库
        HintManager.getInstance().setMasterRouteOnly();
        return userRepository.list();
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
