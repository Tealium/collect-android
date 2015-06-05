package com.tealium.collect;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

final class MPS {

    static final int DEFAULT_DISPATCH_EXPIRATION = -1;
    static final int DEFAULT_OFFLINE_DISPATCH_LIMIT = -1;
    static final int DEFAULT_EVENT_BATCH_SIZE = 1;
    static final boolean DEFAULT_WIFI_ONLY_SENDING = false;
    static final boolean DEFAULT_BATTERY_SAVER = true;

    private final int dispatchExpiration;
    private final int offlineDispatchLimit;
    private final int eventBatchSize;
    private final boolean wifiOnlySending;
    private final boolean batterySaver;

    private volatile int hashCode;

    MPS() {
        this.dispatchExpiration = DEFAULT_DISPATCH_EXPIRATION;
        this.offlineDispatchLimit = DEFAULT_OFFLINE_DISPATCH_LIMIT;
        this.eventBatchSize = DEFAULT_EVENT_BATCH_SIZE;
        this.wifiOnlySending = DEFAULT_WIFI_ONLY_SENDING;
        this.batterySaver = DEFAULT_BATTERY_SAVER;
    }

    MPS(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP.NAME, 0);
        this.dispatchExpiration = sp.getInt(Constant.SP.KEY_DISPATCH_EXPIRATION, DEFAULT_DISPATCH_EXPIRATION);
        this.offlineDispatchLimit = sp.getInt(Constant.SP.KEY_OFFLINE_DISPATCH_LIMIT, DEFAULT_OFFLINE_DISPATCH_LIMIT);
        this.eventBatchSize = sp.getInt(Constant.SP.KEY_EVENT_BATCH_SIZE, DEFAULT_EVENT_BATCH_SIZE);
        this.wifiOnlySending = sp.getBoolean(Constant.SP.KEY_WIFI_ONLY_SENDING, DEFAULT_WIFI_ONLY_SENDING);
        this.batterySaver = sp.getBoolean(Constant.SP.KEY_BATTERY_SAVER, DEFAULT_BATTERY_SAVER);
    }

    MPS(JSONObject o) throws DisabledLibraryException {

        if (!o.optBoolean(Constant.SP.KEY_IS_ENABLED, true)) {
            throw new DisabledLibraryException();
        }

        this.dispatchExpiration = o.optInt(Constant.SP.KEY_DISPATCH_EXPIRATION, -1);
        this.offlineDispatchLimit = o.optInt(Constant.SP.KEY_OFFLINE_DISPATCH_LIMIT, -1);
        this.eventBatchSize = o.optInt(Constant.SP.KEY_EVENT_BATCH_SIZE, 1);
        this.wifiOnlySending = o.optBoolean(Constant.SP.KEY_WIFI_ONLY_SENDING, false);
        this.batterySaver = o.optBoolean(Constant.SP.KEY_BATTERY_SAVER, true);
    }

    int getDispatchExpiration() {
        return dispatchExpiration;
    }

    int getOfflineDispatchLimit() {
        return offlineDispatchLimit;
    }

    int getEventBatchSize() {
        return eventBatchSize;
    }

    boolean isWifiOnlySending() {
        return wifiOnlySending;
    }

    boolean isBatterySaver() {
        return batterySaver;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MPS) {
            MPS other = (MPS) o;

            return this.dispatchExpiration == other.dispatchExpiration &&
                    this.offlineDispatchLimit == other.offlineDispatchLimit &&
                    this.eventBatchSize == other.eventBatchSize &&
                    this.wifiOnlySending == other.wifiOnlySending &&
                    this.batterySaver == other.batterySaver;
        }

        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = this.hashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + this.dispatchExpiration;
            result = 31 * result + this.offlineDispatchLimit;
            result = 31 * result + this.eventBatchSize;
            result = 31 * result + (this.wifiOnlySending ? 1 : 0);
            result = 31 * result + (this.batterySaver ? 1 : 0);
            this.hashCode = result;
        }
        return result;
    }

    void save(Context context) {
        context.getSharedPreferences(Constant.SP.NAME, 0).edit()
                .putInt(Constant.SP.KEY_DISPATCH_EXPIRATION, this.dispatchExpiration)
                .putInt(Constant.SP.KEY_OFFLINE_DISPATCH_LIMIT, this.offlineDispatchLimit)
                .putInt(Constant.SP.KEY_EVENT_BATCH_SIZE, this.eventBatchSize)
                .putBoolean(Constant.SP.KEY_WIFI_ONLY_SENDING, this.wifiOnlySending)
                .putBoolean(Constant.SP.KEY_BATTERY_SAVER, this.batterySaver)
                .commit();
    }

    @Override
    public String toString() {
        return this.toString(null);
    }

    public String toString(String indentation) {

        if (indentation == null) {
            indentation = "";
        }

        final String dbltab = indentation.length() == 0 ? Constant.TAB : indentation + indentation;

        return indentation + '{' + Constant.NEW_LINE +
                dbltab + Constant.SP.KEY_BATTERY_SAVER + " : " + this.batterySaver + ',' + Constant.NEW_LINE +
                dbltab + Constant.SP.KEY_DISPATCH_EXPIRATION + " : " + this.dispatchExpiration + ',' + Constant.NEW_LINE +
                dbltab + Constant.SP.KEY_EVENT_BATCH_SIZE + " : " + this.eventBatchSize + ',' + Constant.NEW_LINE +
                dbltab + Constant.SP.KEY_OFFLINE_DISPATCH_LIMIT + " : " + this.offlineDispatchLimit + ',' + Constant.NEW_LINE +
                dbltab + Constant.SP.KEY_WIFI_ONLY_SENDING + " : " + this.wifiOnlySending + Constant.NEW_LINE +
                indentation + '}';
    }

    static class DisabledLibraryException extends Exception {
        private static final long serialVersionUID = -681561947900248949L;
    }
}
