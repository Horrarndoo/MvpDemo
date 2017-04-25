package com.zyw.horrarndoo.mvpdemo.mvp.presenter;

import android.os.Handler;
import android.os.Looper;

import com.zyw.horrarndoo.mvpdemo.mvp.model.ILoginModel;
import com.zyw.horrarndoo.mvpdemo.mvp.model.LoginModel;
import com.zyw.horrarndoo.mvpdemo.mvp.view.ILoginView;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public class LoginPresenter {
    ILoginModel mILoginModel;
    ILoginView mILoginView;
    Handler mHandler = new Handler(Looper.getMainLooper());
    public LoginPresenter(ILoginView iLoginView){
        mILoginModel = new LoginModel();
        mILoginView = iLoginView;
    }

    public void login(){
        String userName = mILoginView.getUserName();
        String userPassword = mILoginView.getUserPassword();
        mILoginView.showDialog();
        mILoginModel.requestLogin(userName, userPassword, new LoginModel.LoginListener() {
            @Override
            public void loginSuccess() {
                if(mILoginView != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mILoginView.hideDialog();
                            mILoginView.showLoginSuccess();
                        }
                    });
                }
            }

            @Override
            public void loginFailed() {
                if (mILoginView != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mILoginView.hideDialog();
                            mILoginView.showLoginFailed();
                        }
                    });
                }
            }
        });
    }
}
