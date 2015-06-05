package com.tealium.kitchensink;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.tealium.kitchensink.helper.TMSHelper;
import com.tealium.kitchensink.util.Constant;

public class App extends Application {

    private BroadcastReceiver pushBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        this.pushBroadcastReceiver = createPushBroadcastReceiver();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                this.pushBroadcastReceiver,
                new IntentFilter(Constant.LocalBroadcast.ACTION_PUSH_RECEIVED));

        TMSHelper.setup(this);
    }

    @Override
    public void onTerminate() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.pushBroadcastReceiver);
        super.onTerminate();
    }

    private BroadcastReceiver createPushBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String message = intent.getStringExtra(Constant.LocalBroadcast.EXTRA_MESSAGE);
                if (message != null) {
                    Toast.makeText(App.this, message, Toast.LENGTH_LONG).show();
                }
            }
        };
    }
}
