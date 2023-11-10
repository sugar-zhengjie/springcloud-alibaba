package com.zj.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zj.user.entity.User;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/10 14:10
 */
public interface UserMapper extends BaseMapper<User> {

    int insertOne(User user);

    int updateUserById(@Param("id") User user);

    IPage<User> selectUserInfoByPage(Page<Object> objectPage,
                                     @Param("signCondition") String signCondition);

    int deleteUsersByBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);
}
