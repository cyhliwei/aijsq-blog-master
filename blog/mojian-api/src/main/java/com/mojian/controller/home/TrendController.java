package com.mojian.controller.home;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mojian.common.Result;
import com.mojian.entity.SysAiTrend;
import com.mojian.service.TrendService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@Api(tags = "门户-文章管理")
public class TrendController {
    @Autowired
    private TrendService trendService;

    @GetMapping("/trends")
    public Result<IPage<SysAiTrend>> list(SysAiTrend sysAiTrend) {
        return Result.success(trendService.selectPage(sysAiTrend));
    }

    @GetMapping("/{keyword}")
    public Result<SysAiTrend> getDetail(@PathVariable String keyword) {
        return Result.success(trendService.getDetail(keyword));
    }
}
