package com.westriver.controller;

import com.westriver.common.ServerResponse;
import com.westriver.pojo.User;
import com.westriver.service.Impl.IUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zack on 2018/10/15.
 */

@RestController
public class UserController {
    @Autowired
    private IUserServiceImpl iUserServiceImpl;


    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ServerResponse  <User> login(String userName, String password) {
        ServerResponse<User> response = iUserServiceImpl.login(userName, password);

        return  response;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ServerResponse<String> register(User user) {
        return iUserServiceImpl.register(user);
    }

    @RequestMapping(value = "check_valid", method = RequestMethod.POST)
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserServiceImpl.checkValid(str, type);
    }


}
