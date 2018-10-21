package com.westriver.service.Impl;

import com.westriver.common.ServerResponse;
import com.westriver.dao.UserMapper;
import com.westriver.pojo.User;
import com.westriver.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (checkResponse.isSuccess()) {

            int insertCount = userMapper.insert(user);
            if (insertCount == 0 ) {
                ServerResponse.createByErrorMessage("注册失败");
            }

            return ServerResponse.createBySuccess("注册成功");
        }else {
            return checkResponse;
        }
    }

    private ServerResponse<String> checkValid(String userName, String email) {
        if (userMapper.checkUserName(userName) != 0) {
            return ServerResponse.createByErrorMessage("用户名已经存在");
        }

        if (userMapper.checkEmail(email) != 0) {
            return ServerResponse.createByErrorMessage("邮箱已经被注册");
        }

        return ServerResponse.createBySuccess();
    }
}
