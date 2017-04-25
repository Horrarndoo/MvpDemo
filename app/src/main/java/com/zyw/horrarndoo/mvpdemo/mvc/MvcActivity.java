package com.zyw.horrarndoo.mvpdemo.mvc;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zyw.horrarndoo.mvpdemo.R;
import com.zyw.horrarndoo.mvpdemo.sdk.LoginRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public class MvcActivity extends AppCompatActivity {
    @BindView(R.id.tv_login_result)
    TextView tvLoginResult;

    private ProgressDialog mWaitPorgressDialog;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvc);
        ButterKnife.bind(this);
        mWaitPorgressDialog = new ProgressDialog(this);
        login();
    }

    private void login() {
        showProgressDialog("login...");
        LoginRequest.login("Horrarndoo", "mvczzzzzz", new LoginRequest.LoginListener() {
            @Override
            public void onSuccess() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvLoginResult.setText("login success.");
                        tvLoginResult.setVisibility(View.VISIBLE);
                        hideProgressDialog();
                    }
                });
            }

            @Override
            public void onFailed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvLoginResult.setText("login failed.");
                        tvLoginResult.setVisibility(View.VISIBLE);
                        hideProgressDialog();
                    }
                });
            }
        });
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
