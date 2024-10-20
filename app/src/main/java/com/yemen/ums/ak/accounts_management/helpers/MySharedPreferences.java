package com.yemen.ums.ak.accounts_management.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    private static final String PREFERENCES_NAME = "AMPreferencesFile";

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_USER_LOGGED_IN = "IsUserLoggedIn";


    public static Boolean isFirstTimeLaunch(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Boolean returnValue = preferences.getBoolean(IS_FIRST_TIME_LAUNCH,true);

        return returnValue;
    }

    private static void setIsFirstTimeLaunch_false(SharedPreferences sharedPreferences){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH,false);
        editor.apply();
    }

    public static Boolean isUserLoggedIn(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Boolean returnValue = preferences.getBoolean(IS_USER_LOGGED_IN,false);
        return returnValue;
    }

    public static Boolean checkUser(Context context,String username,String password){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String theUsername = preferences.getString(USERNAME,"");
        String thePassword = preferences.getString(PASSWORD,"");

        if (username.contentEquals(theUsername) && password.contentEquals(thePassword)){
            logIn(context);
            return true ;
        }

        return false;
    }

    public static void signup(Context context,String username,String password){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(USERNAME,username).apply();
        preferences.edit().putString(PASSWORD,password).apply();

        logIn(context);
        MySharedPreferences.setIsFirstTimeLaunch_false(preferences);
    }


    public static void logIn(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(IS_USER_LOGGED_IN,true).apply();
    }

    public static void logOut(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(IS_USER_LOGGED_IN,false).apply();
    }

}
