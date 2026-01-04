package com.mojian.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mojian.entity.SysAiTrend;
import com.mojian.mapper.SysAiTrendMapper;
import com.mojian.service.TrendService;
import com.mojian.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI趋势表 服务实现类
 */
@Service
@RequiredArgsConstructor
public class TrendServiceImpl extends ServiceImpl<SysAiTrendMapper, SysAiTrend> implements TrendService {

    @Autowired
    SysAiTrendMapper trendMapper;

    /**
     * 查询AI趋势表分页列表
     */
    @Override
    public IPage<SysAiTrend> selectPage(SysAiTrend sysAiTrend) {
        LambdaQueryWrapper<SysAiTrend> wrapper = new LambdaQueryWrapper<>();
        // 构建查询条件
        wrapper.eq(sysAiTrend.getId() != null, SysAiTrend::getId, sysAiTrend.getId());
        wrapper.eq(sysAiTrend.getKeyword() != null && sysAiTrend.getKeyword() !="", SysAiTrend::getKeyword, sysAiTrend.getKeyword());
        wrapper.eq(sysAiTrend.getCategory() != null && sysAiTrend.getCategory() != "", SysAiTrend::getCategory, sysAiTrend.getCategory());
        wrapper.eq(sysAiTrend.getSearchVolume() != null, SysAiTrend::getSearchVolume, sysAiTrend.getSearchVolume());
        wrapper.eq(sysAiTrend.getChangePercent() != null, SysAiTrend::getChangePercent, sysAiTrend.getChangePercent());
        wrapper.eq(sysAiTrend.getTrendDate() != null, SysAiTrend::getTrendDate, sysAiTrend.getTrendDate());
        wrapper.eq(sysAiTrend.getCreateTime() != null, SysAiTrend::getCreateTime, sysAiTrend.getCreateTime());
        return page(PageUtil.getPage(), wrapper);
    }

    /**
     * 查询AI趋势表列表
     */
    @Override
    public List<SysAiTrend> selectList(SysAiTrend sysAiTrend) {
        LambdaQueryWrapper<SysAiTrend> wrapper = new LambdaQueryWrapper<>();
        // 构建查询条件
        wrapper.eq(sysAiTrend.getId() != null, SysAiTrend::getId, sysAiTrend.getId());
        wrapper.eq(sysAiTrend.getKeyword() != null, SysAiTrend::getKeyword, sysAiTrend.getKeyword());
        wrapper.eq(sysAiTrend.getCategory() != null, SysAiTrend::getCategory, sysAiTrend.getCategory());
        wrapper.eq(sysAiTrend.getSearchVolume() != null, SysAiTrend::getSearchVolume, sysAiTrend.getSearchVolume());
        wrapper.eq(sysAiTrend.getChangePercent() != null, SysAiTrend::getChangePercent, sysAiTrend.getChangePercent());
        wrapper.eq(sysAiTrend.getTrendDate() != null, SysAiTrend::getTrendDate, sysAiTrend.getTrendDate());
        wrapper.eq(sysAiTrend.getCreateTime() != null, SysAiTrend::getCreateTime, sysAiTrend.getCreateTime());
        return list(wrapper);
    }

    /**
     * 新增AI趋势表
     */
    @Override
    public boolean insert(SysAiTrend sysAiTrend) {
        return save(sysAiTrend);
    }

    /**
     * 修改AI趋势表
     */
    @Override
    public boolean update(SysAiTrend sysAiTrend) {
        return updateById(sysAiTrend);
    }

    /**
     * 批量删除AI趋势表
     */
    @Override
    public boolean deleteByIds(List<Long> ids) {
        return removeByIds(ids);
    }

    @Override
    public SysAiTrend getDetail(String keyword) {
        LambdaQueryWrapper<SysAiTrend> wrapper = new LambdaQueryWrapper<>();
        // 构建查询条件
        wrapper.eq(keyword != null && keyword !="", SysAiTrend::getKeyword, keyword);
        return trendMapper.selectOne(wrapper);
    }
}