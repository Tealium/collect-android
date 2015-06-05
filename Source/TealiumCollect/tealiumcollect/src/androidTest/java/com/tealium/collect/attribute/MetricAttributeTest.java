package com.tealium.collect.attribute;

import junit.framework.Assert;
import junit.framework.TestCase;

public class MetricAttributeTest extends TestCase {

    public void testConstructor() throws Throwable {

        try {
            new MetricAttribute(null, 1);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new MetricAttribute(null, 0);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new MetricAttribute("", 1);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new MetricAttribute("", 0);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        new MetricAttribute("a", 1);
        new MetricAttribute("a", 0);

        new MetricAttribute("The Lord is my shepherd, I shall not want.", 1);
        new MetricAttribute("The Lord is my shepherd, I shall not want.", 0);
    }

    public void testEquals() throws Throwable {

        final MetricAttribute metricA1 = new MetricAttribute("a", 1);
        final MetricAttribute metricA0 = new MetricAttribute("a", 0);
        final MetricAttribute flagBTrue = new MetricAttribute("b", 1);
        final MetricAttribute flagBFalse = new MetricAttribute("b", 0);

        Assert.assertTrue(metricA1.equals(metricA1));
        Assert.assertFalse(metricA1.equals(flagBTrue));
        Assert.assertFalse(metricA1.equals(metricA0));
        Assert.assertFalse(metricA1.equals(flagBFalse));
        Assert.assertFalse(metricA1.equals(null));
        Assert.assertTrue(metricA1.equals(new MetricAttribute("a", 1)));
        Assert.assertFalse(metricA1.equals(new MetricAttribute("A", 1)));
    }

    public void testHashCode() throws Throwable {

        final MetricAttribute metricA1 = new MetricAttribute("a", 1);
        final MetricAttribute metricA0 = new MetricAttribute("a", 0);
        final MetricAttribute flagBTrue = new MetricAttribute("b", 1);
        final MetricAttribute flagBFalse = new MetricAttribute("b", 0);

        Assert.assertEquals(metricA1.hashCode(), new MetricAttribute("a", 1).hashCode());
        Assert.assertFalse(metricA1.hashCode() == new MetricAttribute("A", 1).hashCode());
        Assert.assertFalse(metricA1.hashCode() == metricA0.hashCode());
        Assert.assertFalse(metricA1.hashCode() == flagBTrue.hashCode());
        Assert.assertFalse(metricA1.hashCode() == flagBFalse.hashCode());
    }

    public void testToString() throws Throwable {
        final MetricAttribute metricA1 = new MetricAttribute("a", 1);
        final MetricAttribute metricA0 = new MetricAttribute("a", 0);
        final MetricAttribute flagBTrue = new MetricAttribute("b", 1);
        final MetricAttribute flagBFalse = new MetricAttribute("b", 0);

        Assert.assertEquals(metricA1.toString(), new MetricAttribute("a", 1).toString());
        Assert.assertFalse(metricA1.toString().equals(metricA0.toString()));
        Assert.assertFalse(metricA1.toString().equals(flagBTrue.toString()));
        Assert.assertFalse(metricA1.toString().equals(flagBFalse.toString()));
    }

    public void testGetValue() throws Throwable {
        final MetricAttribute metricA1 = new MetricAttribute("a", 1f);
        final MetricAttribute metricA0 = new MetricAttribute("a", 0f);

        Assert.assertEquals(Double.toString(1f), Double.toString(metricA1.getValue()));
        Assert.assertEquals(Double.toString(0f), Double.toString(metricA0.getValue()));
    }
}
