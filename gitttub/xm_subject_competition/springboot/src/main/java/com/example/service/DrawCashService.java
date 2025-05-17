package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.DrawCash;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.DrawCashMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提现记录业务处理
 **/
@Service
public class DrawCashService {

    @Resource
    private DrawCashMapper drawCashMapper;
    @Resource
    UserService userService;

    /**
     * 新增
     */
    @Transactional
    public void add(DrawCash drawCash) {
        Integer userId = drawCash.getUserId();
        User user = userService.selectById(userId);
        if (user.getAccount().compareTo(drawCash.getMoney()) < 0) {
            throw new CustomException("500", "您的余额不足");
        }
        user.setAccount(user.getAccount().subtract(drawCash.getMoney()));
        userService.updateById(user);
        drawCash.setTime(DateUtil.now());
        drawCashMapper.insert(drawCash);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        drawCashMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            drawCashMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(DrawCash drawCash) {
        drawCashMapper.updateById(drawCash);
    }

    /**
     * 根据ID查询
     */
    public DrawCash selectById(Integer id) {
        return drawCashMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<DrawCash> selectAll(DrawCash drawCash) {
        return drawCashMapper.selectAll(drawCash);
    }

    /**
     * 分页查询
     */
    public PageInfo<DrawCash> selectPage(DrawCash drawCash, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DrawCash> list = drawCashMapper.selectAll(drawCash);
        return PageInfo.of(list);
    }

}