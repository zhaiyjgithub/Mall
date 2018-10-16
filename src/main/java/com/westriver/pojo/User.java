package com.westriver.pojo;

import java.io.Serializable;

/**
 * Created by zack on 2018/10/15.
 */
public class User implements Serializable{
    private int id;
    private String name;
    private String account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
