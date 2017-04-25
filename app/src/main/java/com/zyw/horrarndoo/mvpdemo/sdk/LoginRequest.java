package com.zyw.horrarndoo.mvpdemo.sdk;

import android.util.Log;

/**
 * Created by Horrarndoo on 2017/4/25.
 */

public class LoginRequest {
    public static void login(String name, String password, final LoginListener loginListener) {
        Log.w("tag", "name = " + name);
        Log.w("tag", "password = " + password);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loginListener.onSuccess();
            }
        }).start();
    }

    public interface LoginListener {
        void onSuccess();

        void onFailed();
    }
}
