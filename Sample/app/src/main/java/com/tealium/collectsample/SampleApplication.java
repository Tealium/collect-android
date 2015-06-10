package com.tealium.collectsample;

import android.app.Application;
import android.util.Log;

import com.tealium.collect.TealiumCollect;

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TealiumCollect.enable(new TealiumCollect.Config(this, "tealiummobile", "demo", "dev")
                .setHttpsEnabled(false)
                .setLogLevel(Log.VERBOSE));
    }
}
