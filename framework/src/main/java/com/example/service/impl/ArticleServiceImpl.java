package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.ResponseResult;
import com.example.domain.entity.Article;
import com.example.domain.entity.Category;
import com.example.domain.vo.ArticleDetailVo;
import com.example.domain.vo.ArticleListVo;
import com.example.domain.vo.HotArticleVo;
import com.example.domain.vo.PageVo;
import com.example.mapper.ArticleMapper;
import com.example.service.ArticleService;
import com.example.service.CategoryService;
import com.example.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.constants.SystemConstants.ARTICLE_STATUS_NORMAL;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult hotArticleList() {
        // 查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 必须是正式文章
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        // 按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        // 最多只能查询出十条消息（分页）
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();
//        List<HotArticleVo> articleVos = new ArrayList<>();
//        // 硬拷贝
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(articles, vo);
//            articleVos.add(vo);
//
//        }

        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }


    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //如果有传categoryId 就要求查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);

        //状态是正式发布的文章
        lambdaQueryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);

        //对isTop进行排序 置顶的文章要显示在最前面
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);


        List<Article> articles = page.getRecords();
        //查询categoryId(两个表联查)
        //stream流
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

        //articleId去查询articleName进行设置

//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//
//        }

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //转换成Vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名称
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category != null){
            articleDetailVo.setCategoryName(category.getName());
        }

        //封装响应返回

        return ResponseResult.okResult(articleDetailVo);
    }
}



















