package com.translatealll.anguagesapp.multiscreen;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefFile {
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public static PrefFile prefFile;



    public PrefFile() {
    }

    public void init(Context context) {
        if (context != null) {
            appSharedPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            prefsEditor = appSharedPrefs.edit();
        }
    }

    public static synchronized PrefFile getInstance(){
        if (prefFile == null){
            prefFile = new PrefFile();
        }
        return prefFile;
    }

    public boolean checkPreferenceSet(String key_value) {
        return appSharedPrefs.contains(key_value);
    }

    public boolean getBool(String key_value) {
        return appSharedPrefs.getBoolean(key_value, false);
    }

    public void setBool(String key_value, boolean default_value) {
        prefsEditor.putBoolean(key_value, default_value).commit();
    }

    public int getInt(String key_value) {
        return appSharedPrefs.getInt(key_value, 0);
    }

    public void setInt(String key_value, int default_value) {
        prefsEditor.putInt(key_value, default_value).commit();
    }

    public String getString(String key_value, String default_value) {
        return appSharedPrefs.getString(key_value, default_value);
    }

    public String getString(String key_value) {
        return appSharedPrefs.getString(key_value, "");
    }

    public void setString(String key_value, String default_value) {
        prefsEditor.putString(key_value, default_value).commit();
    }

    public long getLong(String key_value) {
        return appSharedPrefs.getLong(key_value, -1);
    }

    public void setLong(String key_value, Long default_value) {
        prefsEditor.putLong(key_value, default_value).commit();
    }
}
