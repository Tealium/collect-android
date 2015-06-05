package com.tealium.collect.attribute;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AudienceAttributeTest extends TestCase {


    public void testConstructor() throws Throwable {

        try {
            new AudienceAttribute(null, "");
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new AudienceAttribute(null, null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new AudienceAttribute("", "");
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new AudienceAttribute("", null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        new AudienceAttribute("a", "true");
        new AudienceAttribute("a", "false");

        new AudienceAttribute("The Lord is my shepherd, I shall not want.", "true");
        new AudienceAttribute("The Lord is my shepherd, I shall not want.", "false");
    }

    public void testEquals() throws Throwable {

        final AudienceAttribute audienceATrue = new AudienceAttribute("a", "true");
        final AudienceAttribute audienceAFalse = new AudienceAttribute("a", "false");
        final AudienceAttribute audienceBTrue = new AudienceAttribute("b", "true");
        final AudienceAttribute audienceBFalse = new AudienceAttribute("b", "false");

        Assert.assertTrue(audienceATrue.equals(audienceATrue));
        Assert.assertFalse(audienceATrue.equals(audienceBTrue));
        Assert.assertFalse(audienceATrue.equals(audienceAFalse));
        Assert.assertFalse(audienceATrue.equals(audienceBFalse));
        Assert.assertFalse(audienceATrue.equals(null));
        Assert.assertTrue(audienceATrue.equals(new AudienceAttribute("a", "true")));
        Assert.assertFalse(audienceATrue.equals(new AudienceAttribute("A", "true")));
    }

    public void testHashCode() throws Throwable {

        final AudienceAttribute audienceATrue = new AudienceAttribute("a", "true");
        final AudienceAttribute audienceAFalse = new AudienceAttribute("a", "false");
        final AudienceAttribute audienceBTrue = new AudienceAttribute("b", "true");
        final AudienceAttribute audienceBFalse = new AudienceAttribute("b", "false");

        Assert.assertEquals(audienceATrue.hashCode(), new AudienceAttribute("a", "true").hashCode());
        Assert.assertFalse(audienceATrue.hashCode() == new AudienceAttribute("A", "true").hashCode());
        Assert.assertFalse(audienceATrue.hashCode() == audienceAFalse.hashCode());
        Assert.assertFalse(audienceATrue.hashCode() == audienceBTrue.hashCode());
        Assert.assertFalse(audienceATrue.hashCode() == audienceBFalse.hashCode());
    }

    public void testToString() throws Throwable {
        final AudienceAttribute audienceATrue = new AudienceAttribute("a", "true");
        final AudienceAttribute audienceAFalse = new AudienceAttribute("a", "false");
        final AudienceAttribute audienceBTrue = new AudienceAttribute("b", "true");
        final AudienceAttribute audienceBFalse = new AudienceAttribute("b", "false");

        Assert.assertEquals(audienceATrue.toString(), new AudienceAttribute("a", "true").toString());
        Assert.assertFalse(audienceATrue.toString().equals(audienceAFalse.toString()));
        Assert.assertFalse(audienceATrue.toString().equals(audienceBTrue.toString()));
        Assert.assertFalse(audienceATrue.toString().equals(audienceBFalse.toString()));
    }

    public void testGetName() throws Throwable {
        final AudienceAttribute audienceATrue = new AudienceAttribute("a", "true");
        final AudienceAttribute audienceAFalse = new AudienceAttribute("a", "false");

        Assert.assertEquals("true", audienceATrue.getName());
        Assert.assertEquals("false", audienceAFalse.getName());
    }
}
