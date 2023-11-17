package com.zj.auth.runner;

import cn.hutool.core.collection.CollectionUtil;
import com.zj.auth.entity.Resource;
import com.zj.common.constant.AuthConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 容器启动完成加载资源权限数据到缓存
 * ApplicationContext 完全初始化后，且在 SpringApplication.run(...) 方法完成之前执行
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/17 13:36
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class InitResourceRolesCacheRunner implements CommandLineRunner {

    private final RedisTemplate redisTemplate;

    //private final IResourceService resourceService;

    /**
     * 是否开启租户模式
     */
    @Value(("${tenant.enable}"))
    private Boolean enable;

    @Override
    public void run(String... args) {

        log.info("InitResourceRolesCacheRunner running");

        // 查询系统角色和权限的关系
        //List<Resource> resourceList = resourceService.queryResourceRoleIds();
        List<Resource> resourceList = new ArrayList<Resource>();

        // 判断是否开启了租户模式，如果开启了，那么角色权限需要按租户进行分类存储
        if (enable) {
            Map<Long, List<Resource>> resourceListMap =
                    resourceList.stream().collect(Collectors.groupingBy(Resource::getTenantId));
            resourceListMap.forEach((key, value) -> {
                String redisKey = AuthConstant.TENANT_RESOURCE_ROLES_KEY + key;
                redisTemplate.delete(redisKey);
                addRoleResource(redisKey, value);
                System.out.println(redisTemplate.opsForHash().entries(redisKey).size());
            });
        } else {
            redisTemplate.delete(AuthConstant.RESOURCE_ROLES_KEY);
            addRoleResource(AuthConstant.RESOURCE_ROLES_KEY, resourceList);
        }
    }

    private void addRoleResource(String key, List<Resource> resourceList) {
        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
        Optional.ofNullable(resourceList).orElse(new ArrayList<>()).forEach(resource -> {
            // roleId -> ROLE_{roleId}
            List<String> roles = Optional.ofNullable(resource.getRoleIds()).orElse(new ArrayList<>()).stream()
                    .map(roleId -> AuthConstant.AUTHORITY_PREFIX + roleId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(roles)) {
                resourceRolesMap.put(resource.getResourceUrl(), roles);
            }
        });
        redisTemplate.opsForHash().putAll(key, resourceRolesMap);
    }
}
