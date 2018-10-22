package com.westriver.service.Impl;

import com.westriver.common.Const;
import com.westriver.common.ServerResponse;
import com.westriver.dao.UserMapper;
import com.westriver.pojo.User;
import com.westriver.service.IUserService;
import com.westriver.util.MD5Util;
import org.apache.catalina.ssi.ResponseIncludeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.locks.Condition;


/**
 * Created by zack on 2018/10/18.
 */

@Service("iUserService")
public class IUserServiceImpl implements IUserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String userName, String password) {
        if (userMapper.checkUserName(userName) == 0) {
            return ServerResponse.createByErrorCodeMessage(1, "用户不存在");
        }

        User user = userMapper.selectLogin(userName, password);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(1, "账号或密码不正确");
        }


        user.setPassword("");
        return ServerResponse.createBySuccess(user);
    }

    public ServerResponse<String> register(User user) {
        ServerResponse checkResponse = checkValid(user.getUsername(), user.getEmail());
        if (!checkResponse.isSuccess()) {

            return checkResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER); //设置角色
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int insertCount = userMapper.insert(user);
        if (insertCount == 0 ) {
            ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccess("注册成功");
    }

    public ServerResponse<String> checkValid(String str, String type) {
        if (!StringUtils.isEmpty(str)) {
            if (Const.USERNAME.equals(type)) {
                if (userMapper.checkUserName(str) != 0) {
                    return ServerResponse.createByErrorMessage("用户已经存在");
                }
            }

            if (Const.EMAIL.equals(str)) {
                if (userMapper.checkEmail(str) != 0) {
                    return ServerResponse.createByErrorMessage("邮箱已经被注册");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("验证成功");
    }

    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse response = this.checkValid(username, Const.USERNAME);

        if (!response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String question = userMapper.selectQuestion(username);

        if (!StringUtils.isEmpty(question)) {
            return ServerResponse.createByErrorMessage("没有设置问题");
        }

        return ServerResponse.createBySuccess(question);
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int answerCount = userMapper.checkAnswer(username, question, answer);

        if (answerCount == 0) {
            return ServerResponse.createByErrorMessage("答案错误");
        }

        String token = ""; //验证通过后，发送访问令牌token到客户端，允许访问特定资源
        return ServerResponse.createBySuccess(token);
    }
}
