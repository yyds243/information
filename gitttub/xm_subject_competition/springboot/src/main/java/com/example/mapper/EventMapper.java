package com.example.mapper;

import com.example.entity.Event;
import java.util.List;

/**
 * 操作event相关数据接口
*/
public interface EventMapper {

    /**
      * 新增
    */
    int insert(Event event);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Event event);

    /**
      * 根据ID查询
    */
    Event selectById(Integer id);

    /**
      * 查询所有
    */
    List<Event> selectAll(Event event);

}