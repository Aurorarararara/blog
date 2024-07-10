package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.constants.SystemConstants;
import com.example.domain.ResponseResult;
import com.example.domain.entity.Link;
import com.example.domain.vo.LinkVo;
import com.example.mapper.LinkMapper;
import com.example.service.LinkService;
import com.example.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2024-07-10 11:36:38
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLinks() {
        // 查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.Link_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        //转换为vo
        List<LinkVo> LinkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(LinkVos);
    }
}
