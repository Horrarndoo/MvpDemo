package com.zyw.horrarndoo.mvpdemo.mvp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zyw.horrarndoo.mvpdemo.R;
import com.zyw.horrarndoo.mvpdemo.mvp.presenter.LoginPresenter;
import com.zyw.horrarndoo.mvpdemo.mvp.view.ILoginView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public class MvpActivity extends AppCompatActivity implements ILoginView{
    @BindView(R.id.tv_login_result)
    TextView tvLoginResult;

    LoginPresenter mPresenter;
    private ProgressDialog mWaitPorgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        ButterKnife.bind(this);
        mWaitPorgressDialog = new ProgressDialog(this);
        mPresenter = new LoginPresenter(this);
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

    private void showProgressDialog(String msg) {
        mWaitPorgressDialog.setMessage(msg);
        mWaitPorgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mWaitPorgressDialog != null) {
            mWaitPorgressDialog.dismiss();
        }
    }
}
