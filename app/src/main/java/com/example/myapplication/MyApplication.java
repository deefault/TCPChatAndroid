package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class MyApplication extends Application {
    private static Context context;
    public TCPService service;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        Intent intent = new Intent(this,TCPService.class);
        startService(intent);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
