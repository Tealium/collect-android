package com.tealium.collect;

import android.app.Application;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import org.json.JSONObject;

public class MPSTest extends ApplicationTestCase<Application> {
    public MPSTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.createApplication();

        this.getApplication().getSharedPreferences(Constant.SP.NAME, 0)
                .edit().clear().commit();
    }

    public void testConstructor() throws Exception {

        // default constructor
        MPS mps = new MPS();
        Assert.assertEquals(MPS.DEFAULT_BATTERY_SAVER, mps.isBatterySaver());
        Assert.assertEquals(MPS.DEFAULT_DISPATCH_EXPIRATION, mps.getDispatchExpiration());
        Assert.assertEquals(MPS.DEFAULT_EVENT_BATCH_SIZE, mps.getEventBatchSize());
        Assert.assertEquals(MPS.DEFAULT_OFFLINE_DISPATCH_LIMIT, mps.getOfflineDispatchLimit());
        Assert.assertEquals(MPS.DEFAULT_WIFI_ONLY_SENDING, mps.isWifiOnlySending());

        // No historical config
        mps = new MPS(this.getApplication());
        Assert.assertEquals(MPS.DEFAULT_BATTERY_SAVER, mps.isBatterySaver());
        Assert.assertEquals(MPS.DEFAULT_DISPATCH_EXPIRATION, mps.getDispatchExpiration());
        Assert.assertEquals(MPS.DEFAULT_EVENT_BATCH_SIZE, mps.getEventBatchSize());
        Assert.assertEquals(MPS.DEFAULT_OFFLINE_DISPATCH_LIMIT, mps.getOfflineDispatchLimit());
        Assert.assertEquals(MPS.DEFAULT_WIFI_ONLY_SENDING, mps.isWifiOnlySending());

        // Test explicit values
        mps = new MPS(new JSONObject()
                .put(Constant.SP.KEY_BATTERY_SAVER, false)
                .put(Constant.SP.KEY_DISPATCH_EXPIRATION, 111)
                .put(Constant.SP.KEY_EVENT_BATCH_SIZE, 222)
                .put(Constant.SP.KEY_OFFLINE_DISPATCH_LIMIT, 333)
                .put(Constant.SP.KEY_WIFI_ONLY_SENDING, true));

        Assert.assertEquals(false, mps.isBatterySaver());
        Assert.assertEquals(111, mps.getDispatchExpiration());
        Assert.assertEquals(222, mps.getEventBatchSize());
        Assert.assertEquals(333, mps.getOfflineDispatchLimit());
        Assert.assertEquals(true, mps.isWifiOnlySending());

        mps.save(this.getApplication());

        // Test caching
        mps = new MPS(this.getApplication());
        Assert.assertEquals(false, mps.isBatterySaver());
        Assert.assertEquals(111, mps.getDispatchExpiration());
        Assert.assertEquals(222, mps.getEventBatchSize());
        Assert.assertEquals(333, mps.getOfflineDispatchLimit());
        Assert.assertEquals(true, mps.isWifiOnlySending());
        Assert.assertEquals(true, mps.isWifiOnlySending());

        try {
            new MPS(new JSONObject().put(Constant.SP.KEY_IS_ENABLED, false));
            Assert.fail();
        } catch (MPS.DisabledLibraryException ignored) {
        }
    }

    public void testEquals() throws Exception {
        MPS mps1 = new MPS();
        MPS mps2 = new MPS();

        Assert.assertFalse(mps1.equals(null));

        Assert.assertTrue(mps1.equals(mps2));
        Assert.assertEquals(mps1.hashCode(), mps2.hashCode());

        mps2 = new MPS(new JSONObject()
                .put(Constant.SP.KEY_BATTERY_SAVER, false));

        Assert.assertFalse(mps1.equals(mps2));
        Assert.assertTrue(mps1.hashCode() != mps2.hashCode());
    }
}
