package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.News;
import com.example.mapper.NewsMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 赛事资讯业务处理
 **/
@Service
public class NewsService {

    @Resource
    private NewsMapper newsMapper;

    /**
     * 新增
     */
    public void add(News news) {
        news.setReadCount(0);
        news.setTime(DateUtil.now());
        newsMapper.insert(news);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        newsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            newsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(News news) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRole().equals(RoleEnum.HOST.name())) {
            news.setStatus("待审核");
        }
        newsMapper.updateById(news);
    }

    /**
     * 根据ID查询
     */
    public News selectById(Integer id) {
        newsMapper.updateCountById(id);
        return newsMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<News> selectAll(News news) {
        return newsMapper.selectAll(news);
    }

    /**
     * 分页查询
     */
    public PageInfo<News> selectPage(News news, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<News> list = newsMapper.selectAll(news);
        return PageInfo.of(list);
    }

}