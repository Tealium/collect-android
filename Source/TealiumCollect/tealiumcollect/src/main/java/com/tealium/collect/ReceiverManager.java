package com.tealium.collect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import java.security.InvalidParameterException;

final class ReceiverManager implements Events.OnDisableListener {

    private static final String ACTION_TRACE = "tealium.audiencestream.action.TRACE";
    private static final String EXTRA_TRACE_ID = "trace_id";

    private final BroadcastReceiver battChangedReceiver;
    private final BroadcastReceiver traceReceiver;
    private final Context context;

    private boolean isRegistered;

    ReceiverManager(TealiumCollect.Config config) {
        this.battChangedReceiver = new BattChangeReceiver();
        this.traceReceiver = createTraceReceiver();
        this.isRegistered = false;
        this.context = config.getContext();
    }

    public ReceiverManager register() {
        if (!this.isRegistered) {

            battChangedReceiver.onReceive(
                    this.context,
                    this.context.registerReceiver(
                            battChangedReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)));
            this.context.registerReceiver(this.traceReceiver, new IntentFilter(ACTION_TRACE));

            this.isRegistered = true;
        } else {
            throw new IllegalStateException("register called when already registered.");
        }

        return this;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    @Override
    public void onDisable() {
        if (this.isRegistered) {
            this.context.unregisterReceiver(this.battChangedReceiver);
            this.context.unregisterReceiver(this.traceReceiver);
            this.isRegistered = false;
        } else {
            throw new IllegalStateException("disable called when already registered.");
        }
    }

    private static BroadcastReceiver createTraceReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.hasExtra(EXTRA_TRACE_ID)) {
                        final String traceId = intent.getStringExtra(EXTRA_TRACE_ID);
                        if (!traceId.matches("^[a-zA-Z0-9]*$")) {
                            throw new InvalidParameterException('"' + traceId + "\" is not a valid trace id.");
                        }
                        EventBus.submit(Events.createTraceUpdateEvent(traceId));
                        Logger.d("Joining trace \"" + traceId + '"');
                    } else {
                        EventBus.submit(Events.createTraceUpdateEvent(null));
                        Logger.d("Leaving trace.");
                    }
                } catch (Throwable t) {
                    Logger.e(t);
                }
            }
        };
    }

    private static class BattChangeReceiver extends BroadcastReceiver {
        private boolean isBatteryLow;

        BattChangeReceiver() {
            this.isBatteryLow = false;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if (level > -1 && scale > -1) {
                int percent = Math.round((((float) level) / ((float) scale)) * 100);
                final boolean isBatteryNowLow = percent <= 15;
                if (isBatteryNowLow ^ this.isBatteryLow) {
                    this.isBatteryLow = isBatteryNowLow;
                    // Change occurred
                    EventBus.submit(Events.createBatteryUpdateEvent(this.isBatteryLow));
                }
            } else {
                // should never happen.
                this.isBatteryLow = false;
                EventBus.submit(Events.createBatteryUpdateEvent(false));
            }
        }
    }
}
