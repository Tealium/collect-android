package com.tealium.collect.visitor;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.AudienceAttribute;
import com.tealium.collect.testutil.Timeout;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AudienceDiffNotifierTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TealiumCollect.getEventListeners().clear();
    }

    public void testRun() throws Exception {

        AudienceListener listener = new AudienceListener();
        TealiumCollect.getEventListeners().add(listener);

        AudienceDiffNotifier adn = new AudienceDiffNotifier(null, null);
        Timeout timeout = Timeout.start();
        adn.run();
        while (!timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new AudienceDiffNotifier(createOldAudiences(), null);
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new AudienceDiffNotifier(null, createNewAudiences());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();

        adn = new AudienceDiffNotifier(createOldAudiences(), createNewAudiences());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();

        adn = new AudienceDiffNotifier(createOldAudiences(), createModifiedAudiences());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(1, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());
    }

    private static AttributeGroup<AudienceAttribute> createOldAudiences() {

        Set<AudienceAttribute> set = new HashSet<>(1);
        set.add(new AudienceAttribute("old_audience", "old_name"));

        AttributeGroup<AudienceAttribute> audiences = new AttributeGroup<>(set);
        return audiences;
    }

    private static AttributeGroup<AudienceAttribute> createModifiedAudiences() {

        Set<AudienceAttribute> set = new HashSet<>(1);
        set.add(new AudienceAttribute("old_audience", "new_name"));

        AttributeGroup<AudienceAttribute> audiences = new AttributeGroup<>(set);
        return audiences;
    }

    private static AttributeGroup<AudienceAttribute> createNewAudiences() {

        Set<AudienceAttribute> set = new HashSet<>(1);
        set.add(new AudienceAttribute("new_audience", "new_name"));

        AttributeGroup<AudienceAttribute> audiences = new AttributeGroup<>(set);
        return audiences;
    }

    private static class AudienceListener implements TealiumCollect.OnAudienceUpdateListener {

        volatile boolean called = false;
        final AtomicInteger removedCount = new AtomicInteger();
        final AtomicInteger modifiedCount = new AtomicInteger();
        final AtomicInteger addedCount = new AtomicInteger();

        @Override
        public void onAudienceUpdate(AudienceAttribute oldAudience, AudienceAttribute newAudience) {
            if (oldAudience != null && newAudience == null) {
                this.removedCount.incrementAndGet();
            } else if (oldAudience == null && newAudience != null) {
                this.addedCount.incrementAndGet();
            } else if (oldAudience != null) {
                this.modifiedCount.incrementAndGet();
            }
        }

        public void reset() {
            this.called = false;
            this.removedCount.set(0);
            this.modifiedCount.set(0);
            this.addedCount.set(0);
        }
    }
}
