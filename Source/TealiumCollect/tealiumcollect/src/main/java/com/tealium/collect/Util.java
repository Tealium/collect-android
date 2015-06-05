package com.tealium.collect;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.RejectedExecutionException;

final class Util {
    private Util() {
    }

    private static final Handler uiHandler = new Handler(Looper.getMainLooper());

    static final class JSON {
        private JSON() {

        }

        /**
         * Converts a given map into a JSONObject.
         * <p/>
         * When a map instance is provided, all of its entries are coerced into
         * JSONObject or JSONArrays if the values are containers, otherwise
         * they're stringified.
         *
         * @param map instance or null.
         * @return JSONObject of deep-copied values or null if given map was
         * null.
         */
        static JSONObject mapToJSONObject(Map<?, ?> map) {
            if (map == null) {
                return null;
            }

            JSONObject o = new JSONObject();

            for (Entry<?, ?> entry : map.entrySet()) {
                JSON.copyPutIntoJSONObject(o, entry.getKey(), entry.getValue());
            }

            return o;
        }

        /**
         * Converts a given collection into a JSONArray.
         * <p/>
         * When a collection instance is provided, all of its entries are
         * coerced into JSONObject or JSONArrays if the values are containers,
         * otherwise they're stringified.
         *
         * @param collection instance or null.
         * @return JSONArray of deep-copied values or null if given collection
         * was null.
         */
        static JSONArray collectionToJSONArray(Collection<?> collection) {
            if (collection == null) {
                return null;
            }

            JSONArray array = new JSONArray();

            for (Object item : collection) {
                JSON.copyPutIntoJSONArray(array, item);
            }

            return array;
        }

        /**
         * Converts a given native array into a JSONArray.
         * <p/>
         * When an array instance is provided, all of its entries are coerced
         * into JSONObject or JSONArrays if the values are containers, otherwise
         * they're stringified.
         *
         * @param array instance or null.
         * @return JSONArray of deep-copied values or null if given array was
         * null.
         * @throws IllegalArgumentException when the given object is not a native array.
         */
        static JSONArray arrayToJSONArray(Object array) {

            if (array == null) {
                return null;
            }

            if (!array.getClass().isArray()) {
                throw new IllegalArgumentException("given object must be a native array.");
            }

            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < Array.getLength(array); i++) {
                JSON.copyPutIntoJSONArray(jsonArray, Array.get(array, i));
            }

            return jsonArray;
        }

