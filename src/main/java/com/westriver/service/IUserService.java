package com.westriver.service;

import com.westriver.common.ServerResponse;

/**
 * Created by zack on 2018/10/18.
 */
public interface IUserService {
    ServerResponse login(String userName, String password);
}
