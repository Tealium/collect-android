package com.tealium.collect.attribute;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


public class AttributeGroupTest extends TestCase {

    public void testConstructor() throws Throwable {
        final AttributeGroup<BadgeAttribute> badges = new AttributeGroup<>();
        Assert.assertEquals(0, badges.size());
        Assert.assertEquals(0, badges.toArray().length);
        Assert.assertTrue(badges.isEmpty());
        Assert.assertFalse(badges.iterator().hasNext());
    }

    public void testCollectionsConstructor() throws Throwable {

        List<BadgeAttribute> badgeCollection = new LinkedList<>();
        badgeCollection.add(new BadgeAttribute("0"));

        // Test single element collection
        AttributeGroup<BadgeAttribute> badges = new AttributeGroup<>(badgeCollection);

        Assert.assertEquals(1, badges.size());
        Assert.assertFalse(badges.isEmpty());
        Assert.assertTrue(badges.iterator().hasNext());
        Assert.assertTrue(badges.contains(badgeCollection.get(0)));
        Assert.assertFalse(badges.contains(new BadgeAttribute("3")));

        // Test multi element collection
        badgeCollection.add(new BadgeAttribute("1"));
        badgeCollection.add(new BadgeAttribute("2"));

        badges = new AttributeGroup<>(badgeCollection);

        Assert.assertEquals(3, badges.size());
        Assert.assertFalse(badges.isEmpty());
        Assert.assertTrue(badges.iterator().hasNext());
        Assert.assertTrue(badges.contains(badgeCollection.get(0)));
        Assert.assertTrue(badges.contains(badgeCollection.get(1)));
        Assert.assertTrue(badges.contains(badgeCollection.get(2)));
        Assert.assertFalse(badges.contains(new BadgeAttribute("3")));

        // Test incompatible Attributes
        List<FlagAttribute> flagCollection = new LinkedList<>();
        flagCollection.add(new FlagAttribute("a", true));
        flagCollection.add(new FlagAttribute("a", true));

        try {
            new AttributeGroup<>(flagCollection);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {

        }

        // Test incompatible Attributes
        flagCollection.clear();
        flagCollection.add(new FlagAttribute("a", true));
        flagCollection.add(new FlagAttribute("a", false));

        try {
            new AttributeGroup<>(flagCollection);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {

        }
    }

    public void testContains() throws Throwable {

        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        List<FlagAttribute> flagsList = new ArrayList<>(2);
        flagsList.add(flagATrue);
        flagsList.add(flagBTrue);

        AttributeGroup<FlagAttribute> flags = new AttributeGroup<>(flagsList);
        Assert.assertTrue(flags.contains(flagATrue));
        Assert.assertTrue(flags.contains(flagBTrue));
        Assert.assertFalse(flags.contains(flagAFalse));
        Assert.assertFalse(flags.contains(flagBFalse));
    }

    public void testSize() throws Throwable {

        ArrayList<BadgeAttribute> badgeList = new ArrayList<>(5);

        Assert.assertEquals(0, new AttributeGroup<>().size());
        Assert.assertEquals(0, new AttributeGroup<>(badgeList).size());

        badgeList.add(new BadgeAttribute("0"));

        Assert.assertEquals(1, new AttributeGroup<>(badgeList).size());

        badgeList.add(new BadgeAttribute("1"));
        badgeList.add(new BadgeAttribute("2"));
        badgeList.add(null);
        badgeList.add(new BadgeAttribute("4"));

        Assert.assertEquals(5, badgeList.size()); // Ensure the null was added.
        Assert.assertEquals(4, new AttributeGroup<>(badgeList).size());
    }

    public void testContainsAll() throws Throwable {

        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        final Set<FlagAttribute> flagSetTrue = new HashSet<>(1);
        flagSetTrue.add(flagATrue);

        final Set<FlagAttribute> flagSetFalse = new HashSet<>(1);
        flagSetFalse.add(flagAFalse);

        final Set<FlagAttribute> flagSetTrues = new HashSet<>(2);
        flagSetTrues.add(flagATrue);
        flagSetTrues.add(flagBTrue);

        final Set<FlagAttribute> flagSetFalses = new HashSet<>(2);
        flagSetFalses.add(flagAFalse);
        flagSetFalses.add(flagBFalse);

        final AttributeGroup<FlagAttribute> flagTrue = new AttributeGroup<>(flagSetTrue);
        final AttributeGroup<FlagAttribute> flagFalse = new AttributeGroup<>(flagSetFalse);
        final AttributeGroup<FlagAttribute> flagTrues = new AttributeGroup<>(flagSetTrues);
        final AttributeGroup<FlagAttribute> flagFalses = new AttributeGroup<>(flagSetFalses);

        // Equal count
        Assert.assertTrue(flagTrue.containsAll(flagTrue));
        Assert.assertFalse(flagTrue.containsAll(flagFalse));
        Assert.assertTrue(flagTrues.containsAll(flagTrues));

        // Contained by larger set
        Assert.assertTrue(flagTrues.containsAll(flagTrue));
        Assert.assertFalse(flagFalses.containsAll(flagTrue));

        // Identical ids, different values
        Assert.assertFalse(flagTrues.containsAll(flagFalses));

        // Small contains larger
        Assert.assertFalse(flagTrue.containsAll(flagTrues));
    }

    public void testContainsAllIds() throws Throwable {

        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);
        final FlagAttribute flagCTrue = new FlagAttribute("c", true);

        final Set<FlagAttribute> flagSetTrue = new HashSet<>(1);
        flagSetTrue.add(flagATrue);

        final Set<FlagAttribute> flagSetFalse = new HashSet<>(1);
        flagSetFalse.add(flagAFalse);

        final Set<FlagAttribute> flagSetTruesAB = new HashSet<>(2);
        flagSetTruesAB.add(flagATrue);
        flagSetTruesAB.add(flagBTrue);

        final Set<FlagAttribute> flagSetTruesAC = new HashSet<>(2);
        flagSetTruesAC.add(flagATrue);
        flagSetTruesAC.add(flagCTrue);

        final Set<FlagAttribute> flagSetFalses = new HashSet<>(2);
        flagSetFalses.add(flagAFalse);
        flagSetFalses.add(flagBFalse);

        final AttributeGroup<FlagAttribute> flagTrue = new AttributeGroup<>(flagSetTrue);
        final AttributeGroup<FlagAttribute> flagFalse = new AttributeGroup<>(flagSetFalse);
        final AttributeGroup<FlagAttribute> flagTruesAB = new AttributeGroup<>(flagSetTruesAB);
        final AttributeGroup<FlagAttribute> flagTruesAC = new AttributeGroup<>(flagSetTruesAC);
        final AttributeGroup<FlagAttribute> flagFalses = new AttributeGroup<>(flagSetFalses);

        // Equal count
        Assert.assertTrue(flagTrue.containsAllIds(flagTrue));
        Assert.assertTrue(flagTrue.containsAllIds(flagFalse));
        Assert.assertTrue(flagTruesAB.containsAllIds(flagTruesAB));
        Assert.assertTrue(flagTruesAB.containsAllIds(flagFalses));
        Assert.assertFalse(flagTruesAB.containsAllIds(flagTruesAC));

        // Contained by larger set
        Assert.assertTrue(flagTruesAB.containsAllIds(flagTrue));
        Assert.assertTrue(flagFalses.containsAllIds(flagTrue));

        // Identical ids, different values
        Assert.assertTrue(flagTruesAB.containsAllIds(flagFalses));

        // Small contains larger
        Assert.assertFalse(flagTrue.containsAllIds(flagTruesAB));
    }

    public void testContainsIdString() throws Throwable {

        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        Set<FlagAttribute> flagSetTrues = new HashSet<>(2);
        flagSetTrues.add(flagATrue);
        flagSetTrues.add(flagBTrue);

        Set<FlagAttribute> flagSetFalses = new HashSet<>(2);
        flagSetFalses.add(flagAFalse);
        flagSetFalses.add(flagBFalse);

        final AttributeGroup<FlagAttribute> flagTrues = new AttributeGroup<>(flagSetTrues);
        final AttributeGroup<FlagAttribute> flagFalses = new AttributeGroup<>(flagSetFalses);

        Assert.assertTrue(flagTrues.containsId("a"));
        Assert.assertTrue(flagFalses.containsId("a"));
        Assert.assertFalse(flagTrues.containsId("A"));
        Assert.assertFalse(flagTrues.containsId((String) null));
        Assert.assertFalse(flagTrues.containsId(""));
    }

    public void testContainsIdAttribute() throws Throwable {

        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        Set<FlagAttribute> flagSetTrues = new HashSet<>(2);
        flagSetTrues.add(flagATrue);
        flagSetTrues.add(flagBTrue);

        Set<FlagAttribute> flagSetFalses = new HashSet<>(2);
        flagSetFalses.add(flagAFalse);
        flagSetFalses.add(flagBFalse);

        final AttributeGroup<FlagAttribute> flagTrues = new AttributeGroup<>(flagSetTrues);
        final AttributeGroup<FlagAttribute> flagFalses = new AttributeGroup<>(flagSetFalses);

        Assert.assertTrue(flagTrues.containsId(flagATrue));
        Assert.assertTrue(flagFalses.containsId(flagATrue));
        Assert.assertFalse(flagTrues.containsId((FlagAttribute) null));
    }

    public void testGet() throws Throwable {

        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        Set<FlagAttribute> flagSetTrues = new HashSet<>(2);
        flagSetTrues.add(flagATrue);
        flagSetTrues.add(flagBTrue);

        Set<FlagAttribute> flagSetFalses = new HashSet<>(2);
        flagSetFalses.add(flagAFalse);
        flagSetFalses.add(flagBFalse);

        final AttributeGroup<FlagAttribute> flagTrues = new AttributeGroup<>(flagSetTrues);
        final AttributeGroup<FlagAttribute> flagFalses = new AttributeGroup<>(flagSetFalses);

        Assert.assertEquals(flagATrue, flagTrues.get(flagATrue.getId()));
        Assert.assertEquals(flagAFalse, flagFalses.get(flagATrue.getId()));
        Assert.assertNull(flagTrues.get(""));
        Assert.assertNull(flagTrues.get(null));
    }

    public void testIsEmpty() throws Throwable {

        List<BadgeAttribute> badges = new ArrayList<>(1);

        Assert.assertTrue(new AttributeGroup<BadgeAttribute>().isEmpty());
        Assert.assertTrue(new AttributeGroup<>(badges).isEmpty());

        badges.add(new BadgeAttribute("a"));

        Assert.assertFalse(new AttributeGroup<>(badges).isEmpty());
    }

    public void testEquals() throws Throwable {
        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        final Set<FlagAttribute> setAs = new HashSet<>(1);
        setAs.add(flagATrue);

        final Set<FlagAttribute> setTrues = new HashSet<>(2);
        setTrues.add(flagATrue);
        setTrues.add(flagBTrue);

        final Set<FlagAttribute> setFalses = new HashSet<>(2);
        setFalses.add(flagAFalse);
        setFalses.add(flagBFalse);

        final AttributeGroup<FlagAttribute> trueAttrs = new AttributeGroup<>(setTrues);

        Assert.assertTrue(trueAttrs.equals(new AttributeGroup<>(setTrues)));
        Assert.assertFalse(trueAttrs.equals(new AttributeGroup<>(setFalses)));
        Assert.assertFalse(trueAttrs.equals(new AttributeGroup<>(setAs)));
    }

    public void testEqualIds() throws Throwable {
        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        final Set<FlagAttribute> setAs = new HashSet<>(1);
        setAs.add(flagATrue);

        final Set<FlagAttribute> setTrues = new HashSet<>(2);
        setTrues.add(flagATrue);
        setTrues.add(flagBTrue);

        final Set<FlagAttribute> setFalses = new HashSet<>(2);
        setFalses.add(flagAFalse);
        setFalses.add(flagBFalse);

        final AttributeGroup<FlagAttribute> trueAttrs = new AttributeGroup<>(setTrues);

        Assert.assertTrue(trueAttrs.equalIds(new AttributeGroup<>(setTrues)));
        Assert.assertTrue(trueAttrs.equalIds(new AttributeGroup<>(setFalses)));
        Assert.assertFalse(trueAttrs.equalIds(new AttributeGroup<>(setAs)));
    }

    public void testHashCode() throws Throwable {

        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        final Set<FlagAttribute> setAs = new HashSet<>(1);
        setAs.add(flagATrue);

        final Set<FlagAttribute> setTrues = new HashSet<>(2);
        setTrues.add(flagATrue);
        setTrues.add(flagBTrue);

        final Set<FlagAttribute> setFalses = new HashSet<>(2);
        setFalses.add(flagAFalse);
        setFalses.add(flagBFalse);

        final AttributeGroup<FlagAttribute> trueAttrs = new AttributeGroup<>(setTrues);

        Assert.assertEquals(trueAttrs.hashCode(), new AttributeGroup<>(setTrues).hashCode());
        Assert.assertFalse(trueAttrs.hashCode() == new AttributeGroup<>(setFalses).hashCode());
        Assert.assertFalse(trueAttrs.hashCode() == new AttributeGroup<>(setAs).hashCode());
    }

    public void testIterator() throws Throwable {

        List<BadgeAttribute> badges = new ArrayList<>(1);

        Assert.assertNotNull(new AttributeGroup<BadgeAttribute>().iterator());
        Assert.assertNotNull(new AttributeGroup<>(badges).iterator());

        badges.add(new BadgeAttribute("a"));

        Assert.assertNotNull(new AttributeGroup<>(badges).iterator());
    }

    public void testIterableHasNext() throws Throwable {
        List<BadgeAttribute> badges = new ArrayList<>(1);

        Assert.assertFalse(new AttributeGroup<BadgeAttribute>().iterator().hasNext());
        Assert.assertFalse(new AttributeGroup<>(badges).iterator().hasNext());

        badges.add(new BadgeAttribute("a"));

        Iterator<BadgeAttribute> i = new AttributeGroup<>(badges).iterator();

        Assert.assertTrue(i.hasNext());
        i.next();
        Assert.assertFalse(i.hasNext());
    }

    public void testIterableNext() throws Throwable {

        List<BadgeAttribute> badges = new ArrayList<>(1);

        try {
            new AttributeGroup<BadgeAttribute>().iterator().next();
            Assert.fail();
        } catch (NoSuchElementException ignored) {
        }

        try {
            new AttributeGroup<>(badges).iterator().next();
            Assert.fail();
        } catch (NoSuchElementException ignored) {
        }

        badges.add(new BadgeAttribute("a"));

        Iterator<BadgeAttribute> i = new AttributeGroup<>(badges).iterator();
        i.next();
        try {
            i.next();
            Assert.fail();
        } catch (NoSuchElementException ignored) {
        }
    }

    public void testIterableRemove() throws Throwable {

        List<BadgeAttribute> badges = new ArrayList<>(1);

        try {
            new AttributeGroup<BadgeAttribute>().iterator().remove();
            Assert.fail();
        } catch (UnsupportedOperationException ignored) {
        }

        try {
            new AttributeGroup<>(badges).iterator().remove();
            Assert.fail();
        } catch (UnsupportedOperationException ignored) {
        }

        badges.add(new BadgeAttribute("a"));

        try {
            new AttributeGroup<>(badges).iterator().remove();
            Assert.fail();
        } catch (UnsupportedOperationException ignored) {
        }
    }

    public void testToArray() throws Throwable {

        List<BadgeAttribute> badges = new ArrayList<>(1);

        Assert.assertEquals(0, new AttributeGroup<BadgeAttribute>().toArray().length);
        Assert.assertEquals(0, new AttributeGroup<>(badges).toArray().length);

        badges.add(new BadgeAttribute("a"));

        Object[] badgeArray = new AttributeGroup<>(badges).toArray();

        Assert.assertEquals(1, badgeArray.length);
        Assert.assertEquals(new BadgeAttribute("a"), badgeArray[0]);
    }

    public void testToArrayT() throws Throwable {
        List<BadgeAttribute> badges = new ArrayList<>(1);

        Assert.assertEquals(0, new AttributeGroup<BadgeAttribute>()
                .toArray(new BadgeAttribute[0]).length);
        Assert.assertEquals(1, new AttributeGroup<BadgeAttribute>()
                .toArray(new BadgeAttribute[1]).length);

        Assert.assertEquals(0, new AttributeGroup<>(badges)
                .toArray(new BadgeAttribute[0]).length);
        Assert.assertEquals(1, new AttributeGroup<>(badges)
                .toArray(new BadgeAttribute[1]).length);

        badges.add(new BadgeAttribute("a"));

        Assert.assertEquals(1, new AttributeGroup<>(badges)
                .toArray(new BadgeAttribute[0]).length);
        Assert.assertEquals(1, new AttributeGroup<>(badges)
                .toArray(new BadgeAttribute[1]).length);
        Assert.assertEquals(3, new AttributeGroup<>(badges)
                .toArray(new BadgeAttribute[3]).length);
    }

    public void testToString() throws Throwable {
        final FlagAttribute flagATrue = new FlagAttribute("a", true);
        final FlagAttribute flagAFalse = new FlagAttribute("a", false);
        final FlagAttribute flagBTrue = new FlagAttribute("b", true);
        final FlagAttribute flagBFalse = new FlagAttribute("b", false);

        final Set<FlagAttribute> setAs = new HashSet<>(1);
        setAs.add(flagATrue);

        final Set<FlagAttribute> setTrues = new HashSet<>(2);
        setTrues.add(flagATrue);
        setTrues.add(flagBTrue);

        final Set<FlagAttribute> setFalses = new HashSet<>(2);
        setFalses.add(flagAFalse);
        setFalses.add(flagBFalse);

        final AttributeGroup<FlagAttribute> trueAttrs = new AttributeGroup<>(setTrues);

        Assert.assertEquals(trueAttrs.toString(), new AttributeGroup<>(setTrues).toString());
        Assert.assertFalse(trueAttrs.toString().equals(new AttributeGroup<>(setFalses).toString()));
        Assert.assertFalse(trueAttrs.toString().equals(new AttributeGroup<>(setAs).toString()));
    }
}
