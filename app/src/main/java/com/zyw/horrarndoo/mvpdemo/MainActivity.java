package com.zyw.horrarndoo.mvpdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zyw.horrarndoo.mvpdemo.mvc.MvcActivity;
import com.zyw.horrarndoo.mvpdemo.mvp.MvpActivity;
import com.zyw.horrarndoo.mvpdemo.mvpNew.MvpNewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_go_mvc_activity, R.id.btn_go_mvp_activity, R.id.btn_go_new_mvp_activity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go_mvc_activity:
                startActivity(new Intent(MainActivity.this, MvcActivity.class));
                break;
            case R.id.btn_go_mvp_activity:
                startActivity(new Intent(MainActivity.this, MvpActivity.class));
                break;
            case R.id.btn_go_new_mvp_activity:
                startActivity(new Intent(MainActivity.this, MvpNewActivity.class));
                break;
        }
    }
}
