package com.westriver.common;

/**
 * Created by zack on 2018/10/22.
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLW_ADMIN = 1;
    }

    public interface Cart {
        boolean CHECKED = true;//即购物车选中状态
        boolean UN_CHECKED = false;//购物车中未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }
}
