package com.example.controller;

import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.service.*;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private HostService hostService;
    @Resource
    UserService userService;
    @Resource
    TypeService typeService;
    @Resource
    SignService signService;
    @Resource
    EventService eventService;

    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello () {
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account loginAccount = null;
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            loginAccount = adminService.login(account);
        } else if (RoleEnum.HOST.name().equals(account.getRole())) {
            loginAccount = hostService.login(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            loginAccount = userService.login(account);
        }
        return Result.success(loginAccount);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if (RoleEnum.HOST.name().equals(account.getRole())) {
            hostService.updatePassword(account);
        } else if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

    @GetMapping("/selectBar")
    public Result selectBar() {
        List<Type> typeList = typeService.selectAll(null);
        List<Event> eventList = eventService.selectAll(null);
        List<Long> countList = new ArrayList<>();
        for (Type type : typeList) {
            long count = eventList.stream().filter(event -> event.getTypeId().equals(type.getId())).count();
            countList.add(count);
        }
        Dict dict = Dict.create().set("type", typeList.stream().map(Type::getName).toList()).set("count", countList);
        return Result.success(dict);
    }

    @GetMapping("/selectPie")
    public Result selectPie() {
        List<Event> eventList = eventService.selectAll(null);
        List<Sign> signList = signService.selectAll(null);
        List<Dict> dictList = new ArrayList<>();
        for (Event event : eventList) {
            long count = signList.stream().filter(sign -> sign.getStatus().equals("通过")).filter(sign -> sign.getEventId().equals(event.getId())).count();
            Dict dict = Dict.create().set("name", event.getName()).set("value", count);
            dictList.add(dict);
        }
        return Result.success(dictList);
    }

    @GetMapping("/selectCount")
    public Result selectCount() {
        List<Event> eventList = eventService.selectAll(null);
        List<Sign> signList = signService.selectAll(null);
        List<Host> hostList = hostService.selectAll(null);
        List<User> userList = userService.selectAll(null);
        Dict dict = Dict.create().set("event", eventList.size()).set("sign", signList.size())
                .set("host", hostList.size()).set("user", userList.size());
        return Result.success(dict);
    }

}
