package com.tealium.collect;

import android.net.ConnectivityManager;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


final class Dispatcher implements
        Events.OnDispatchReadyListener,
        Events.OnMPSUpdateListener,
        Events.OnBatteryUpdateListener,
        Events.OnTraceUpdateListener {

    private final ConnectivityManager connectivity;
    private final String accountName;
    private final String overrideProfileName;
    private final Queue queue;
    private final String httpScheme;

    private boolean isBatteryLow;
    private String traceId;
    private MPS mps;

    Dispatcher(TealiumCollect.Config config) {

        if (config == null) {
            throw new IllegalArgumentException("config must not be null.");
        }

        this.httpScheme = config.isHttpsEnabled() ? "https" : "http";
        this.accountName = config.getAccountName();
        this.overrideProfileName = config.getOverrideProfile();
        this.connectivity = config.getConnectivityManager();
        this.queue = new Queue(config);

        EventBus.registerListener(this.queue);

        this.mps = config.getMPS();
        this.isBatteryLow = false;
    }

    private void sendDispatch(JSONObject dispatch) {
        String request = createRequest(dispatch);
        Logger.v("Sending data to " + request);
        Util.Network.performRequest(null, request, 0);
        EventBus.submit(Events.createDispatchSentEvent(dispatch));
    }

    private void queueDispatch(JSONObject dispatch) {

        if (!dispatch.has(Key.WAS_QUEUED)) {
            try {
                dispatch.put(Key.WAS_QUEUED, Boolean.toString(true));
            } catch (JSONException e) {
                throw new RuntimeException(e);// Should never happen.
            }
        }

        this.queue.enqueueDispatch(dispatch);

        EventBus.submit(Events.createDispatchQueuedEvent(dispatch));
    }

    private String createRequest(JSONObject dispatch) {

        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme(this.httpScheme)
                .authority("datacloud.tealiumiq.com")
                .appendPath("vdata")
                .appendPath("i.gif")
                .appendQueryParameter("tealium_vid", TealiumCollect.getVisitorId())
                .appendQueryParameter("tealium_account", this.accountName)
                .appendQueryParameter("tealium_profile",
                        this.overrideProfileName == null ? "main" : this.overrideProfileName);

        if (this.traceId != null) {
            uriBuilder.appendQueryParameter("tealium_trace_id", this.traceId);
        }

        Iterator<String> keys = dispatch.keys();
        String key;
        while (keys.hasNext()) {
            key = keys.next();
            uriBuilder.appendQueryParameter(key, dispatch.optString(key, ""));
        }

        return uriBuilder.build().toString();
    }

    @Override
    public void onDispatchReady(JSONObject data, boolean straightToQueue) {

        if (data == null) {
            throw new IllegalArgumentException("eventName and data must not be null.");
        }

        final boolean isOverBatchLimit = this.queue.getDispatchCount() + 1 >= this.mps.getEventBatchSize();
        final boolean isConnected = Util.Connectivity.isConnected(this.connectivity, this.mps);
        final boolean isBatteryTooLow = this.isBatteryLow && this.mps.isBatterySaver();

        if (!isConnected || !isOverBatchLimit || straightToQueue || isBatteryTooLow) {
            this.queueDispatch(data);
            return;
        }

        this.sendDispatch(data);

        for (JSONObject storedDispatch : this.queue.dequeueDispatches()) {
            this.sendDispatch(storedDispatch);
        }
    }

    @Override
    public void onMPSUpdate(MPS newMPS) {
        this.mps = newMPS;
    }

    @Override
    public void onBatteryUpdate(boolean isBatteryLow) {
        this.isBatteryLow = isBatteryLow;
    }

    @Override
    public void onTraceUpdate(String traceId) {
        this.traceId = traceId;
    }
}
