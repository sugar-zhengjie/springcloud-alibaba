package com.zj.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import cn.hutool.core.date.DateUtil;
import java.util.Date;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/10 13:45
 */
@Embeddable
@Getter
@Setter
@Data
@Accessors(chain = true)
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID 雪花算法")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("delete_flag")
    @ApiModelProperty(value = "删除状态(默认0未删除 | 1删除)")
    @TableLogic(value = "false", delval = "true")
    private Boolean deleteFlag = false;

    @TableField(value = "created_by", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @TableField(value = "updated_by", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新人")
    private Long updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "created_date", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "updated_date", fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updatedDate;

    @TableField("version")
    @Version
    private Integer version;

    public void insertMapper(Long createdBy) {
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
        this.createdDate = DateUtil.date();
        this.updatedDate = DateUtil.date();
    }

    public void editMapper(Long updatedBy) {
        this.updatedBy = updatedBy;
        this.updatedDate = DateUtil.date();
    }

    public void editMapper(Long updatedBy, Long id) {
        this.updatedBy = updatedBy;
        this.updatedDate = DateUtil.date();
        this.id = id;
    }

    public static final String ID = "id";
    public static final String DELETE_FLAG = "delete_flag";
    public static final String CREATED_BY = "created_by";
    public static final String CREATED_DATE = "created_date";
    public static final String UPDATED_BY = "updated_by";
    public static final String UPDATED_DATE = "updated_date";

}
