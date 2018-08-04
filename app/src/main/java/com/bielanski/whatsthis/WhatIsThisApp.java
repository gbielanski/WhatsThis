package com.bielanski.whatsthis;

import android.app.Application;

import timber.log.Timber;

public class WhatIsThisApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

    }
}
