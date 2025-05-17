package com.example.service;

import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.entity.Event;
import com.example.entity.Sign;
import com.example.mapper.CollectMapper;
import com.example.mapper.EventMapper;
import com.example.mapper.SignMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 赛事信息业务处理
 **/
@Service
public class EventService {

    @Resource
    private EventMapper eventMapper;
    @Resource
    SignMapper signMapper;
    @Resource
    CollectMapper collectMapper;

    /**
     * 新增
     */
    public void add(Event event) {
        eventMapper.insert(event);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        eventMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            eventMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Event event) {
        eventMapper.updateById(event);
    }

    /**
     * 根据ID查询
     */
    public Event selectById(Integer id) {
        Event event = eventMapper.selectById(id);
        List<Sign> signList = signMapper.selectAll(null);
        event.setSignCount(signList.stream().filter(sign -> sign.getStatus().equals("通过"))
                .filter(sign -> event.getId().equals(sign.getEventId())).count());
        Account currentUser = TokenUtils.getCurrentUser();
        Collect collect = collectMapper.selectByUserIdAndEventId(currentUser.getId(), id);
        event.setUserCollect(collect != null);
        return event;
    }

    /**
     * 查询所有
     */
    public List<Event> selectAll(Event event) {
        return eventMapper.selectAll(event);
    }

    /**
     * 分页查询
     */
    public PageInfo<Event> selectPage(Event event, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Event> list = eventMapper.selectAll(event);
        return PageInfo.of(list);
    }

    public List<Event> selectHot() {
        List<Sign> signList = signMapper.selectAll(null);
        List<Event> eventList = eventMapper.selectAll(null).stream().filter(event -> event.getStatus().equals("未开始")).toList();
        for (Event event : eventList) {
            event.setSignCount(signList.stream().filter(sign -> sign.getStatus().equals("通过"))
                    .filter(sign -> event.getId().equals(sign.getEventId())).count());
        }
        eventList = eventList.stream().sorted(Comparator.comparing(Event::getSignCount, Comparator.reverseOrder())).limit(4).toList();
        return eventList;
    }

}