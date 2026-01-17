// blog/mojian-server/src/main/java/com/mojian/controller/SeoController.java
package com.mojian;

import com.mojian.entity.SysArticle;
import com.mojian.entity.SysCategory;
import com.mojian.entity.SysWebConfig;
import com.mojian.service.SysArticleService;
import com.mojian.service.SysCategoryService;
import com.mojian.service.SysWebConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * SEO 页面渲染控制器
 * 专门为搜索引擎爬虫提供静态 HTML 渲染
 */
@Controller
@RequiredArgsConstructor
public class SeoController {

    private final SysArticleService articleService;
    private final SysCategoryService categoryService;
    private final SysWebConfigService webConfigService;

    /**
     * 文章详情页 SEO 渲染
     * 路由: /article/{id}
     */
    @GetMapping(value = "/seo-posts/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String renderArticle(@PathVariable Long id, HttpServletRequest request) {
        // 1. 查询文章详情
        SysArticle article = articleService.getById(id);
        if (article == null || article.getStatus() != 1) {
            return render404("文章不存在或未发布");
        }

        // 2. 查询文章所属分类
        SysCategory category = null;
        if (article.getCategoryId() != null) {
            category = categoryService.getById(article.getCategoryId());
        }

        // 3. 查询网站配置
        SysWebConfig webConfig = webConfigService.getConfig();

        // 4. 构建模板数据
        request.setAttribute("article", article);
        request.setAttribute("category", category);
        request.setAttribute("webConfig", webConfig);
        request.setAttribute("canonical", request.getRequestURL().toString());

        // 5. 返回 Thymeleaf 模板
        return "seo/article-detail";
    }

    /**
     * 首页 SEO 渲染（带分页）
     */
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String renderHome(
            @RequestParam(defaultValue = "1") Integer page,
            HttpServletRequest request) {

        // 验证页码
        if (page < 1) {
            page = 1;
        }

        int pageSize = 2;

        // 1. 查询网站配置
        SysWebConfig webConfig = webConfigService.getConfig();

        // 2. 查询分类列表
        List<SysCategory> categories = categoryService.list();

        // 3. 查询文章列表（分页）
        Map<String, Object> articlePage = articleService.getSeoArticleList(page, pageSize);

        // 4. 计算总页数
        long total = (Long) articlePage.get("total");
        int totalPages = (int) Math.ceil((double) total / pageSize);

        // 5. 构建模板数据
        request.setAttribute("webConfig", webConfig);
        request.setAttribute("categories", categories);
        request.setAttribute("articles", articlePage.get("records"));
        request.setAttribute("total", total);
        request.setAttribute("current", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPages", totalPages);  // ✅ 直接传入总页数

        return "seo/home";
    }

    /**
     * 分类页 SEO 渲染
     * 路由: /category/{id}
     */
//    @GetMapping(value = "/category/{id}", produces = MediaType.TEXT_HTML_VALUE)
//    public String renderCategory(
//            @PathVariable Long id,
//            @RequestParam(defaultValue = "1") Integer page,
//            HttpServletRequest request) {
//
//        // 1. 查询分类详情
//        SysCategory category = categoryService.getById(id);
//        if (category == null) {
//            return render404("分类不存在");
//        }
//
//        // 2. 查询网站配置
//        SysWebConfig webConfig = webConfigService.getConfig();
//
//        // 3. 查询该分类下的文章
//        Map<String, Object> articlePage = articleService.getSeoArticleByCategory(id, page, 10);
//
//        // 4. 构建模板数据
//        request.setAttribute("category", category);
//        request.setAttribute("webConfig", webConfig);
//        request.setAttribute("articles", articlePage.get("records"));
//        request.setAttribute("total", articlePage.get("total"));
//        request.setAttribute("current", page);
//        request.setAttribute("canonical", request.getRequestURL().toString());
//
//        return "seo/category";
//    }
    @GetMapping(value = "/seo-category/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String renderCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer page,
            HttpServletRequest request) {

        int pageSize = 10;

        // 1. 查询分类详情
        SysCategory category = categoryService.getById(id);
        if (category == null) {
            request.setAttribute("message", "分类不存在");
            return "seo/404";
        }

        // 2. 查询网站配置
        SysWebConfig webConfig = webConfigService.getConfig();

        // 3. 查询该分类下的文章
        Map<String, Object> articlePage = articleService.getSeoArticleByCategory(id, page, pageSize);

        // 4. 计算总页数
        long total = (Long) articlePage.get("total");
        int totalPages = (int) Math.ceil((double) total / pageSize);

        // 5. 构建模板数据
        request.setAttribute("category", category);
        request.setAttribute("webConfig", webConfig);
        request.setAttribute("articles", articlePage.get("records"));
        request.setAttribute("total", total);
        request.setAttribute("current", page);
        request.setAttribute("totalPages", totalPages);

        return "seo/category";
    }

    /**
     * 404 页面渲染
     */
    @GetMapping(value = "/404", produces = MediaType.TEXT_HTML_VALUE)
    public String render404(HttpServletRequest request) {
        return render404("页面不存在", request);
    }

    private String render404(String message) {
        return render404(message, null);
    }

    private String render404(String message, HttpServletRequest request) {
        if (request != null) {
            request.setAttribute("message", message);
            SysWebConfig webConfig = webConfigService.getConfig();
            request.setAttribute("webConfig", webConfig);
        }
        return "seo/404";
    }
}