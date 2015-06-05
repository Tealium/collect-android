package com.tealium.collect;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tealium.collect.TealiumCollect.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

final class Queue implements Events.OnMPSUpdateListener {

    private final String path;

    private MPS mps;
    private int dispatchCount;

    Queue(Config config) {

        if (config == null) {
            throw new IllegalArgumentException("config must not be null.");
        }

        this.path = config.getContext().getCacheDir().getAbsolutePath() +
                File.pathSeparator +
                "tealium.collect.db";

        this.mps = config.getMPS();

        SQLiteDatabase db = SQLiteDatabase.openDatabase(
                this.path,
                null,
                SQLiteDatabase.CREATE_IF_NECESSARY);

        db.execSQL("CREATE TABLE IF NOT EXISTS dispatch ( " +
                "	data_json TEXT NOT NULL, " +
                "	post_time INT NOT NULL " +
                ")");

        Cursor c = db.rawQuery("SELECT COUNT(*) FROM dispatch", null);
        c.moveToFirst();
        this.dispatchCount = c.getInt(0);
        c.close();
        purgeOutdated(db, this.mps.getOfflineDispatchLimit());
        db.close();
    }

    String getPath() {
        return this.path;
    }

    void enqueueDispatch(JSONObject dispatch) {
        this.enqueueDispatch(dispatch, System.currentTimeMillis());
    }

    void enqueueDispatch(JSONObject dispatch, long timestamp) {
        if (dispatch == null) {
            throw new IllegalArgumentException("dispatch must not be null.");
        }

        if (this.mps.getOfflineDispatchLimit() == 0) {
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        final int newCount = this.dispatchCount + 1;

        if ((this.mps.getOfflineDispatchLimit() != -1) &&
                newCount > this.mps.getOfflineDispatchLimit()) {

            db.execSQL(
                    "DELETE FROM dispatch " +
                            "WHERE rowid IN ( " +
                            "	SELECT rowid " +
                            "	FROM dispatch " +
                            "	ORDER BY post_time ASC " +
                            "	LIMIT ? " +
                            ")", new Object[]{newCount - this.mps.getOfflineDispatchLimit()});

            this.dispatchCount = this.mps.getOfflineDispatchLimit();
        } else {
            this.dispatchCount++;
        }

        ContentValues vals = new ContentValues(3);
        vals.put("data_json", dispatch.toString());
        vals.put("post_time", timestamp);
        db.insert("dispatch", null, vals);
        db.close();
    }

    JSONObject[] dequeueDispatches() {

        if (this.dispatchCount == 0) {
            return new JSONObject[0];
        }

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query("dispatch", new String[]{"data_json"},
                null, null, null, null, null);

        JSONObject[] dispatches = new JSONObject[c.getCount()];
        int i = 0;
        c.moveToFirst();

        try {
            while (!c.isAfterLast()) {
                dispatches[i++] = new JSONObject(c.getString(0));
                c.moveToNext();
            }
        } catch (JSONException e) {
            Logger.e(e);
            // Data was corrupted send empty.
            dispatches = new JSONObject[0];
        }

        c.close();
        db.delete("dispatch", null, null);
        this.dispatchCount = 0;
        db.close();

        return dispatches;
    }

    int getDispatchCount() {
        return this.dispatchCount;
    }

    private SQLiteDatabase getWritableDatabase() {
        return SQLiteDatabase.openDatabase(
                this.path,
                null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Purge records older than what was specified by the {@link MPS#getDispatchExpiration()}.
     */
    static void purgeOutdated(SQLiteDatabase openDB, int dispatchExpirationInDays) {
        if (dispatchExpirationInDays < 0) {
            return;
        }

        final long expiration = System.currentTimeMillis() -
                ((long) dispatchExpirationInDays * 86400000L);

        openDB.execSQL("DELETE FROM dispatch WHERE post_time < ?", new Object[]{expiration});
    }

    @Override
    public void onMPSUpdate(MPS newMPS) {
        // Is in service thread.
        this.mps = newMPS;
        SQLiteDatabase db = this.getWritableDatabase();
        purgeOutdated(db, this.mps.getDispatchExpiration());
        db.close();
    }
}
