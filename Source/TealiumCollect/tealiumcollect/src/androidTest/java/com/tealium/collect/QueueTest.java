package com.tealium.collect;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import org.json.JSONObject;

public class QueueTest extends ApplicationTestCase<Application> {

    private Queue queue;

    public QueueTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.createApplication();

        this.queue = new Queue(new TealiumCollect.Config(
                this.getApplication(),
                "tealiummobile",
                "demo",
                "dev"));

        // Default MPS
        this.queue.onMPSUpdate(new MPS());
        this.queue.dequeueDispatches();
    }

    @Override
    protected void tearDown() throws Exception {
        this.queue.dequeueDispatches();
        this.queue = null;
        super.tearDown();
    }

    public void testConstructor() throws Throwable {
        try {
            new Queue(null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    public void testEnqueueLimit3() throws Throwable {

        this.queue.onMPSUpdate(new MPS(new JSONObject()
                .put(Constant.SP.KEY_OFFLINE_DISPATCH_LIMIT, 3)));

        // Should only keep 3.
        this.queue.enqueueDispatch(new JSONObject());
        this.queue.enqueueDispatch(new JSONObject());
        this.queue.enqueueDispatch(new JSONObject());
        this.queue.enqueueDispatch(new JSONObject());

        Assert.assertEquals(3, this.getCount());
        Assert.assertEquals(3, this.queue.getDispatchCount());
    }

    public void testEnqueueLimit0() throws Throwable {

        this.queue.onMPSUpdate(new MPS(new JSONObject()
                .put(Constant.SP.KEY_OFFLINE_DISPATCH_LIMIT, 0)));

        // Should keep none.
        this.queue.enqueueDispatch(new JSONObject());
        this.queue.enqueueDispatch(new JSONObject());
        this.queue.enqueueDispatch(new JSONObject());
        this.queue.enqueueDispatch(new JSONObject());

        Assert.assertEquals(0, this.getCount());
        Assert.assertEquals(0, this.queue.getDispatchCount());
    }

    public void testEnqueueLimitNeg1() throws Throwable {

        this.queue.onMPSUpdate(new MPS(new JSONObject()
                .put(Constant.SP.KEY_OFFLINE_DISPATCH_LIMIT, -1)));

        this.queue.enqueueDispatch(new JSONObject());
        this.queue.enqueueDispatch(new JSONObject());
        this.queue.enqueueDispatch(new JSONObject());
        this.queue.enqueueDispatch(new JSONObject());

        Assert.assertEquals(4, this.getCount());
        Assert.assertEquals(4, this.queue.getDispatchCount());
    }

    public void testDequeue() throws Throwable {

        this.queue.enqueueDispatch(new JSONObject());
        this.queue.enqueueDispatch(new JSONObject());

        this.queue.dequeueDispatches();

        Assert.assertEquals(this.getCount(), this.queue.getDispatchCount());
    }

    public void testClean() throws Throwable {

        final long now = System.currentTimeMillis();
        final long weekOld = now - (7L * 24L * 60L * 60L * 1000L);
        final long oldest = 0;

        this.queue.enqueueDispatch(new JSONObject(), oldest);
        this.queue.enqueueDispatch(new JSONObject(), weekOld);
        this.queue.enqueueDispatch(new JSONObject(), now);

        SQLiteDatabase db = this.getDB();

        Queue.purgeOutdated(db, -1);
        Assert.assertEquals(3, getCount(db));

        Queue.purgeOutdated(db, 8);
        Assert.assertEquals(2, getCount(db));

        Queue.purgeOutdated(db, 1);
        Assert.assertEquals(1, getCount(db));

        Queue.purgeOutdated(db, 0);
        Assert.assertEquals(0, getCount(db));

        db.close();
    }

    private int getCount() {
        SQLiteDatabase db = getDB();
        final int count = getCount(db);
        db.close();
        return count;
    }

    private int getCount(SQLiteDatabase openedDB) {
        Cursor c = openedDB.rawQuery("SELECT COUNT(*) FROM dispatch", null);
        final int count = c.moveToFirst() ? c.getInt(0) : -1;
        c.close();
        return count;
    }

    private SQLiteDatabase getDB() {
        return SQLiteDatabase.openDatabase(
                this.queue.getPath(),
                null,
                SQLiteDatabase.OPEN_READWRITE);
    }


}