        /**
         * Deep copies a given array coercing values into JSONObjects,
         * JSONArrays, or Strings.
         *
         * @param source instance or null.
         * @return JSONArray of deep-copied values or null if given source was
         * null.
         */
        static JSONArray deepCopyJSONArray(JSONArray source) {

            if (source == null) {
                return null;
            }

            JSONArray destination = new JSONArray();

            try {

                for (int i = 0; i < source.length(); i++) {
                    JSON.copyPutIntoJSONArray(destination, source.get(i));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return destination;

        }

        /**
         * Deep copies a given JSONObject coercing values into JSONObjects,
         * JSONArrays, or Strings.
         *
         * @param source instance or null.
         * @return JSONObject of deep-copied values or null if given source was
         * null.
         */
        static JSONObject deepCopyJSONObject(JSONObject source) {

            if (source == null) {
                return null;
            }

            Iterator<String> keys = source.keys();
            String key;

            JSONObject destination = new JSONObject();

            try {

                while (keys.hasNext()) {
                    JSON.copyPutIntoJSONObject(destination, key = keys.next(), source.get(key));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return destination;
        }

        private static void copyPutIntoJSONObject(JSONObject destination, Object key, Object value) {

            if (destination == null || key == null || value == null) {
                return;
            }

            try {

                if (value instanceof Map<?, ?>) {
                    destination.put(key.toString(), mapToJSONObject((Map<?, ?>) value));
                } else if (value instanceof Collection<?>) {
                    destination.put(key.toString(), collectionToJSONArray((Collection<?>) value));
                } else if (value.getClass().isArray()) {
                    destination.put(key.toString(), arrayToJSONArray(value));
                } else if (value instanceof JSONObject) {
                    destination.put(key.toString(), deepCopyJSONObject((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    destination.put(key.toString(), deepCopyJSONArray((JSONArray) value));
                } else {
                    destination.put(key.toString(), value.toString());
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        private static void copyPutIntoJSONArray(JSONArray destination, Object item) {

            if (item == null || destination == null) {
                return;
            }

            if (item instanceof Map<?, ?>) {
                destination.put(mapToJSONObject((Map<?, ?>) item));
            } else if (item instanceof Collection<?>) {
                destination.put(collectionToJSONArray((Collection<?>) item));
            } else if (item.getClass().isArray()) {
                destination.put(arrayToJSONArray(item));
            } else if (item instanceof JSONObject) {
                destination.put(deepCopyJSONObject((JSONObject) item));
            } else if (item instanceof JSONArray) {
                destination.put(deepCopyJSONArray((JSONArray) item));
            } else {
                destination.put(item.toString());
            }
        }
    }

    static final class Connectivity {
        private Connectivity() {
        }

        static boolean isConnected(ConnectivityManager connectivityManager, MPS mps) {

            final NetworkInfo activeNetworkInfo;

            if (mps.isWifiOnlySending()) {
                activeNetworkInfo =
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            } else {

                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            }

            final boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();

            if (Constant.DEBUG) {
                Log.v(Constant.DEBUG_TAG, String.format(
                        Locale.ROOT,
                        "# %b: isConnected; (Is Wifi only %b)",
                        isConnected,
                        mps.isWifiOnlySending()
                ));
            }

            return isConnected;
        }
    }

    static final class Network {

        static final String METHOD_HEAD = "HEAD";
        static final String METHOD_GET = "GET";

        interface OnHttpResponseListener {
            void onHttpResponse(String url, String method, int status, String entity);

            void onHttpError(String url, Throwable t);
        }

        private Network() {
        }

        static void performRequest(OnHttpResponseListener listener, String url, long delay) {
            performRequest(listener, url, METHOD_GET, null, delay);
        }

        /**
         * Callbacks will be invoked on the same background thread the http communication was
         * performed.
         */
        static void performRequest(
                OnHttpResponseListener listener,
                String url,
                String method,
                Map<String, String> headers,
                long delay) {

            final Runnable taskRunnable = createTaskRunnable(
                    listener,
                    url,
                    method,
                    headers);

            if (delay == 0) {
                uiHandler.post(taskRunnable);
                return;
            }

            uiHandler.postDelayed(taskRunnable, delay);
        }

        private static Runnable createTaskRunnable(final OnHttpResponseListener listener,
                                                   final String url,
                                                   final String method,
                                                   final Map<String, String> headers) {
            return new Runnable() {
                @Override
                public void run() {

                    // for API 10, an AsyncTask must be assembled in a thread that has called Looper.prepare();
                    final AsyncTask<Void, Void, Void> task = createNetworkTask(listener, url, method, headers);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        try {
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } catch (RejectedExecutionException e) {
                            // Needs to be recreated to avoid .
                            createNetworkTask(listener, url, method, headers)
                                    .executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                        }
                    } else {
                        task.execute();
                    }
                }
            };
        }

        private static AsyncTask<Void, Void, Void> createNetworkTask(
                final OnHttpResponseListener listener,
                final String url,
                final String method,
                final Map<String, String> headers) {

            return new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {

                    HttpURLConnection connection = null;

                    try {
                        connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setRequestMethod(method);
                        connection.setDoInput(true);
                        connection.setDoOutput(false);
                        if (headers != null) {
                            for (Entry<String, String> header : headers.entrySet()) {
                                connection.setRequestProperty(header.getKey(), header.getValue());
                            }
                        }
                        connection.connect();

                        // Read needs to be performed to complete the HTTP transaction.
                        final String result = read(new InputStreamReader(connection.getInputStream()));

                        if (listener == null) {
                            return null;
                        }

                        listener.onHttpResponse(url, method, connection.getResponseCode(), result);
                    } catch (Throwable t) {
                        Logger.e(t);
                        if (listener != null) {
                            listener.onHttpError(url, t);
                        }
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }

                    return null;
                }
            };
        }

    }

    static boolean isEmptyOrNull(String value) {
        return value == null || value.length() == 0;
    }

    static String getUnixTimestamp() {
        return longToUnixTimestamp(System.currentTimeMillis());
    }

    static String longToUnixTimestamp(long timestamp) {
        return Long.toString(timestamp / 1000);
    }

    static String stackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    static boolean appHasPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    static String readFile(File file) {

        try {
            return read(new FileReader(file));
        } catch (IOException e) {
            Logger.e("Error reading " + file.getAbsolutePath(), e);
            return null;
        }
    }

    private static String read(Reader reader) throws IOException {
        StringBuilder text = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                if (text.length() > 0) {
                    text.append(Constant.NEW_LINE);
                }
                text.append(line);
            }
            return text.toString();
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    static boolean writeFile(File file, String contents) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(contents);
            outputStreamWriter.close();
            return true;
        } catch (IOException e) {
            Logger.e("Error writing " + file.getAbsolutePath(), e);
            return false;
        }
    }
}
