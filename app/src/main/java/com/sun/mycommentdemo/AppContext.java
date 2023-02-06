package com.sun.mycommentdemo;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }
}
