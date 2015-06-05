package com.tealium.collect.attribute;

import junit.framework.Assert;
import junit.framework.TestCase;

public class FlagAttributeTest extends TestCase {

    public void testConstructor() throws Throwable {

        try {
            new FlagAttribute(null, true);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new FlagAttribute(null, false);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new FlagAttribute("", true);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new FlagAttribute("", false);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        new FlagAttribute("a", true);
        new FlagAttribute("a", false);

        new FlagAttribute("The Lord is my shepherd, I shall not want.", true);
        new FlagAttribute("The Lord is my shepherd, I shall not want.", false);
    }

    public void testEquals() throws Throwable {

        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        Assert.assertTrue(flagATrue.equals(flagATrue));
        Assert.assertFalse(flagATrue.equals(flagBTrue));
        Assert.assertFalse(flagATrue.equals(flagAFalse));
        Assert.assertFalse(flagATrue.equals(flagBFalse));
        Assert.assertFalse(flagATrue.equals(null));
        Assert.assertTrue(flagATrue.equals(new FlagAttribute("a", true)));
        Assert.assertFalse(flagATrue.equals(new FlagAttribute("A", true)));
    }

    public void testHashCode() throws Throwable {

        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        Assert.assertEquals(flagATrue.hashCode(), new FlagAttribute("a", true).hashCode());
        Assert.assertFalse(flagATrue.hashCode() == new FlagAttribute("A", true).hashCode());
        Assert.assertFalse(flagATrue.hashCode() == flagAFalse.hashCode());
        Assert.assertFalse(flagATrue.hashCode() == flagBTrue.hashCode());
        Assert.assertFalse(flagATrue.hashCode() == flagBFalse.hashCode());
    }

    public void testToString() throws Throwable {
        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        Assert.assertEquals(flagATrue.toString(), new FlagAttribute("a", true).toString());
        Assert.assertFalse(flagATrue.toString().equals(flagAFalse.toString()));
        Assert.assertFalse(flagATrue.toString().equals(flagBTrue.toString()));
        Assert.assertFalse(flagATrue.toString().equals(flagBFalse.toString()));
    }

    public void testGetValue() throws Throwable {
        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);

        Assert.assertTrue(flagATrue.getValue());
        Assert.assertFalse(flagAFalse.getValue());
    }
}
