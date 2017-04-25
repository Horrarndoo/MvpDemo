package com.zyw.horrarndoo.mvpdemo.mvpNew.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zyw.horrarndoo.mvpdemo.mvpNew.contract.MyContract;
import com.zyw.horrarndoo.mvpdemo.mvpNew.model.LoginModel;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public class LoginPresenter extends MyContract.LoginPresenter {
    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void login() {
        String userName = mIView.getUserName();
        String userPassword = mIView.getUserPassword();
        mIView.showDialog();
        mIModel.requestLogin(userName, userPassword, new LoginModel.LoginListener() {
            @Override
            public void loginSuccess() {
                if(mIView != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mIView.hideDialog();
                            mIView.showLoginSuccess();
                            mIView.showToast("hahaha, this is new mvp.");
                        }
                    });
                }
            }

            @Override
            public void loginFailed() {
                if (mIView != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mIView.hideDialog();
                            mIView.showLoginFailed();
                        }
                    });
                }
            }
        });
    }

    @Override
    public LoginModel getModel() {
        return new LoginModel();
    }

    @Override
    public void onStart() {
        Log.w("tag", "LoginPresenter onStart.");
    }
}
