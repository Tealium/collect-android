package com.tealium.collect.visitor;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.FlagAttribute;
import com.tealium.collect.testutil.Timeout;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class FlagDiffNotifierTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TealiumCollect.getEventListeners().clear();
    }

    public void testRun() throws Exception {

        FlagListener listener = new FlagListener();
        TealiumCollect.getEventListeners().add(listener);

        FlagDiffNotifier adn = new FlagDiffNotifier(null, null);
        Timeout timeout = Timeout.start();
        adn.run();
        while (!timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new FlagDiffNotifier(createOldFlags(), null);
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new FlagDiffNotifier(null, createNewFlags());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();

        adn = new FlagDiffNotifier(createOldFlags(), createNewFlags());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();

        adn = new FlagDiffNotifier(createOldFlags(), createModifiedFlags());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(1, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());
    }

    private static AttributeGroup<FlagAttribute> createOldFlags() {
        Set<FlagAttribute> set = new HashSet<>(1);
        set.add(new FlagAttribute("old_flag", false));
        return new AttributeGroup<>(set);
    }

    private static AttributeGroup<FlagAttribute> createModifiedFlags() {
        Set<FlagAttribute> set = new HashSet<>(1);
        set.add(new FlagAttribute("old_flag", true));
        return new AttributeGroup<>(set);
    }

    private static AttributeGroup<FlagAttribute> createNewFlags() {
        Set<FlagAttribute> set = new HashSet<>(1);
        set.add(new FlagAttribute("new_flag", true));
        return new AttributeGroup<>(set);
    }

    private static class FlagListener implements TealiumCollect.OnFlagUpdateListener {

        volatile boolean called = false;
        final AtomicInteger removedCount = new AtomicInteger();
        final AtomicInteger modifiedCount = new AtomicInteger();
        final AtomicInteger addedCount = new AtomicInteger();

        @Override
        public void onFlagUpdate(FlagAttribute oldFlag, FlagAttribute newFlag) {
            if (oldFlag != null && newFlag == null) {
                this.removedCount.incrementAndGet();
            } else if (oldFlag == null && newFlag != null) {
                this.addedCount.incrementAndGet();
            } else if (oldFlag != null) {
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
