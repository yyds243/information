package com.example.controller;

import com.example.common.Result;
import com.example.entity.Host;
import com.example.service.HostService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 管理员表前端操作接口
 **/
@RestController
@RequestMapping("/host")
public class HostController {

    @Resource
    private HostService hostService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Host host) {
        hostService.add(host);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        hostService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        hostService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Host host) {
        hostService.updateById(host);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Host host = hostService.selectById(id);
        return Result.success(host);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Host host) {
        List<Host> list = hostService.selectAll(host);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Host host,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Host> page = hostService.selectPage(host, pageNum, pageSize);
        return Result.success(page);
    }

}