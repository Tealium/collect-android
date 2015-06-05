package com.tealium.collect.visitor;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.MetricAttribute;
import com.tealium.collect.testutil.Timeout;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class MetricDiffNotifierTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TealiumCollect.getEventListeners().clear();
    }

    public void testRun() throws Exception {

        MetricListener listener = new MetricListener();
        TealiumCollect.getEventListeners().add(listener);

        MetricDiffNotifier adn = new MetricDiffNotifier(null, null);
        Timeout timeout = Timeout.start();
        adn.run();
        while (!timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new MetricDiffNotifier(createOldMetrics(), null);
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new MetricDiffNotifier(null, createNewMetric());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();

        adn = new MetricDiffNotifier(createOldMetrics(), createNewMetric());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();

        adn = new MetricDiffNotifier(createOldMetrics(), createModifiedMetrics());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(1, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());
    }

    private static AttributeGroup<MetricAttribute> createOldMetrics() {
        Set<MetricAttribute> set = new HashSet<>(1);
        set.add(new MetricAttribute("old_date", 0));
        return new AttributeGroup<>(set);
    }

    private static AttributeGroup<MetricAttribute> createModifiedMetrics() {
        Set<MetricAttribute> set = new HashSet<>(1);
        set.add(new MetricAttribute("old_date", 1));
        return new AttributeGroup<>(set);
    }

    private static AttributeGroup<MetricAttribute> createNewMetric() {
        Set<MetricAttribute> set = new HashSet<>(1);
        set.add(new MetricAttribute("new_date", 1));
        return new AttributeGroup<>(set);
    }

    private static class MetricListener implements TealiumCollect.OnMetricUpdateListener {

        volatile boolean called = false;
        final AtomicInteger removedCount = new AtomicInteger();
        final AtomicInteger modifiedCount = new AtomicInteger();
        final AtomicInteger addedCount = new AtomicInteger();

        @Override
        public void onMetricUpdate(MetricAttribute oldMetric, MetricAttribute newMetric) {
            if (oldMetric != null && newMetric == null) {
                this.removedCount.incrementAndGet();
            } else if (oldMetric == null && newMetric != null) {
                this.addedCount.incrementAndGet();
            } else if (oldMetric != null) {
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
