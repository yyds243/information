package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Host;
import com.example.entity.Host;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.HostMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 业务处理
 **/
@Service
public class HostService {

    @Resource
    private HostMapper hostMapper;

    /**
     * 新增
     */
    public void add(Host host) {
        Host dbHost = hostMapper.selectByUsername(host.getUsername());
        if (ObjectUtil.isNotNull(dbHost)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(host.getPassword())) {
            host.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(host.getName())) {
            host.setName(host.getUsername());
        }
        host.setRole(RoleEnum.HOST.name());
        hostMapper.insert(host);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        hostMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            hostMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Host host) {
        hostMapper.updateById(host);
    }

    /**
     * 根据ID查询
     */
    public Host selectById(Integer id) {
        return hostMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Host> selectAll(Host host) {
        return hostMapper.selectAll(host);
    }

    /**
     * 分页查询
     */
    public PageInfo<Host> selectPage(Host host, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Host> list = hostMapper.selectAll(host);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Host login(Account account) {
        Host dbHost = hostMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbHost)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!dbHost.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String token = TokenUtils.createToken(dbHost.getId() + "-" + dbHost.getRole(), dbHost.getPassword());
        dbHost.setToken(token);
        return dbHost;
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Host dbHost = hostMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbHost)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbHost.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbHost.setPassword(account.getNewPassword());
        hostMapper.updateById(dbHost);
    }

}