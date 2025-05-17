package com.example.mapper;

import com.example.entity.Host;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作host相关数据接口
*/
public interface HostMapper {

    /**
      * 新增
    */
    int insert(Host host);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Host host);

    /**
      * 根据ID查询
    */
    Host selectById(Integer id);

    /**
      * 查询所有
    */
    List<Host> selectAll(Host host);

    @Select("select * from `host` where username = #{username}")
    Host selectByUsername(String username);

}