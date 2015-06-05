package com.tealium.kitchensink.service;

import android.animation.IntEvaluator;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.tealium.kitchensink.R;
import com.tealium.kitchensink.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;


public class PushIntentService extends IntentService {

    public PushIntentService() {
        super("PushIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

/*
*
* Parse "write your message"
*
* Received {
I/Tealium ( 5586): 	data:{"alert":"Test4","push_hash":"41d5e808720c8ee71257214e952a6721"}
I/Tealium ( 5586): 	from:1076345567071
I/Tealium ( 5586): 	android.support.content.wakelockid:1
I/Tealium ( 5586): 	collapse_key:do_not_collapse
I/Tealium ( 5586): }
* */


        /**
         *
         * Parse Custom JSON
         *
         * I/Tealium ( 5586): Received {
         I/Tealium ( 5586): 	data:{"message":"from parse","push_hash":"d41d8cd98f00b204e9800998ecf8427e","some":"custom"}
         I/Tealium ( 5586): 	from:1076345567071
         I/Tealium ( 5586): 	android.support.content.wakelockid:2
         I/Tealium ( 5586): 	collapse_key:do_not_collapse
         I/Tealium ( 5586): }

         *
         *
         * */

        final String data = intent.getStringExtra("data");
        int notificationId = (int) System.currentTimeMillis();
        final String title = "Tealium Kitchen Sink PUSH!";
        String message;

        if (data == null) {
            message = "Uh-oh, I seem to be missing some data...";
        } else {
            try {
                JSONObject payload = new JSONObject(data);
                message = payload.optString("alert", payload.toString());
            } catch (JSONException e) {
                message = data;
            }
        }

        Intent i = new Intent(Constant.LocalBroadcast.ACTION_PUSH_RECEIVED);
        i.putExtra(Constant.LocalBroadcast.EXTRA_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .notify(notificationId, new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .build());
    }
}
