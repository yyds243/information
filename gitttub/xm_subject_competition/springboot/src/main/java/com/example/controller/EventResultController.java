package com.example.controller;

import com.example.common.Result;
import com.example.entity.EventResult;
import com.example.service.EventResultService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 赛事结果前端操作接口
 **/
@RestController
@RequestMapping("/eventResult")
public class EventResultController {

    @Resource
    private EventResultService eventResultService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody EventResult eventResult) {
        eventResultService.add(eventResult);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        eventResultService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        eventResultService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody EventResult eventResult) {
        eventResultService.updateById(eventResult);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        EventResult eventResult = eventResultService.selectById(id);
        return Result.success(eventResult);
    }

    @GetMapping("/selectUserResult")
    public Result selectUserResult(@RequestParam Integer userId, @RequestParam Integer eventId) {
        EventResult eventResult = eventResultService.selectUserResult(userId, eventId);
        return Result.success(eventResult);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(EventResult eventResult) {
        List<EventResult> list = eventResultService.selectAll(eventResult);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(EventResult eventResult,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<EventResult> page = eventResultService.selectPage(eventResult, pageNum, pageSize);
        return Result.success(page);
    }

}