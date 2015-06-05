package com.tealium.collect.visitor;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.BadgeAttribute;
import com.tealium.collect.testutil.Timeout;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class BadgeDiffNotifierTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TealiumCollect.getEventListeners().clear();
    }

    public void testRun() throws Exception {

        BadgeListener listener = new BadgeListener();
        TealiumCollect.getEventListeners().add(listener);

        BadgeDiffNotifier adn = new BadgeDiffNotifier(null, null);
        Timeout timeout = Timeout.start();
        adn.run();
        while (!timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new BadgeDiffNotifier(createOldBadges(), null);
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new BadgeDiffNotifier(null, createNewBadges());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();

        adn = new BadgeDiffNotifier(createOldBadges(), createNewBadges());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();
    }

    private static AttributeGroup<BadgeAttribute> createOldBadges() {
        Set<BadgeAttribute> set = new HashSet<>(1);
        set.add(new BadgeAttribute("old_badge"));
        return new AttributeGroup<>(set);
    }

    private static AttributeGroup<BadgeAttribute> createNewBadges() {
        Set<BadgeAttribute> set = new HashSet<>(1);
        set.add(new BadgeAttribute("new_badges"));
        return new AttributeGroup<>(set);
    }

    private static class BadgeListener implements TealiumCollect.OnBadgeUpdateListener {

        volatile boolean called = false;
        final AtomicInteger removedCount = new AtomicInteger();
        final AtomicInteger modifiedCount = new AtomicInteger();
        final AtomicInteger addedCount = new AtomicInteger();

        @Override
        public void onBadgeUpdate(BadgeAttribute oldBadge, BadgeAttribute newBadge) {
            if (oldBadge != null && newBadge == null) {
                this.removedCount.incrementAndGet();
            } else if (oldBadge == null && newBadge != null) {
                this.addedCount.incrementAndGet();
            } else if (oldBadge != null) {
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
