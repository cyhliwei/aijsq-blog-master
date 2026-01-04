package com.mojian.controller.home;
import java.util.List;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.mojian.entity.SysAiTrend;
import com.mojian.service.SysAiTrendService;
import com.mojian.common.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;

/**
 * AI趋势表 控制器
 */
@RestController
@RequestMapping("/sys/sysAiTrend")
@RequiredArgsConstructor
@Api(tags = "AI趋势表管理")
public class SysAiTrendController {

    private final SysAiTrendService sysAiTrendService;

    @GetMapping("/list")
    @ApiOperation(value = "获取AI趋势表列表")
    public Result<IPage<SysAiTrend>> list(SysAiTrend sysAiTrend) {
        return Result.success(sysAiTrendService.selectPage(sysAiTrend));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取AI趋势表详情")
    public Result<SysAiTrend> getInfo(@PathVariable("id") Long id) {
        return Result.success(sysAiTrendService.getById(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加AI趋势表")
    public Result<Object> add(@RequestBody SysAiTrend sysAiTrend) {
        return Result.success(sysAiTrendService.insert(sysAiTrend));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改AI趋势表")
    public Result<Object> edit(@RequestBody SysAiTrend sysAiTrend) {
        return Result.success(sysAiTrendService.update(sysAiTrend));
    }

    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "删除AI趋势表")
    public Result<Object> remove(@PathVariable List<Long> ids) {
        return Result.success(sysAiTrendService.deleteByIds(ids));
    }
}