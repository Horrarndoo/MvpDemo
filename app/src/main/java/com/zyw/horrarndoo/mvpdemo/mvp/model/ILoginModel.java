package com.zyw.horrarndoo.mvpdemo.mvp.model;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public interface ILoginModel {
    void requestLogin(String userName, String userPassword, LoginModel.LoginListener loginListener);
}
