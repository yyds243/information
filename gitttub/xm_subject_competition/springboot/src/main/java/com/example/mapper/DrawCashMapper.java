package com.example.mapper;

import com.example.entity.DrawCash;
import java.util.List;

/**
 * 操作draw_cash相关数据接口
*/
public interface DrawCashMapper {

    /**
      * 新增
    */
    int insert(DrawCash drawCash);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(DrawCash drawCash);

    /**
      * 根据ID查询
    */
    DrawCash selectById(Integer id);

    /**
      * 查询所有
    */
    List<DrawCash> selectAll(DrawCash drawCash);

}