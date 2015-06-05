package com.tealium.collect;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import com.tealium.collect.visitor.Processor;
import com.tealium.collect.visitor.VisitorProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Fetches the profile based on the Infrequent profile fetch pattern.
 * <p/>
 * 1. Waits for the first POST.
 * 2. GETS Visitor Profile to prime Visitor Service
 * 3. Wait 10 seconds
 * 4. GETS Visitor Profile expecting populated JSON
 */
final class ProfileRetriever implements
        Events.OnDispatchSentListener,
        Util.Network.OnHttpResponseListener {

    private static final long PRIMING_DELAY = 10000;

    private final Context context;
    private final String visitorProfileAddress;
    private final AtomicLong lastFetch;
    private final AtomicBoolean isFetching;

    ProfileRetriever(TealiumCollect.Config config) {

        this.context = config.getContext();
        this.lastFetch = new AtomicLong(0);
        this.isFetching = new AtomicBoolean(false);

        this.visitorProfileAddress = new Uri.Builder()
                .scheme(config.isHttpsEnabled() ? "https" : "http")
                .authority("visitor-service.tealiumiq.com")
                .appendPath(config.getAccountName())
                .appendPath(config.getOverrideProfile() == null ? "main" : config.getOverrideProfile())
                .appendPath(TealiumCollect.getVisitorId())
                .build().toString();
    }

    @Override
    public void onDispatchSent(JSONObject data) {
        //If a dispatch was sent, there must be connectivity.

        final boolean hasListeners = TealiumCollect.getEventListeners().size() > 0;

        if (!hasListeners || !this.isFetching.compareAndSet(false, true)) {
            if (Constant.DEBUG) {
                if (!hasListeners) {
                    Log.v(Constant.DEBUG_TAG, "# No listeners, no need to fetch profile.");
                } else {
                    Log.v(Constant.DEBUG_TAG, "# Is currently fetching profile...");
                }
            }
            return;
        }

        final long delta = SystemClock.uptimeMillis() - this.lastFetch.get();
        final long offset;

        if (delta > PRIMING_DELAY) {
            // been longer than 10 seconds, need to prime.
            Util.Network.performRequest(null, this.visitorProfileAddress, offset = 0);
        } else {
            // Allow for at least 10 seconds to elapse.
            offset = delta;
        }

        // Fetch (Ensure after 10s)
        Util.Network.performRequest(this, this.visitorProfileAddress, offset + PRIMING_DELAY);

        Logger.v("Fetching visitor profile from " + this.visitorProfileAddress);
    }

    @Override
    public void onHttpResponse(String url, String method, int status, String entity) {

        this.isFetching.set(false);

        // "" or "{}" are not valid profiles
        if (entity == null || entity.length() <= 2) {
            Logger.e("Retrieved bad visitor profile: " + entity);
            return;
        }

        this.lastFetch.set(SystemClock.uptimeMillis());

        VisitorProfile newVisitorProfile = null;
        try {
            newVisitorProfile = VisitorProfile.fromJSON(entity);
        } catch (JSONException e) {
            Logger.e(e);
        }

        final VisitorProfile oldVisitorProfile = TealiumCollect.getCachedVisitorProfile();
        final boolean isNotSameProfile = oldVisitorProfile == null || !oldVisitorProfile.equals(newVisitorProfile);

        if (isNotSameProfile && newVisitorProfile != null) {

            if (Constant.DEBUG) {
                Log.v(Constant.DEBUG_TAG, "! Detected profile update: " + newVisitorProfile.toString());
            }

            // Changes have occurred.

            SharedPreferences sp = this.context.getSharedPreferences(Constant.SP.NAME, 0);

            // Save to memory.
            sp.edit().putString(Constant.SP.KEY_PROFILE, entity).commit();
            TealiumCollect.setCachedVisitorProfile(newVisitorProfile);
            Processor.process(oldVisitorProfile, newVisitorProfile);
        } else if (Constant.DEBUG) {
            Logger.i("Visitor profile matched existing profile.");
        }
    }

    @Override
    public void onHttpError(String url, Throwable t) {
        Logger.e("Error with " + url, t);
        this.isFetching.set(false);
    }
}


