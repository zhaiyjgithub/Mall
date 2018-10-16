package com.westriver.controller;

import com.westriver.pojo.User;
import com.westriver.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zack on 2018/10/15.
 */

@RestController
public class UserController {
    @Autowired
    private UserServiceImp serviceImp;

    @RequestMapping("/list")
    public List<User> listUser() {
         return serviceImp.list();
    }
}
