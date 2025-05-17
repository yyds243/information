package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.EventResult;
import com.example.entity.Sign;
import com.example.exception.CustomException;
import com.example.mapper.EventResultMapper;
import com.example.mapper.SignMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 赛事报名业务处理
 **/
@Service
public class SignService {

    @Resource
    private SignMapper signMapper;
    @Resource
    EventResultMapper eventResultMapper;

    /**
     * 新增
     */
    public void add(Sign sign) {
        Sign dbSign = signMapper.selectUserSign(sign.getUserId(), sign.getEventId());
        if (dbSign != null && (dbSign.getStatus().equals("待审核") || dbSign.getStatus().equals("通过"))) {
            throw new CustomException("500", "您已报名过该此赛");
        }
        sign.setTime(DateUtil.now());
        signMapper.insert(sign);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        signMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            signMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Sign sign) {
        signMapper.updateById(sign);
    }

    /**
     * 根据ID查询
     */
    public Sign selectById(Integer id) {
        return signMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Sign> selectAll(Sign sign) {
        return signMapper.selectAll(sign);
    }

    /**
     * 分页查询
     */
    public PageInfo<Sign> selectPage(Sign sign, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Sign> list = signMapper.selectAll(sign);
        Account currentUser = TokenUtils.getCurrentUser();
        for (Sign s : list) {
            EventResult eventResult = eventResultMapper.selectUserResult(currentUser.getId(), s.getEventId());
            s.setResult(eventResult != null);
        }
        return PageInfo.of(list);
    }

    public Sign selectUserSign(Integer userId, Integer eventId) {
        return signMapper.selectUserSign(userId, eventId);
    }

}