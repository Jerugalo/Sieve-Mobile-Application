package com.example.a53914.sievemobileapplication;

import android.app.Application;
import android.content.Context;

/**
 * Provides Context to methods that cannot create thier own Context
 */

abstract class App extends Application {
    private static App mApp = null;

    /** Saves App Context to mApp */
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    /** Returns mApp */
    public static Context context() {
        return mApp.getApplicationContext();
    }
}
