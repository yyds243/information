package com.example.controller;

import com.example.common.Result;
import com.example.entity.Carousel;
import com.example.service.CarouselService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 前端操作接口
 **/
@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Resource
    private CarouselService carouselService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Carousel carousel) {
        carouselService.add(carousel);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        carouselService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        carouselService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Carousel carousel) {
        carouselService.updateById(carousel);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Carousel carousel = carouselService.selectById(id);
        return Result.success(carousel);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Carousel carousel) {
        List<Carousel> list = carouselService.selectAll(carousel);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Carousel carousel,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Carousel> page = carouselService.selectPage(carousel, pageNum, pageSize);
        return Result.success(page);
    }

}