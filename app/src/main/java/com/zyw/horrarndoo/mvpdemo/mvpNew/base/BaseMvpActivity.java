package com.zyw.horrarndoo.mvpdemo.mvpNew.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public abstract class BaseMvpActivity<P extends BasePresenter> extends
        AppCompatActivity implements IBaseView {

    protected ProgressDialog mWaitPorgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getlayoutId());
        ButterKnife.bind(this);
        initData();
    }

    protected abstract int getlayoutId();

    /**
     * presenter 具体的presenter由子类确定
     */
    protected P mPresenter;

    /**
     * 初始化数据
     * <p>
     * 子类可以复写此方法初始化子类数据
     */
    protected void initData() {
        mWaitPorgressDialog = new ProgressDialog(this);
        if (this instanceof IBaseView) {
            this.setPresenter();
            if (mPresenter != null) {
                mPresenter.attachMV(this);
                Log.d("tag", "attach M V success.");
            }
        } else {
            Log.e("tag", "attach M V failed.");
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachMV();
            Log.d("tag", "detach M V success.");
        }
        super.onDestroy();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showProgressDialog(String msg) {
        mWaitPorgressDialog.setMessage(msg);
        mWaitPorgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mWaitPorgressDialog != null) {
            mWaitPorgressDialog.dismiss();
        }
    }
}
