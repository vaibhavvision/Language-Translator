package com.translatealll.anguagesapp.database;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public final class ExCommon {


    public static boolean isOnline(Context context) {
        Object systemService = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) systemService).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
