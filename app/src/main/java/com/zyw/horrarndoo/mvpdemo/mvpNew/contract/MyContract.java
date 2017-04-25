package com.zyw.horrarndoo.mvpdemo.mvpNew.contract;


import com.zyw.horrarndoo.mvpdemo.mvpNew.base.BasePresenter;
import com.zyw.horrarndoo.mvpdemo.mvpNew.base.IBaseModel;
import com.zyw.horrarndoo.mvpdemo.mvpNew.base.IBaseView;
import com.zyw.horrarndoo.mvpdemo.mvpNew.model.LoginModel;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public interface MyContract {
    abstract class LoginPresenter extends BasePresenter<ILoginModel, ILoginView> {
        public abstract void login();
    }

    interface ILoginModel extends IBaseModel {
        void requestLogin(String userName, String userPassword, LoginModel.LoginListener
                loginListener);
    }

    interface ILoginView extends IBaseView {
        void showLoginSuccess();
        void showLoginFailed();
        void showDialog();
        void hideDialog();
        String getUserName();
        String getUserPassword();
    }
}
