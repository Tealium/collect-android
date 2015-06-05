package com.tealium.collect;

import android.app.Application;
import android.os.Build;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import org.json.JSONObject;

public class ProcessingCenterTest extends ApplicationTestCase<Application> {
    public ProcessingCenterTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.createApplication();
    }

    public void testOnPopulateDispatch() throws Exception {
        ProcessingCenter pc = new ProcessingCenter(new TealiumCollect.Config(
                this.getApplication(),
                " ",
                " ",
                " "));

        JSONObject data = new JSONObject();

        pc.onPopulateDispatch(data);

        Assert.assertTrue(data.has(Key.APP_NAME));
        Assert.assertTrue(data.has(Key.TIMESTAMP));
        Assert.assertEquals(Constant.VERSION, data.get(Key.LIBRARY_VERSION));
        Assert.assertEquals(Build.VERSION.RELEASE, data.get(Key.OS_VERSION));
        Assert.assertEquals("android", data.get(Key.PLATFORM));

        // Near random value
        final String newVal = System.nanoTime() + "";

        data.put(Key.APP_NAME, newVal);
        data.put(Key.LIBRARY_VERSION, newVal);
        data.put(Key.OS_VERSION, newVal);
        data.put(Key.TIMESTAMP, newVal);
        data.put(Key.PLATFORM, newVal);

        // Should not replace any existing values
        pc.onPopulateDispatch(data);

        Assert.assertEquals(newVal, data.get(Key.APP_NAME));
        Assert.assertEquals(newVal, data.get(Key.LIBRARY_VERSION));
        Assert.assertEquals(newVal, data.get(Key.OS_VERSION));
        Assert.assertEquals(newVal, data.get(Key.TIMESTAMP));
        Assert.assertEquals(newVal, data.get(Key.PLATFORM));
    }
}
