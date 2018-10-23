package com.westriver.service.Impl;

import com.westriver.common.Const;
import com.westriver.common.ServerResponse;
import com.westriver.common.TokenCache;
import com.westriver.dao.UserMapper;
import com.westriver.pojo.User;
import com.westriver.service.IUserService;
import com.westriver.util.MD5Util;
import org.apache.catalina.ssi.ResponseIncludeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerErrorException;

import java.util.UUID;
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
        ServerResponse checkUserNameResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!checkUserNameResponse.isSuccess()) {

            return checkUserNameResponse;
        }

        ServerResponse checkEmailResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!checkEmailResponse.isSuccess()) {
            return checkEmailResponse;
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

        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String question = userMapper.selectQuestion(username);

        if (org.apache.commons.lang3.StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }

        return ServerResponse.createByErrorMessage("没有设置问题");
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int answerCount = userMapper.checkAnswer(username, question, answer);

        if (answerCount == 0) {
            return ServerResponse.createByErrorMessage("答案错误");
        }

        String token = UUID.randomUUID().toString(); //验证通过后，发送访问令牌token到客户端，允许访问特定资源
        TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, token);
        return ServerResponse.createBySuccess(token);
    }

    public ServerResponse<String> forgotResetPassword(String username, String passwordNew, String token) {
        if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        ServerResponse checkUsernameResponse = this.checkValid(username, Const.USERNAME);

        if (checkUsernameResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String localToken = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (org.apache.commons.lang3.StringUtils.isBlank(localToken)) {
            return  ServerResponse.createByErrorMessage("token无效或者已经过期");
        }

        if (org.apache.commons.lang3.StringUtils.equals(localToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int updateCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (updateCount > 0) {
                return ServerResponse.createBySuccessMessage("更新成功");
            }
        }else {
            ServerResponse.createByErrorMessage("请重新登录，token不一致");
        }

        return ServerResponse.createByErrorMessage("更新失败");
    }

    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        int checkPasswordCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (checkPasswordCount == 0 ) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        String passwordNewMD5 = MD5Util.MD5EncodeUtf8(passwordNew);
        int updatePasswordCount = userMapper.updatePasswordByUserId(passwordNewMD5, user.getId());

        if (updatePasswordCount > 0) {
            return ServerResponse.createBySuccessMessage("更新密码成功");
        }

        return ServerResponse.createByErrorMessage("更新密码失败");

    }
}
