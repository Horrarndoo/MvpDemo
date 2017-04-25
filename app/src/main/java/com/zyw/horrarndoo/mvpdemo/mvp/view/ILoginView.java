package com.zyw.horrarndoo.mvpdemo.mvp.view;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public interface ILoginView {
    void showLoginSuccess();
    void showLoginFailed();
    void showDialog();
    void hideDialog();
    String getUserName();
    String getUserPassword();
}
