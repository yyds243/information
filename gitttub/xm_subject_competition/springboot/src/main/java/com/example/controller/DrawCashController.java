package com.example.controller;

import com.example.common.Result;
import com.example.entity.DrawCash;
import com.example.service.DrawCashService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 提现记录前端操作接口
 **/
@RestController
@RequestMapping("/drawCash")
public class DrawCashController {

    @Resource
    private DrawCashService drawCashService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody DrawCash drawCash) {
        drawCashService.add(drawCash);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        drawCashService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        drawCashService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody DrawCash drawCash) {
        drawCashService.updateById(drawCash);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        DrawCash drawCash = drawCashService.selectById(id);
        return Result.success(drawCash);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(DrawCash drawCash) {
        List<DrawCash> list = drawCashService.selectAll(drawCash);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(DrawCash drawCash,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<DrawCash> page = drawCashService.selectPage(drawCash, pageNum, pageSize);
        return Result.success(page);
    }

}