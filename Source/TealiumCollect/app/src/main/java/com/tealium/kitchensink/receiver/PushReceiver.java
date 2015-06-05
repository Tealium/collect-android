package com.tealium.kitchensink.receiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.tealium.kitchensink.App;
import com.tealium.kitchensink.service.PushIntentService;

public class PushReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that PushIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                PushIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
