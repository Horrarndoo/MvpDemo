package com.zyw.horrarndoo.mvpdemo.mvpNew;

import android.view.View;
import android.widget.TextView;

import com.zyw.horrarndoo.mvpdemo.R;
import com.zyw.horrarndoo.mvpdemo.mvpNew.base.BaseMvpActivity;
import com.zyw.horrarndoo.mvpdemo.mvpNew.contract.MyContract;
import com.zyw.horrarndoo.mvpdemo.mvpNew.presenter.LoginPresenter;

import butterknife.BindView;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public class MvpNewActivity extends BaseMvpActivity<MyContract.LoginPresenter> implements
        MyContract.ILoginView {

    @BindView(R.id.tv_login_result)
    TextView tvLoginResult;

    @Override
    protected int getlayoutId() {
        return R.layout.activity_mvp_new;
    }

    @Override
    public void setPresenter() {
        mPresenter = new LoginPresenter();
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.login();
    }

    @Override
    public void showLoginSuccess() {
        tvLoginResult.setText("login success.");
        tvLoginResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoginFailed() {
        tvLoginResult.setText("login failed.");
        tvLoginResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialog() {
        showProgressDialog("login...");
    }

    @Override
    public void hideDialog() {
        hideProgressDialog();
    }

    @Override
    public String getUserName() {
        return "Horrarndoo";
    }

    @Override
    public String getUserPassword() {
        return "mvpzzzzzz";
    }
}
