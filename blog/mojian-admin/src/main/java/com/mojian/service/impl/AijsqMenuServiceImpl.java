package com.mojian.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mojian.entity.SecureRandomStringGenerator;
import com.mojian.entity.SysMenu;
import org.springframework.stereotype.Service;
import com.mojian.mapper.AijsqMenuMapper;
import com.mojian.entity.AijsqMenu;
import com.mojian.service.AijsqMenuService;
import com.mojian.utils.PageUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * 菜单项表 服务实现类
 */
@Service
@RequiredArgsConstructor
public class AijsqMenuServiceImpl extends ServiceImpl<AijsqMenuMapper, AijsqMenu> implements AijsqMenuService {

    /**
     * 查询菜单项表分页列表
     */
    @Override
    public IPage<AijsqMenu> selectPage(AijsqMenu aijsqMenu) {
        LambdaQueryWrapper<AijsqMenu> wrapper = new LambdaQueryWrapper<>();
        // 构建查询条件
        wrapper.eq(aijsqMenu.getId() != null, AijsqMenu::getId, aijsqMenu.getId());
        wrapper.eq(aijsqMenu.getParentId() != null, AijsqMenu::getParentId, aijsqMenu.getParentId());
        wrapper.eq(aijsqMenu.getTitle() != null, AijsqMenu::getTitle, aijsqMenu.getTitle());
        wrapper.eq(aijsqMenu.getIcon() != null, AijsqMenu::getIcon, aijsqMenu.getIcon());
        wrapper.eq(aijsqMenu.getMenuOrder() != null, AijsqMenu::getMenuOrder, aijsqMenu.getMenuOrder());
        wrapper.eq(aijsqMenu.getLevel() != null, AijsqMenu::getLevel, aijsqMenu.getLevel());
        wrapper.eq(aijsqMenu.getIsFolder() != null, AijsqMenu::getIsFolder, aijsqMenu.getIsFolder());
        wrapper.eq(aijsqMenu.getIsCollapsed() != null, AijsqMenu::getIsCollapsed, aijsqMenu.getIsCollapsed());
        wrapper.eq(aijsqMenu.getIsVisible() != null, AijsqMenu::getIsVisible, aijsqMenu.getIsVisible());
        wrapper.eq(aijsqMenu.getCreatedBy() != null, AijsqMenu::getCreatedBy, aijsqMenu.getCreatedBy());
        wrapper.eq(aijsqMenu.getCreatedAt() != null, AijsqMenu::getCreatedAt, aijsqMenu.getCreatedAt());
        wrapper.eq(aijsqMenu.getUpdatedAt() != null, AijsqMenu::getUpdatedAt, aijsqMenu.getUpdatedAt());
        return page(PageUtil.getPage(), wrapper);
    }

    /**
     * 查询菜单项表列表
     */
    @Override
    public List<AijsqMenu> selectList(AijsqMenu aijsqMenu) {
        LambdaQueryWrapper<AijsqMenu> wrapper = new LambdaQueryWrapper<>();
        // 构建查询条件
        wrapper.eq(aijsqMenu.getId() != null, AijsqMenu::getId, aijsqMenu.getId());
        wrapper.eq(aijsqMenu.getParentId() != null, AijsqMenu::getParentId, aijsqMenu.getParentId());
        wrapper.eq(aijsqMenu.getTitle() != null, AijsqMenu::getTitle, aijsqMenu.getTitle());
        wrapper.eq(aijsqMenu.getIcon() != null, AijsqMenu::getIcon, aijsqMenu.getIcon());
        wrapper.eq(aijsqMenu.getMenuOrder() != null, AijsqMenu::getMenuOrder, aijsqMenu.getMenuOrder());
        wrapper.eq(aijsqMenu.getLevel() != null, AijsqMenu::getLevel, aijsqMenu.getLevel());
        wrapper.eq(aijsqMenu.getIsFolder() != null, AijsqMenu::getIsFolder, aijsqMenu.getIsFolder());
        wrapper.eq(aijsqMenu.getIsCollapsed() != null, AijsqMenu::getIsCollapsed, aijsqMenu.getIsCollapsed());
        wrapper.eq(aijsqMenu.getIsVisible() != null, AijsqMenu::getIsVisible, aijsqMenu.getIsVisible());
        wrapper.eq(aijsqMenu.getCreatedBy() != null, AijsqMenu::getCreatedBy, aijsqMenu.getCreatedBy());
        wrapper.eq(aijsqMenu.getCreatedAt() != null, AijsqMenu::getCreatedAt, aijsqMenu.getCreatedAt());
        wrapper.eq(aijsqMenu.getUpdatedAt() != null, AijsqMenu::getUpdatedAt, aijsqMenu.getUpdatedAt());
        return list(wrapper);
    }

    /**
     * 新增菜单项表
     */
    @Override
    public boolean insert(AijsqMenu aijsqMenu) {
        SecureRandomStringGenerator sc =  new SecureRandomStringGenerator();
        String id = sc.generateRandomString();
        aijsqMenu.setId(id);
        return save(aijsqMenu);
    }

    /**
     * 修改菜单项表
     */
    @Override
    public boolean update(AijsqMenu aijsqMenu) {
        return updateById(aijsqMenu);
    }

    /**
     * 批量删除菜单项表
     */
    @Override
    public boolean deleteByIds(List<String> ids) {
        return removeByIds(ids);
    }

    @Override
    public List<AijsqMenu> getMenuTree(AijsqMenu aijsqMenu) {
        // 获取所有菜单
//        List<AijsqMenu> menusList= new ArrayList<>();
//        List<AijsqMenu> menus = list(new LambdaQueryWrapper<AijsqMenu>()
//                .orderByAsc(AijsqMenu::getMenuOrder));
//
//        // 构建树形结构
//        Map<String, List<AijsqMenu>> childrenMap = menus.stream()
//                .filter(menu -> menu.getParentId() != "0")
//                .collect(Collectors.groupingBy(AijsqMenu::getParentId));
//
//        menus.forEach(menu -> menu.setChildren(childrenMap.get(menu.getId())));
//
//        return menus.stream()
//                .filter(menu -> menu.getParentId() == "0")
//                .collect(Collectors.toList());
        List<AijsqMenu> menusList= new ArrayList<>();
        LambdaQueryWrapper<AijsqMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(aijsqMenu.getType()!=null && aijsqMenu.getType()!="", AijsqMenu::getType, aijsqMenu.getType());
        wrapper.orderByAsc(AijsqMenu::getMenuOrder);
        // 获取所有菜单
        List<AijsqMenu> menus = list(wrapper);


        // 构建树形结构
        Map<String, List<AijsqMenu>> childrenMap = menus.stream()
                .filter(menu -> menu.getParentId() != "0")
                .collect(Collectors.groupingBy(AijsqMenu::getParentId));

        menus.forEach(menu -> menu.setChildren(childrenMap.get(menu.getId())));

        menusList = menus.stream()
                .filter(menu -> menu.getParentId().equals("0"))
                .collect(Collectors.toList());

        return menusList;
    }
}