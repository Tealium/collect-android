package com.tealium.kitchensink.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AudienceAttribute;
import com.tealium.collect.attribute.BadgeAttribute;
import com.tealium.collect.attribute.DateAttribute;
import com.tealium.collect.attribute.FlagAttribute;
import com.tealium.collect.attribute.MetricAttribute;
import com.tealium.collect.attribute.PropertyAttribute;
import com.tealium.collect.visitor.VisitorProfile;
import com.tealium.kitchensink.model.Model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class TMSHelper {

    private static final String TAG = "ASApp";

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    private TMSHelper() {
    }

    public static void setup(Application app) {

        Model model = new Model(app);

        if (TealiumCollect.isEnabled()) {
            TealiumCollect.disable();
        }

        TealiumCollect.enable(new TealiumCollect.Config(
                app,
                model.getAccountName(),
                model.getProfileName(),
                model.getEnvironmentName())
                .setLogLevel(Log.VERBOSE));

        TealiumCollect.getEventListeners().add(new TealiumCollect.OnProfileUpdatedListener() {
            @Override
            public void onProfileUpdated(VisitorProfile oldVisitorProfile, VisitorProfile newVisitorProfile) {
                try {
                    Log.v(TAG, new JSONObject(newVisitorProfile.getSource()).toString(4));
                } catch (Throwable t) {
                    Log.e(TAG, null, t);
                }
            }
        });

        TealiumCollect.getEventListeners().add(new TealiumCollect.OnAudienceUpdateListener() {
            @Override
            public void onAudienceUpdate(AudienceAttribute oldAudience, AudienceAttribute newAudience) {
                Log.d(TAG, String.format(Locale.ROOT, "Audience: old=%s; new=%s", oldAudience, newAudience));
            }
        });

        TealiumCollect.getEventListeners().add(new TealiumCollect.OnBadgeUpdateListener() {
            @Override
            public void onBadgeUpdate(BadgeAttribute oldBadge, BadgeAttribute newBadge) {
                Log.d(TAG, String.format(Locale.ROOT, "Badge: old=%s; new=%s", oldBadge, newBadge));
            }
        });

        TealiumCollect.getEventListeners().add(new TealiumCollect.OnDateUpdateListener() {
            @Override
            public void onDateUpdate(DateAttribute oldDate, DateAttribute newDate) {
                Log.d(TAG, String.format(Locale.ROOT, "Date: old=%s; new=%s", oldDate, newDate));
            }
        });

        TealiumCollect.getEventListeners().add(new TealiumCollect.OnFlagUpdateListener() {
            @Override
            public void onFlagUpdate(FlagAttribute oldFlag, FlagAttribute newFlag) {
                Log.d(TAG, String.format(Locale.ROOT, "Flag: old=%s; new=%s", oldFlag, newFlag));
            }
        });

        TealiumCollect.getEventListeners().add(new TealiumCollect.OnMetricUpdateListener() {
            @Override
            public void onMetricUpdate(MetricAttribute oldMetric, MetricAttribute newMetric) {
                Log.d(TAG, String.format(Locale.ROOT, "Metric: old=%s; new=%s", oldMetric, newMetric));
            }
        });

        TealiumCollect.getEventListeners().add(new TealiumCollect.OnPropertyUpdateListener() {
            @Override
            public void onPropertyUpdate(PropertyAttribute oldProperty, PropertyAttribute newProperty) {
                Log.d(TAG, String.format(Locale.ROOT, "Property: old=%s; new=%s", oldProperty, newProperty));
            }
        });
    }

    public static void onResume(Activity activity) {
        Map<String, String> data = new HashMap<>(1);
        data.put("activity", activity.getClass().getName());

        TealiumCollect.sendView(data);
    }

    public static void onPause(Activity activity) {

    }

    public static void trackEvent(String... args) {

        if (args == null || args.length % 2 != 0) {
            throw new IllegalArgumentException("Args must be key-value pairs.");
        }

        final int count = args.length / 2;

        Map<String, String> data = new HashMap<>(count);

        for (int i = 0; i <= count; i += 2) {


            data.put(args[i], args[i + 1]);
        }

        TealiumCollect.sendEvent(data);
    }

    public static final class Key {
        private Key() {
        }

        public static final String EVENT = "event";
        public static final String EVENT_ITEM = "event_item";
    }
}
