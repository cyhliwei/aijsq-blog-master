package com.mojian.controller.home;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mojian.common.Result;
import com.mojian.entity.SysAiTrend;
import com.mojian.entity.SysCategory;
import com.mojian.service.TrendService;
import com.mojian.vo.article.SysCategoryVo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

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
    @GetMapping("/quick-navs")
    public Result<List<HashMap<String, Object>>> quickNavs() {
        return Result.success(trendService.quickNavs());
    }
    @GetMapping("/ai-news")
    public Result<List<HashMap<String, Object>>> aiNews() {
        return Result.success(trendService.aiNews());
    }
    @GetMapping("/tutorial/popular")
    public Result<List<HashMap<String, Object>>> tutorial_popular() {
        return Result.success(trendService.tutorial_popular());
    }

    @GetMapping("/tutorial/categories")
    public Result<List<HashMap<String, Object>>> tutorial_categories() {
        return Result.success(trendService.tutorial_categories());
    }

    @GetMapping("/aiNewsCategories")
    public Result<List<HashMap<String, Object>>> aiNewsCategories() {
        return Result.success(trendService.aiNewsCates());
    }
    @GetMapping("/aiNewsTopics")
    public Result<List<HashMap<String, Object>>> aiNewsTopics() {
        return Result.success(trendService.aiNewsTopics());
    }



}
