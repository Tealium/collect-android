package com.tealium.collect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class MPSRetriever implements
        Events.OnDispatchSentListener,
        Util.Network.OnHttpResponseListener {

    private static final long FETCH_TIMEOUT = 60L * 60L * 1000L;
    private static final String SETTINGS_VERSION = "4";

    private final SimpleDateFormat ifModifiedFormat;
    private final ConnectivityManager connectivityManager;
    private final AtomicBoolean isFetching;
    private final String mobileHtmlAddress;
    private final Pattern mpsPattern;
    private final Context context;

    private long lastFetch;
    private MPS currentMPS;

    MPSRetriever(TealiumCollect.Config config) {
        this.connectivityManager = config.getConnectivityManager();

        this.mobileHtmlAddress = new Uri.Builder()
                .scheme(config.isHttpsEnabled() ? "https" : "http")
                .authority("tags.tiqcdn.com")
                .appendPath("utag")
                .appendPath(config.getAccountName())
                .appendPath(config.getProfileName())
                .appendPath(config.getEnvironmentName())
                .appendPath("mobile.html").build().toString();

        this.mpsPattern = Pattern.compile("mps *= *(\\{\"\\w*\"\\:(?:\\{.*?\\}|\".*?\").*?\\})",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        this.context = config.getContext();
        this.ifModifiedFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ROOT);
        this.ifModifiedFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        this.currentMPS = config.getMPS();
        this.isFetching = new AtomicBoolean(false);
        this.lastFetch = 0;
    }

    @Override
    public void onHttpResponse(String url, String method, int status, String entity) {
        if (Util.Network.METHOD_HEAD.equals(method)) {
            this.processHeadRequest(status);
        } else {
            this.processMPSFetch(entity);
        }
    }

    @Override
    public void onHttpError(String url, Throwable t) {
        this.isFetching.set(false);
        Logger.e(t);
    }

    @Override
    public void onDispatchSent(JSONObject data) {

        final long delta = System.currentTimeMillis() - this.lastFetch;
        final boolean isReady = delta >= FETCH_TIMEOUT;

        if (!isReady) {
            Logger.v(String.format(Locale.ROOT, "%d ms until next fetch of latest publish settings.", FETCH_TIMEOUT - delta));
            return;
        }

        if (!Util.Connectivity.isConnected(this.connectivityManager, this.currentMPS)) {
            Logger.v("Unable to fetch publish settings: no network connection.");
            return;
        }

        if (this.isFetching.compareAndSet(false, true)) {
            if (this.lastFetch > 0) {
                final Map<String, String> headers = new HashMap<>(1);
                headers.put("If-Modified-Since",
                        this.ifModifiedFormat.format(new Date(this.lastFetch)));

                Util.Network.performRequest(this,
                        this.mobileHtmlAddress,
                        Util.Network.METHOD_HEAD,
                        headers,
                        0);
            } else {
                // App just launched
                Util.Network.performRequest(this, this.mobileHtmlAddress, 0);
            }
        }
    }

    private void processHeadRequest(int status) {
        if (status == 200) {
            Util.Network.performRequest(this, this.mobileHtmlAddress, 0);
            if (Constant.DEBUG) {
                Log.d(Constant.DEBUG_TAG, "! Head request suggests new settings, scheduling fetch...");
            }
        } else {
            this.isFetching.set(false);
            if (Constant.DEBUG) {
                Log.d(Constant.DEBUG_TAG, "! No new MPS:" + status);
            }
        }
    }

    private void processMPSFetch(String entity) {

        this.isFetching.set(false);

        try {
            if (entity == null) {
                // Should not happen.
                Logger.e(new IllegalArgumentException("entity should not be null."));
                return;
            }

            this.lastFetch = System.currentTimeMillis();

            Matcher m = this.mpsPattern.matcher(entity);
            if (m.find()) {
                JSONObject o = new JSONObject(m.group(1));
                try {
                    final JSONObject extractedMPS = o.optJSONObject(SETTINGS_VERSION);
                    final MPS downloadedMPS = new MPS(extractedMPS);

                    if (!this.currentMPS.equals(downloadedMPS)) {
                        this.currentMPS = downloadedMPS;
                        this.currentMPS.save(this.context);

                        Logger.v("Received updated Mobile Publish Settings: " + extractedMPS.toString(4));

                        EventBus.submit(Events.createMPSUpdateEvent(downloadedMPS));
                    }
                } catch (MPS.DisabledLibraryException e) {
                    EventBus.disable();
                }
            } else {
                if (Constant.DEBUG) {
                    Log.e(Constant.DEBUG_TAG, "Cannot find mps in");
                    Log.e(Constant.DEBUG_TAG, entity);
                }
                // Should not happen.
                Logger.e("Unable to retrieve MPS, using default configuration.");
            }
        } catch (JSONException e) {
            // should not happen.
            throw new RuntimeException(e);
        }
    }


}
