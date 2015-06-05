package com.tealium.kitchensink.util;

/**
 * Created by chadhartman on 1/20/15.
 */
public final class Constant {

    private Constant() {
    }

    public static final class LocalBroadcast {
        private LocalBroadcast() {
        }

        public static final String ACTION_COLOR_CHANGE = "color_change";
        public static final String EXTRA_OLD_COLOR = "old_color";
        public static final String EXTRA_NEW_COLOR = "new_color";

        public static final String ACTION_PUSH_RECEIVED = "push_received";
        public static final String EXTRA_MESSAGE = "message";
    }
}
