package com.example.controller;

import com.example.common.Result;
import com.example.entity.Sign;
import com.example.service.SignService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 赛事报名前端操作接口
 **/
@RestController
@RequestMapping("/sign")
public class SignController {

    @Resource
    private SignService signService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Sign sign) {
        signService.add(sign);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        signService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        signService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Sign sign) {
        signService.updateById(sign);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Sign sign = signService.selectById(id);
        return Result.success(sign);
    }

    @GetMapping("/selectUserSign")
    public Result selectUserSign(@RequestParam Integer userId, @RequestParam Integer eventId) {
        Sign sign = signService.selectUserSign(userId, eventId);
        return Result.success(sign);
    }


    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Sign sign) {
        List<Sign> list = signService.selectAll(sign);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Sign sign,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Sign> page = signService.selectPage(sign, pageNum, pageSize);
        return Result.success(page);
    }

}