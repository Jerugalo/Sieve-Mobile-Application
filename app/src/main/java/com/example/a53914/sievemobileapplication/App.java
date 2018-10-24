package com.example.a53914.sievemobileapplication;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static App mApp = null;
    /* (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        mApp = this;
    }
    public static Context context()
    {
        return mApp.getApplicationContext();
    }
}
