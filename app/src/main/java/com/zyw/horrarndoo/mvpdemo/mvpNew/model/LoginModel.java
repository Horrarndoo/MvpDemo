package com.zyw.horrarndoo.mvpdemo.mvpNew.model;

import com.zyw.horrarndoo.mvpdemo.mvpNew.base.BaseModel;
import com.zyw.horrarndoo.mvpdemo.mvpNew.contract.MyContract;
import com.zyw.horrarndoo.mvpdemo.sdk.LoginRequest;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public class LoginModel extends BaseModel implements MyContract.ILoginModel {
    @Override
    public void requestLogin(String userName, String userPassword, final LoginListener loginListener) {
        LoginRequest.login(userName, userPassword, new LoginRequest.LoginListener() {
            @Override
            public void onSuccess() {
                loginListener.loginSuccess();
            }

            @Override
            public void onFailed() {
                loginListener.loginFailed();
            }
        });
    }

    public interface LoginListener {
        void loginSuccess();

        void loginFailed();
    }
}
