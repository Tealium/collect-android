package com.tealium.collect.visitor;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.PropertyAttribute;
import com.tealium.collect.testutil.Timeout;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class PropertyDiffNotifierTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TealiumCollect.getEventListeners().clear();
    }

    public void testRun() throws Exception {

        PropertyListener listener = new PropertyListener();
        TealiumCollect.getEventListeners().add(listener);

        PropertyDiffNotifier adn = new PropertyDiffNotifier(null, null);
        Timeout timeout = Timeout.start();
        adn.run();
        while (!timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new PropertyDiffNotifier(createOldProperties(), null);
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());

        listener.reset();

        adn = new PropertyDiffNotifier(null, createNewProperties());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();

        adn = new PropertyDiffNotifier(createOldProperties(), createNewProperties());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(1, listener.removedCount.get());
        Assert.assertEquals(0, listener.modifiedCount.get());
        Assert.assertEquals(1, listener.addedCount.get());

        listener.reset();

        adn = new PropertyDiffNotifier(createOldProperties(), createModifiedProperties());
        timeout.rerun();
        adn.run();
        while (!listener.called && !timeout.isTimedout()) {
            // wait
        }
        Assert.assertEquals(0, listener.removedCount.get());
        Assert.assertEquals(1, listener.modifiedCount.get());
        Assert.assertEquals(0, listener.addedCount.get());
    }

    private static AttributeGroup<PropertyAttribute> createOldProperties() {
        Set<PropertyAttribute> set = new HashSet<>(1);
        set.add(new PropertyAttribute("old_property", null));
        return new AttributeGroup<>(set);
    }

    private static AttributeGroup<PropertyAttribute> createModifiedProperties() {
        Set<PropertyAttribute> set = new HashSet<>(1);
        set.add(new PropertyAttribute("old_property", "1"));
        return new AttributeGroup<>(set);
    }

    private static AttributeGroup<PropertyAttribute> createNewProperties() {
        Set<PropertyAttribute> set = new HashSet<>(1);
        set.add(new PropertyAttribute("new_property", "1"));
        return new AttributeGroup<>(set);
    }

    private static class PropertyListener implements TealiumCollect.OnPropertyUpdateListener {

        volatile boolean called = false;
        final AtomicInteger removedCount = new AtomicInteger();
        final AtomicInteger modifiedCount = new AtomicInteger();
        final AtomicInteger addedCount = new AtomicInteger();

        @Override
        public void onPropertyUpdate(PropertyAttribute oldProperty, PropertyAttribute newProperty) {
            if (oldProperty != null && newProperty == null) {
                this.removedCount.incrementAndGet();
            } else if (oldProperty == null && newProperty != null) {
                this.addedCount.incrementAndGet();
            } else if (oldProperty != null) {
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
