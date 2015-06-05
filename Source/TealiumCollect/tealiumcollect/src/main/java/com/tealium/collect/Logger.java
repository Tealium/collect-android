package com.tealium.collect;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

final class Logger implements
        Events.OnDispatchSentListener,
        Events.OnDispatchQueuedListener {

    static final int SILENT = Integer.MAX_VALUE;

    private static final String TAG = "Tealium-" + Constant.VERSION;
    private static OnLogListener listener;
    private static volatile int logLevel = Log.WARN;

    static void v(String msg) {
        if (Constant.DEBUG && listener != null) {
            listener.onLog(Log.VERBOSE, msg, null);
        }
        if (logLevel == Log.VERBOSE) {
            Log.v(TAG, msg);
        }
    }

    static void d(String msg) {
        if (Constant.DEBUG && listener != null) {
            listener.onLog(Log.DEBUG, msg, null);
        }
        if (logLevel <= Log.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    static void e(String msg) {
        if (Constant.DEBUG && listener != null) {
            listener.onLog(Log.ERROR, msg, null);
        }
        if (logLevel <= Log.ERROR) {
            Log.e(TAG, msg);
        }
    }

    static void e(String msg, Throwable t) {
        if (Constant.DEBUG && listener != null) {
            listener.onLog(Log.ERROR, msg, null);
        }
        if (logLevel <= Log.ERROR) {
            Log.e(TAG, msg, t);
        }
    }

    static void e(Throwable t) {
        if (Constant.DEBUG && listener != null) {
            listener.onLog(Log.ERROR, null, t);
        }
        if (logLevel <= Log.ERROR) {
            Log.e(TAG, "", t);
        }
    }

    static void w(String msg) {
        if (Constant.DEBUG && listener != null) {
            listener.onLog(Log.WARN, msg, null);
        }
        if (logLevel <= Log.WARN) {
            Log.w(TAG, msg);
        }
    }

    static void w(Throwable t) {
        if (Constant.DEBUG && listener != null) {
            listener.onLog(Log.WARN, null, t);
        }
        if (logLevel <= Log.WARN) {
            Log.w(TAG, "", t);
        }
    }

    static void i(String msg) {
        if (Constant.DEBUG && listener != null) {
            listener.onLog(Log.INFO, msg, null);
        }
        if (logLevel <= Log.INFO) {
            Log.i(TAG, msg);
        }
    }

    static void setLogLevel(int level) {
        if (level < Log.VERBOSE || level > Log.ASSERT) {
            logLevel = SILENT;
        } else {
            logLevel = level;
        }
    }

    static int getLogLevel() {
        return logLevel;
    }

    static void setListener(OnLogListener listener) {
        Logger.listener = listener;
    }

    @Override
    public void onDispatchQueued(JSONObject data) {
        if (logLevel > Log.DEBUG) {
            return;
        }

        Log.d(TAG, "Queued dispatch " + orderAndStringify(data));
    }

    @Override
    public void onDispatchSent(JSONObject data) {
        if (logLevel > Log.DEBUG) {
            return;
        }

        Log.d(TAG, String.format(Locale.ROOT, "Sent %s dispatch %s",
                data.optBoolean(Key.WAS_QUEUED) ? "queued" : "new",
                orderAndStringify(data)));
    }

    private static String orderAndStringify(JSONObject o) {
        try {
            Map<String, Object> map = new TreeMap<>();
            Iterator<String> keys = o.keys();
            String key;
            while (keys.hasNext()) {
                key = keys.next();
                map.put(key, o.get(key));
            }

            StringBuilder sb = new StringBuilder("{").append(Constant.NEW_LINE);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sb.append(Constant.TAB)
                        .append(entry.getKey())
                        .append(" : ")
                        .append(JSONObject.quote(entry.getValue().toString()))
                        .append(Constant.NEW_LINE);
            }
            return sb.append("}").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    interface OnLogListener {
        void onLog(int level, String msg, Throwable t);
    }
}
