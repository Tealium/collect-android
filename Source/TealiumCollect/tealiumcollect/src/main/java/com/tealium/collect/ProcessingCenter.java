package com.tealium.collect;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

final class ProcessingCenter implements
        Events.OnPopulateDispatchListener {

    private final SimpleDateFormat timestampISO;
    private final String appName;
    private final String deviceUUID;

    ProcessingCenter(TealiumCollect.Config config) {
        final Context context = config.getContext();
        this.timestampISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
        this.timestampISO.setTimeZone(TimeZone.getTimeZone("UTC"));
        final int appNameId = context.getApplicationInfo().labelRes;
        this.appName = appNameId == 0 ? context.getPackageName() : context.getString(appNameId);

        SharedPreferences sp = config.getSharedPreferences();
        if (!sp.contains(Constant.SP.KEY_DEVICE_UUID)) {
            // Check for existing device_uuid.

            final String loadedDeviceUUID = loadDeviceUUID(context);
            if (loadedDeviceUUID == null || loadedDeviceUUID.length() == 0) {
                // There is no existing device_uuid.

                final String createdUUID = createDeviceUUID(context);
                if (createdUUID == null) {
                    this.deviceUUID = null;
                } else {
                    sp.edit().putString(
                            Constant.SP.KEY_DEVICE_UUID,
                            this.deviceUUID = createdUUID).apply();
                }
            } else {
                sp.edit().putString(
                        Constant.SP.KEY_DEVICE_UUID,
                        this.deviceUUID = loadedDeviceUUID).apply();
            }
        } else {
            this.deviceUUID = sp.getString(Constant.SP.KEY_DEVICE_UUID, null);
        }
    }

    @Override
    public void onPopulateDispatch(JSONObject dispatch) {
        try {

            if (!dispatch.has(Key.APP_NAME)) {
                dispatch.put(Key.APP_NAME, this.appName);
            }

            if (!dispatch.has(Key.LIBRARY_VERSION)) {
                dispatch.put(Key.LIBRARY_VERSION, Constant.VERSION);
            }

            if (!dispatch.has(Key.OS_VERSION)) {
                dispatch.put(Key.OS_VERSION, Build.VERSION.RELEASE);
            }

            if (!dispatch.has(Key.TIMESTAMP)) {
                dispatch.put(Key.TIMESTAMP,
                        this.timestampISO.format(new Date(System.currentTimeMillis())));
            }

            if (!dispatch.has(Key.PLATFORM)) {
                dispatch.put(Key.PLATFORM, "android");
            }

            if (this.deviceUUID != null && !dispatch.has(Key.DEVICE_UUID)) {
                dispatch.put(Key.DEVICE_UUID, this.deviceUUID);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static String loadDeviceUUID(Context context) {
        if (!Util.appHasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return null;
        }

        final String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state) &&
                !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return null;
        }

        final File file = getDeviceUUIDFile();
        if (!file.exists()) {
            return null;
        }

        String fileContents = null;

        try {
            if ((fileContents = Util.readFile(file)) == null) {
                return null;
            }

            JSONObject contents = new JSONObject(fileContents);
            if(contents.has("dnt")) {
                TealiumCollect.disable();
                return null;
            }
            return contents.optString(Constant.SP.KEY_DEVICE_UUID, null);
        } catch (Throwable t) {
            Logger.e("Error parsing device_uuid from " + fileContents, t);
            return null;
        }
    }

    private static String createDeviceUUID(Context context) {
        if (!Util.appHasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return null;
        }

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return null;
        }

        final String uuid = UUID.randomUUID().toString();
        try {
            File file = getDeviceUUIDFile();
            Util.writeFile(getDeviceUUIDFile(), new JSONObject()
                    .put(Constant.SP.KEY_DEVICE_UUID, uuid)
                    .toString());
            file.setReadOnly();
            return uuid;
        } catch (Throwable t) {
            Logger.e("Error creating device_uuid", t);
            return null;
        }
    }

    private static File getDeviceUUIDFile() {
        return new File(
                Environment.getExternalStorageDirectory(),
                ".tealium_as");
    }
}
