package com.tealium.collect.visitor;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.DateAttribute;
import com.tealium.collect.testutil.Timeout;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class DateDiffNotifierTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TealiumCollect.getEventListeners().clear();
    }

    public void testRun() throws Exception {

        DateListener listener = new DateListener();
        TealiumCollect.getEventListeners().add(listener);

        DateDiffNotifier adn = new DateDiffNotifier(null, null);
        Timeout timeout = Timeout.start();
        adn.run();
        while (!timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new DateDiffNotifier(createOldDates(), null);
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new DateDiffNotifier(null, createNewDate());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();

        adn = new DateDiffNotifier(createOldDates(), createNewDate());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();

        adn = new DateDiffNotifier(createOldDates(), createModifiedDates());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(1, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());
    }

    private static AttributeGroup<DateAttribute> createOldDates() {
        Set<DateAttribute> set = new HashSet<>(1);
        set.add(new DateAttribute("old_date", 0));
        return new AttributeGroup<>(set);
    }

    private static AttributeGroup<DateAttribute> createModifiedDates() {
        Set<DateAttribute> set = new HashSet<>(1);
        set.add(new DateAttribute("old_date", 1));
        return new AttributeGroup<>(set);
    }

    private static AttributeGroup<DateAttribute> createNewDate() {
        Set<DateAttribute> set = new HashSet<>(1);
        set.add(new DateAttribute("new_date", 1));
        return new AttributeGroup<>(set);
    }

    private static class DateListener implements TealiumCollect.OnDateUpdateListener {

        volatile boolean called = false;
        final AtomicInteger removedCount = new AtomicInteger();
        final AtomicInteger modifiedCount = new AtomicInteger();
        final AtomicInteger addedCount = new AtomicInteger();

        @Override
        public void onDateUpdate(DateAttribute oldDate, DateAttribute newDate) {
            if (oldDate != null && newDate == null) {
                this.removedCount.incrementAndGet();
            } else if (oldDate == null && newDate != null) {
                this.addedCount.incrementAndGet();
            } else if (oldDate != null) {
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
