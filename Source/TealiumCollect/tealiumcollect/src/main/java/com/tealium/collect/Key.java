package com.tealium.collect;

import java.util.Map;

/**
 * Values for the various data sources populated by this library.
 */
public final class Key {
    private Key() {
    }

    /**
     * Key for the app name as declared by AndroidManifest.xml.
     */
    public final static String APP_NAME = "app_name";

    /**
     * Key describing the kind of event.
     * <br/>
     * * "view" when calling {@link TealiumCollect#sendView(Map)}.
     * * "link" when calling {@link TealiumCollect#sendEvent(Map)}.
     * * First argument when calling {@link TealiumCollect#send(String, Map)}.
     */
    public final static String CALL_TYPE = "call_type";

    /**
     * Key for the device_uuid if available.
     * <p/>
     * When the android.permission.READ_EXTERNAL_STORAGE permission is available, the value for
     * this key may be populated by an existing device_uuid.
     * <p/>
     * When the android.permission.WRITE_EXTERNAL_STORAGE permission is available, the value for
     * this will be populated when the external storage is available for writing.
     */
    public final static String DEVICE_UUID = "device_uuid";

    /**
     * Key for call_type="link" calls with the value "mobile_link".
     */
    public final static String EVENT_NAME = "event_name";

    /**
     * Current version of the Tealium Collect Library.
     */
    public final static String LIBRARY_VERSION = "library_version";

    /**
     * Key for the value provided by {@link android.os.Build.VERSION#RELEASE}.
     */
    public final static String OS_VERSION = "os_version";

    /**
     * Key for the time of the dispatch formatted ISO8601 at Zulu.
     */
    public final static String TIMESTAMP = "timestamp";

    /**
     * Key for call_type="view" calls with the value "mobile_view".
     */
    public final static String PAGE_TYPE = "page_type";

    /**
     * Key for the value "android".
     */
    public final static String PLATFORM = "platform";

    /**
     * Key with a boolean of whether the dispatch was queued before sending. This can be used to
     * filter what's live vs what is historical.
     */
    public final static String WAS_QUEUED = "was_queued";
}
