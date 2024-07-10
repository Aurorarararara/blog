package com.example.utils;

import com.example.domain.entity.Article;
import com.example.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {

    }

    // 单个实体类的拷贝
    public static <V>V copyBean(Object source, Class<V> clazz) {
        // 创建目标对象
        V result = null;
        try {
            result = clazz.newInstance();
            // 实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回结果
        return result;
    }

    public static <O,V>List<V> copyBeanList(List<O> list, Class<V> clazz) {
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
