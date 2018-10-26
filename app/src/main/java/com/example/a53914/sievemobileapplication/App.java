package com.example.a53914.sievemobileapplication;

import android.app.Application;
import android.content.Context;

public abstract class App extends Application {
    private static App mApp = null;

    public static Context context() {
        return mApp.getApplicationContext();
    }

    /* (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }
}
