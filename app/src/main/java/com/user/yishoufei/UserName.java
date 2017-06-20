package com.user.yishoufei;

import org.litepal.crud.DataSupport;

/**
 * Created by User on 2017/6/20.
 */

public class UserName  extends DataSupport{
    private long  Id;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
