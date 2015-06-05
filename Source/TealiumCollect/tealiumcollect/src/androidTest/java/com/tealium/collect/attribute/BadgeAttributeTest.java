package com.tealium.collect.attribute;

import junit.framework.Assert;
import junit.framework.TestCase;

public class BadgeAttributeTest extends TestCase {

    public void testConstructor() throws Throwable {

        try {
            new BadgeAttribute(null);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new BadgeAttribute("");
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        new BadgeAttribute("a");

        new BadgeAttribute("The Lord is my shepherd, I shall not want.");
    }

    public void testEquals() throws Throwable {

        final BadgeAttribute badgeA = new BadgeAttribute("a");
        final BadgeAttribute badgeB = new BadgeAttribute("b");

        Assert.assertTrue(badgeA.equals(badgeA));
        Assert.assertFalse(badgeA.equals(badgeB));
        Assert.assertFalse(badgeA.equals(null));
        Assert.assertTrue(badgeA.equals(new BadgeAttribute("a")));
        Assert.assertFalse(badgeA.equals(new BadgeAttribute("A")));
        Assert.assertFalse(badgeA.equals(new BaseAttribute("a") {
        }));
    }

    public void testHashCode() throws Throwable {

        final BadgeAttribute badgeA = new BadgeAttribute("a");
        final BadgeAttribute badgeB = new BadgeAttribute("b");

        Assert.assertEquals(badgeA.hashCode(), new BadgeAttribute("a").hashCode());
        Assert.assertFalse(badgeA.hashCode() == badgeB.hashCode());
    }

    public void testToString() throws Throwable {
        final BadgeAttribute badgeA = new BadgeAttribute("a");
        final BadgeAttribute badgeB = new BadgeAttribute("b");

        Assert.assertEquals(badgeA.toString(), new BadgeAttribute("a").toString());
        Assert.assertFalse(badgeA.toString().equals(badgeB.toString()));
    }

}
