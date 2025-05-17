package com.example.mapper;

import com.example.entity.Sign;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作sign相关数据接口
*/
public interface SignMapper {

    /**
      * 新增
    */
    int insert(Sign sign);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Sign sign);

    /**
      * 根据ID查询
    */
    Sign selectById(Integer id);

    /**
      * 查询所有
    */
    List<Sign> selectAll(Sign sign);

    @Select("select * from sign where user_id = #{userId} and event_id = #{eventId} and status != '已取消'")
    Sign selectUserSign(@Param("userId") Integer userId, @Param("eventId") Integer eventId);
}