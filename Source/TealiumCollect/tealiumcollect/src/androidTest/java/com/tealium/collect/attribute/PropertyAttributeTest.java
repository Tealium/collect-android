package com.tealium.collect.attribute;

import junit.framework.Assert;
import junit.framework.TestCase;

public class PropertyAttributeTest extends TestCase {

    public void testConstructor() throws Throwable {

        try {
            new PropertyAttribute(null, "true");
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new PropertyAttribute(null, null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new PropertyAttribute("", "true");
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new PropertyAttribute("", null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        new PropertyAttribute("a", "true");
        new PropertyAttribute("a", null);

        new PropertyAttribute("The Lord is my shepherd, I shall not want.", "true");
        new PropertyAttribute("The Lord is my shepherd, I shall not want.", null);
    }

    public void testEquals() throws Throwable {

        final PropertyAttribute propertyATrue = new PropertyAttribute("a", "true");
        final PropertyAttribute propertyANull = new PropertyAttribute("a", null);
        final PropertyAttribute flagBTrue = new PropertyAttribute("b", "true");
        final PropertyAttribute flagBFalse = new PropertyAttribute("b", null);

        Assert.assertTrue(propertyATrue.equals(propertyATrue));
        Assert.assertFalse(propertyATrue.equals(flagBTrue));
        Assert.assertFalse(propertyATrue.equals(propertyANull));
        Assert.assertFalse(propertyATrue.equals(flagBFalse));
        Assert.assertFalse(propertyATrue.equals(null));
        Assert.assertTrue(propertyATrue.equals(new PropertyAttribute("a", "true")));
        Assert.assertFalse(propertyATrue.equals(new PropertyAttribute("A", "true")));
    }

    public void testHashCode() throws Throwable {

        final PropertyAttribute propertyATrue = new PropertyAttribute("a", "true");
        final PropertyAttribute propertyANull = new PropertyAttribute("a", null);
        final PropertyAttribute flagBTrue = new PropertyAttribute("b", "true");
        final PropertyAttribute flagBFalse = new PropertyAttribute("b", null);

        Assert.assertEquals(propertyATrue.hashCode(), new PropertyAttribute("a", "true").hashCode());
        Assert.assertFalse(propertyATrue.hashCode() == new PropertyAttribute("A", "true").hashCode());
        Assert.assertFalse(propertyATrue.hashCode() == propertyANull.hashCode());
        Assert.assertFalse(propertyATrue.hashCode() == flagBTrue.hashCode());
        Assert.assertFalse(propertyATrue.hashCode() == flagBFalse.hashCode());
    }

    public void testToString() throws Throwable {
        final PropertyAttribute propertyATrue = new PropertyAttribute("a", "true");
        final PropertyAttribute propertyANull = new PropertyAttribute("a", null);
        final PropertyAttribute flagBTrue = new PropertyAttribute("b", "true");
        final PropertyAttribute flagBFalse = new PropertyAttribute("b", null);

        Assert.assertEquals(propertyATrue.toString(), new PropertyAttribute("a", "true").toString());
        Assert.assertFalse(propertyATrue.toString().equals(propertyANull.toString()));
        Assert.assertFalse(propertyATrue.toString().equals(flagBTrue.toString()));
        Assert.assertFalse(propertyATrue.toString().equals(flagBFalse.toString()));
    }

    public void testGetValue() throws Throwable {
        final PropertyAttribute propertyATrue = new PropertyAttribute("a", "true");
        final PropertyAttribute propertyANull = new PropertyAttribute("a", null);

        Assert.assertNotNull(propertyATrue.getValue());
        Assert.assertNull(propertyANull.getValue());
    }
}
