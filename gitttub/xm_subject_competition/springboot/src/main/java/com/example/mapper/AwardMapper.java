package com.example.mapper;

import com.example.entity.Award;
import java.util.List;

/**
 * 操作award相关数据接口
*/
public interface AwardMapper {

    /**
      * 新增
    */
    int insert(Award award);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Award award);

    /**
      * 根据ID查询
    */
    Award selectById(Integer id);

    /**
      * 查询所有
    */
    List<Award> selectAll(Award award);

}