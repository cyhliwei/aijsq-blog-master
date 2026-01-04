package com.mojian.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mojian.entity.SysAiTrend;

import java.util.List;

/**
 * AI趋势表 服务接口
 */
public interface TrendService extends IService<SysAiTrend> {
    /**
     * 查询AI趋势表分页列表
     */
    IPage<SysAiTrend> selectPage(SysAiTrend sysAiTrend);

    /**
     * 查询AI趋势表列表
     */
    List<SysAiTrend> selectList(SysAiTrend sysAiTrend);

    /**
     * 新增AI趋势表
     */
    boolean insert(SysAiTrend sysAiTrend);

    /**
     * 修改AI趋势表
     */
    boolean update(SysAiTrend sysAiTrend);

    /**
     * 批量删除AI趋势表
     */
    boolean deleteByIds(List<Long> ids);

    SysAiTrend getDetail(String keyword);
}