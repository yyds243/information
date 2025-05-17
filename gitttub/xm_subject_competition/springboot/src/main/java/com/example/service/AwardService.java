package com.example.service;

import com.example.entity.Award;
import com.example.mapper.AwardMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 奖项信息业务处理
 **/
@Service
public class AwardService {

    @Resource
    private AwardMapper awardMapper;

    /**
     * 新增
     */
    public void add(Award award) {
        awardMapper.insert(award);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        awardMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            awardMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Award award) {
        awardMapper.updateById(award);
    }

    /**
     * 根据ID查询
     */
    public Award selectById(Integer id) {
        return awardMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Award> selectAll(Award award) {
        return awardMapper.selectAll(award);
    }

    /**
     * 分页查询
     */
    public PageInfo<Award> selectPage(Award award, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Award> list = awardMapper.selectAll(award);
        return PageInfo.of(list);
    }

}