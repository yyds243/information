package com.example.service;

import com.example.entity.Award;
import com.example.entity.EventResult;
import com.example.entity.User;
import com.example.mapper.EventResultMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 赛事结果业务处理
 **/
@Service
public class EventResultService {

    @Resource
    private EventResultMapper eventResultMapper;
    @Resource
    UserService userService;
    @Resource
    AwardService awardService;

    /**
     * 新增
     */
    public void add(EventResult eventResult) {
        if ("已获奖".equals(eventResult.getStatus())) {
            Integer userId = eventResult.getUserId();
            User user = userService.selectById(userId);
            Award award = awardService.selectById(eventResult.getAwardId());
            user.setAccount(user.getAccount().add(award.getMoney()));
            userService.updateById(user);
        }
        eventResultMapper.insert(eventResult);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        eventResultMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            eventResultMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(EventResult eventResult) {
        eventResultMapper.updateById(eventResult);
    }

    /**
     * 根据ID查询
     */
    public EventResult selectById(Integer id) {
        return eventResultMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<EventResult> selectAll(EventResult eventResult) {
        return eventResultMapper.selectAll(eventResult);
    }

    /**
     * 分页查询
     */
    public PageInfo<EventResult> selectPage(EventResult eventResult, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EventResult> list = eventResultMapper.selectAll(eventResult);
        return PageInfo.of(list);
    }

    public EventResult selectUserResult(Integer userId, Integer eventId) {
        return eventResultMapper.selectUserResult(userId, eventId);
    }

}