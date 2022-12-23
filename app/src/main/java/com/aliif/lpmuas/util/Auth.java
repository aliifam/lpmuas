package com.aliif.lpmuas.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Auth {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static boolean isUserLoggedOut(Context context)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean("is_logged_in", true);
    }

    public static void setUserLoggedOut(Context context, boolean value)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putBoolean("is_logged_in", value);
        editor.commit();
    }

    public static String getUserId(Context context)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("user_id", "");
    }

    public static void setUserId(Context context, String user_id)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString("user_id", user_id);
        editor.commit();
    }

}
