package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.domain.ResponseResult;
import com.example.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-07-09 10:00:20
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}
