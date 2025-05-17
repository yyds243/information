package com.example.mapper;

import com.example.entity.EventResult;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作event_result相关数据接口
*/
public interface EventResultMapper {

    /**
      * 新增
    */
    int insert(EventResult eventResult);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(EventResult eventResult);

    /**
      * 根据ID查询
    */
    EventResult selectById(Integer id);

    /**
      * 查询所有
    */
    List<EventResult> selectAll(EventResult eventResult);

    @Select("select * from event_result where user_id = #{userId} and event_id = #{eventId}")
    EventResult selectUserResult(@Param("userId") Integer userId, @Param("eventId") Integer eventId);

}