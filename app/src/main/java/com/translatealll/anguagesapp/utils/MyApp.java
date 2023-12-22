package com.translatealll.anguagesapp.utils;

import android.app.Application;
import android.content.Context;


public class MyApp extends Application {


    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        PrefFile.getInstance().init(getApplicationContext());
    }


}