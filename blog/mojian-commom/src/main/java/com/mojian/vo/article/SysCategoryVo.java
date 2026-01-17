package com.mojian.vo.article;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mojian.utils.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "分类视图对象")
public class SysCategoryVo implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "URL友好名称")
    private String slug;

    @ApiModelProperty(value = "分类描述")
    private String description;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "背景颜色")
    private String bgColor;

    @ApiModelProperty(value = "分类类型:'0:article','1:tutorial','2:tool','3:path'")
    private Integer type;

    @ApiModelProperty(value = "是否启用 0:禁用 1:启用")
    private Integer isActive;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "导航颜色")
    private String navColor;

    @ApiModelProperty(value = "导航图标")
    private String navIcon;

    @ApiModelProperty(value = "导航排序")
    private Integer navOrder;

    @ApiModelProperty(value = "是否在导航显示")
    private Integer navEnabled;
}
