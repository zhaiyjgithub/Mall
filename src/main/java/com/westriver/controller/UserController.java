package com.westriver.controller;

import com.westriver.common.Const;
import com.westriver.common.ServerResponse;
import com.westriver.pojo.User;
import com.westriver.service.Impl.IUserServiceImpl;
import com.westriver.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.tools.tree.RemainderExpression;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import java.rmi.ServerError;
import java.util.List;

/**
 * Created by zack on 2018/10/15.
 */

@RestController
public class UserController {
    @Autowired
    private IUserServiceImpl iUserServiceImpl;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ServerResponse  <User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserServiceImpl.login(username, MD5Util.MD5EncodeUtf8(password));
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

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

    @RequestMapping(value = "/select_question", method = RequestMethod.POST)
    public ServerResponse<String> selectQuestion(String username) {
        return iUserServiceImpl.selectQuestion(username);
    }

    @RequestMapping(value = "/check_answer", method = RequestMethod.POST)
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        return iUserServiceImpl.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "/reset_password", method = RequestMethod.POST)
    public ServerResponse<String> forgotResetPassword(String username, String passwordNew, String token) {
        return iUserServiceImpl.forgotResetPassword(username, passwordNew, token);
    }

    @RequestMapping(value = "/update_password", method = RequestMethod.POST)
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorMessage("请先登录");
        }

        return iUserServiceImpl.resetPassword(passwordOld, passwordNew, user);
    }
}
