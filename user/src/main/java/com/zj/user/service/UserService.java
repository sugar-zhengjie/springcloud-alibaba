package com.zj.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zj.user.entity.User;
import com.zj.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/13 14:45
 */
@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * mybatis + pagehelper
     */
    public void findAllByPageHelper(){
        int pageNum = 0;
        int pageSize = 20;
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.findAllByPageHelper();
    }

    /**
     *mybatis plus 分页插件分页
     */
    public IPage<User> selectUserPage(Integer state) {
        Page<User> page = new Page<>(0, 20);
        // 构造查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", state);

        // 执行分页查询
        return userMapper.selectPage(page, queryWrapper);
    }
}
