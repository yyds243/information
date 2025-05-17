package com.example.controller;

import com.example.common.Result;
import com.example.entity.Award;
import com.example.service.AwardService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 奖项信息前端操作接口
 **/
@RestController
@RequestMapping("/award")
public class AwardController {

    @Resource
    private AwardService awardService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Award award) {
        awardService.add(award);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        awardService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        awardService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Award award) {
        awardService.updateById(award);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Award award = awardService.selectById(id);
        return Result.success(award);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Award award) {
        List<Award> list = awardService.selectAll(award);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Award award,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Award> page = awardService.selectPage(award, pageNum, pageSize);
        return Result.success(page);
    }

}