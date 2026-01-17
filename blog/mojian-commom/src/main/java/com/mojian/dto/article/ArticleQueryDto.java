package com.mojian.dto.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "文章查询对象")
public class ArticleQueryDto {

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "状态 0：下架 1：发布")
    private Integer status;

    @ApiModelProperty(value = "标签id")
    private Integer tagId;
    @ApiModelProperty(value = "内容类型")
    private String contentType;
    @ApiModelProperty(value = "难度级别")
    private String difficulty;
    @ApiModelProperty(value = "agi菜单id")
    private String menuId;
}
