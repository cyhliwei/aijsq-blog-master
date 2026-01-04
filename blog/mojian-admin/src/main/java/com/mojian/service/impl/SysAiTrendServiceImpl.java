package com.mojian.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.mojian.mapper.SysAiTrendMapper;
import com.mojian.entity.SysAiTrend;
import com.mojian.service.SysAiTrendService;
import com.mojian.utils.PageUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;

/**
 * AI趋势表 服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysAiTrendServiceImpl extends ServiceImpl<SysAiTrendMapper, SysAiTrend> implements SysAiTrendService {

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
}