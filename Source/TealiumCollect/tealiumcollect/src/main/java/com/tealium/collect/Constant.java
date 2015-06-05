package com.tealium.collect;

final class Constant {

    static final String NEW_LINE = System.getProperty("line.separator");
    static final String TAB = "    ";

    // Created because BuildConfig.DEBUG always false when creating this module.
    static final boolean DEBUG = false;
    static final String DEBUG_TAG = "AS-DEBUG";
    static final String VERSION = "AS_1.0";

    private Constant() {
    }

    final static class SP {

        static final String NAME = "tealium.collect";
        static final String KEY_PROFILE = "profile";
        static final String KEY_IS_ENABLED = "_is_enabled";
        static final String KEY_DISPATCH_EXPIRATION = "dispatch_expiration";
        static final String KEY_OFFLINE_DISPATCH_LIMIT = "offline_dispatch_limit";
        static final String KEY_EVENT_BATCH_SIZE = "event_batch_size";
        static final String KEY_WIFI_ONLY_SENDING = "wifi_only_sending";
        static final String KEY_BATTERY_SAVER = "battery_saver";
        static final String KEY_DEVICE_UUID = "device_uuid";

        private SP() {
        }
    }
}
