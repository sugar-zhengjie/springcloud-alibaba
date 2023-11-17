package com.zj.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zj.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/17 13:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_sys_role")
@ApiModel(value="Role对象", description="角色表")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "父id")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "角色名称")
    @TableField("role_name")
    private String roleName;

    @ApiModelProperty(value = "角色标识")
    @TableField("role_key")
    private String roleKey;

    @ApiModelProperty(value = "角色级别")
    @TableField("role_level")
    private Integer roleLevel;

    @ApiModelProperty(value = "1有效，0禁用")
    @TableField("role_status")
    private Integer roleStatus;

    @ApiModelProperty(value = "角色数据权限")
    @TableField("data_permission_type")
    private String dataPermissionType;

    @ApiModelProperty(value = "备注")
    @TableField("comments")
    private String comments;

}
