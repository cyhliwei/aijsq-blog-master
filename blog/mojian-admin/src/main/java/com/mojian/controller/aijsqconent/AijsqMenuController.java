package com.mojian.controller.aijsqconent;

import java.util.List;

import com.mojian.entity.SysMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.mojian.entity.AijsqMenu;
import com.mojian.service.AijsqMenuService;
import com.mojian.common.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;

/**
 * 菜单项表 控制器
 */
@RestController
@RequestMapping("/sys/aijsqMenu")
@RequiredArgsConstructor
@Api(tags = "菜单项表管理")
public class AijsqMenuController {

    private final AijsqMenuService aijsqMenuService;

    @GetMapping("/tree")
    @ApiOperation(value = "获取菜单树列表")
    public Result<List<AijsqMenu>> getMenuTree(AijsqMenu aijsqMenu) {
        return Result.success(aijsqMenuService.getMenuTree(aijsqMenu));
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取菜单项表列表")
    public Result<IPage<AijsqMenu>> list(AijsqMenu aijsqMenu) {
        return Result.success(aijsqMenuService.selectPage(aijsqMenu));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取菜单项表详情")
    public Result<AijsqMenu> getInfo(@PathVariable("id") String id) {
        return Result.success(aijsqMenuService.getById(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加菜单项表")
    public Result<Object> add(@RequestBody AijsqMenu aijsqMenu) {
        return Result.success(aijsqMenuService.insert(aijsqMenu));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改菜单项表")
    public Result<Object> edit(@RequestBody AijsqMenu aijsqMenu) {
        return Result.success(aijsqMenuService.update(aijsqMenu));
    }

    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "删除菜单项表")
    public Result<Object> remove(@PathVariable List<String> ids) {
        return Result.success(aijsqMenuService.deleteByIds(ids));
    }
}