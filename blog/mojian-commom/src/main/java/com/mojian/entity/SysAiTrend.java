package com.mojian.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mojian.utils.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

@Data
@TableName("sys_ai_trend")
@ApiModel(value = "AI趋势表对象")
public class SysAiTrend implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "类别")
    private String category;

    @ApiModelProperty(value = "搜索量")
    private Integer searchVolume;

    @ApiModelProperty(value = "变化百分比")
    private BigDecimal changePercent;

    @ApiModelProperty(value = "趋势日期")
    //@JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime trendDate;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createTime;
}