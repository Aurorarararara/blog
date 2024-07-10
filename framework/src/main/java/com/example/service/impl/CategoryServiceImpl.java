package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.SystemConstants;
import com.example.domain.ResponseResult;
import com.example.domain.entity.Article;
import com.example.domain.entity.Category;
import com.example.domain.vo.CategoryVo;
import com.example.mapper.CategoryMapper;
import com.example.service.ArticleService;
import com.example.service.CategoryService;
import com.example.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2024-07-09 10:00:21
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;


    @Override
    public ResponseResult getCategoryList() {
        //查询文章表 状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id
        Set<Long> categoryId = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categories = listByIds(categoryId);
        categories.stream().
                filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());


        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }
}
