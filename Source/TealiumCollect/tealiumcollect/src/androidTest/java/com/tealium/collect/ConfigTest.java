package com.tealium.collect;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import junit.framework.Assert;

public class ConfigTest extends ApplicationTestCase<Application> {
    public ConfigTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.createApplication();
    }

    public void testConstructor() throws Exception {

        try {
            new TealiumCollect.Config(null, null, null, null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {

        }

        try {
            new TealiumCollect.Config(this.getApplication(), null, null, null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new TealiumCollect.Config(this.getApplication(), "", "", "");
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        createDefaultConfig();
    }

    public void testSetHttpsEnabled() throws Exception {
        TealiumCollect.Config config = createDefaultConfig()
                .setHttpsEnabled(false);

        Assert.assertFalse(config.isHttpsEnabled());

        Assert.assertTrue(config.setHttpsEnabled(true)
                .isHttpsEnabled());
    }

    public void testSetLogLevel() throws Exception {
        TealiumCollect.Config config = createDefaultConfig();


        config.setLogLevel(0); // Min is 2
        Assert.assertEquals(Logger.SILENT, Logger.getLogLevel());

        config.setLogLevel(Log.DEBUG);
        Assert.assertEquals(Log.DEBUG, Logger.getLogLevel());

        config.setLogLevel(Log.WARN);
        Assert.assertEquals(Log.WARN, Logger.getLogLevel());

        config.setLogLevel(8); // Max is 7
        Assert.assertEquals(Logger.SILENT, Logger.getLogLevel());
    }

    public void testSetOverrideProfile() throws Exception {
        TealiumCollect.Config config = createDefaultConfig()
                .setOverrideProfile("test");

        Assert.assertEquals("test", config.getOverrideProfile());
    }

    private TealiumCollect.Config createDefaultConfig() {
        return new TealiumCollect.Config(this.getApplication(), " ", " ", " ");
    }
}
