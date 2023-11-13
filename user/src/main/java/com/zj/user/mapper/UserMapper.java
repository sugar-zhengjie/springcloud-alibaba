package com.zj.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zj.user.entity.User;
import com.zj.user.entity.UserVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/13 10:35
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 顺序参数查找
     */
    User selectUserByOrder(String name, Integer status);

    /**
     * 注解参数查找
     */
    User selectUserByAnnotation(@Param("userName") String name, @Param("status") Integer status);

    /**
     * Map参数查找
     */
    User selectUserByMap(Map<String, Object> params);

    /**
     * Bean参数查找
     */
    User selectUserByBean(User user);

    /**
     * 根据Id获取某个属性
     */
    String selectEmailById(@Param("id") Long id);

    /**
     * 获取多条记录
     */
    List<User> getAllUsers();

    /**
     * 查询结果 map 单条
     */
    Map<String, Object> getUserAsMapById(Long id);

    /**
     * 查询结果 map 多条
     * 查询所有学生的信息，把数据库中的 'id' 字段作为 key,对应的 value 封装成 User 对象
     * MapKey 中的值表示用数据库中的哪个字段名作 key
     */
    @MapKey("id")
    Map<Long, User> getAllUserAsMap();

    /**
     * 查询所有用户id，username，resultMap做映射处理
     */
    List<User> findAll();

    /**
     * if条件的使用
     */
    List<User> queryUserIf(Map<String, Object> map);

    /**
     * where条件的使用
     */
    List<User> queryUserWhere(Map<String, Object> map);

    /**
     * choose,when,otherwise条件的使用
     */
    List<User> queryUserchoose(Map<String, Object> map);

    /**
     * set 用于更新操作(并且会智能的去掉最后一个语句后面的逗号)
     */
    int updatePwd(@Param("username") String username, @Param("password") String password, @Param("id") Integer id);

    /**
     * 分页
     */
    List<User> getUserByLimit(Map<String,Integer> data);

    /**
     * 分页插件
     */
    //@Select("select * from user")
    List<User> findAllByPageHelper();

}
