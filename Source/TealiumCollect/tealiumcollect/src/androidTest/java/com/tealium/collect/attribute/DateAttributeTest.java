package com.tealium.collect.attribute;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DateAttributeTest extends TestCase {

    public void testConstructor() throws Throwable {

        try {
            new DateAttribute(null, 0);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new DateAttribute(null, 0);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new DateAttribute("", Long.MAX_VALUE);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        try {
            new DateAttribute("", Long.MAX_VALUE);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {
        }

        new DateAttribute("a", Long.MAX_VALUE);
        new DateAttribute("a", 0);

        new DateAttribute("The Lord is my shepherd, I shall not want.", Long.MAX_VALUE);
        new DateAttribute("The Lord is my shepherd, I shall not want.", 0);
    }

    public void testEquals() throws Throwable {

        final DateAttribute dateAMax = new DateAttribute("a", Long.MAX_VALUE);
        final DateAttribute dateAZero = new DateAttribute("a", 0);
        final DateAttribute dateBMax = new DateAttribute("b", Long.MAX_VALUE);
        final DateAttribute dateBZero = new DateAttribute("b", 0);

        Assert.assertTrue(dateAMax.equals(dateAMax));
        Assert.assertFalse(dateAMax.equals(dateBMax));
        Assert.assertFalse(dateAMax.equals(dateAZero));
        Assert.assertFalse(dateAMax.equals(dateBZero));
        Assert.assertFalse(dateAMax.equals(null));
        Assert.assertTrue(dateAMax.equals(new DateAttribute("a", Long.MAX_VALUE)));
        Assert.assertFalse(dateAMax.equals(new DateAttribute("A", Long.MAX_VALUE)));
    }

    public void testHashCode() throws Throwable {

        final DateAttribute dateAMax = new DateAttribute("a", Long.MAX_VALUE);
        final DateAttribute dateAZero = new DateAttribute("a", 0);
        final DateAttribute dateBMax = new DateAttribute("b", Long.MAX_VALUE);
        final DateAttribute dateBZero = new DateAttribute("b", 0);

        Assert.assertEquals(dateAMax.hashCode(), new DateAttribute("a", Long.MAX_VALUE).hashCode());
        Assert.assertFalse(dateAMax.hashCode() == new DateAttribute("A", Long.MAX_VALUE).hashCode());
        Assert.assertFalse(dateAMax.hashCode() == dateAZero.hashCode());
        Assert.assertFalse(dateAMax.hashCode() == dateBMax.hashCode());
        Assert.assertFalse(dateAMax.hashCode() == dateBZero.hashCode());
    }

    public void testToString() throws Throwable {
        final DateAttribute dateAMax = new DateAttribute("a", Long.MAX_VALUE);
        final DateAttribute dateAZero = new DateAttribute("a", 0);
        final DateAttribute dateBMax = new DateAttribute("b", Long.MAX_VALUE);
        final DateAttribute dateBZero = new DateAttribute("b", 0);

        Assert.assertEquals(dateAMax.toString(), new DateAttribute("a", Long.MAX_VALUE).toString());
        Assert.assertFalse(dateAMax.toString().equals(dateAZero.toString()));
        Assert.assertFalse(dateAMax.toString().equals(dateBMax.toString()));
        Assert.assertFalse(dateAMax.toString().equals(dateBZero.toString()));
    }

    public void testGetValue() throws Throwable {
        final DateAttribute dateAMax = new DateAttribute("a", Long.MAX_VALUE);
        final DateAttribute dateAZero = new DateAttribute("a", 0);

        Assert.assertEquals(Long.MAX_VALUE, dateAMax.getTime());
        Assert.assertEquals(0, dateAZero.getTime());
    }
}
