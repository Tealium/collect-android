package com.tealium.collect.visitor;

import com.tealium.collect.attribute.AudienceAttribute;
import com.tealium.collect.attribute.BadgeAttribute;
import com.tealium.collect.attribute.DateAttribute;
import com.tealium.collect.attribute.FlagAttribute;
import com.tealium.collect.attribute.MetricAttribute;
import com.tealium.collect.attribute.PropertyAttribute;
import com.tealium.collect.testutil.TestUtil;

import junit.framework.Assert;
import junit.framework.TestCase;

@SuppressWarnings({"ObjectEqualsNull", "EqualsBetweenInconvertibleTypes"})
public class VisitorProfileTest extends TestCase {

    public void testBuilder() throws Throwable {

        final long now = System.currentTimeMillis();

        VisitorProfile visitorProfile = new VisitorProfile.Builder()
                .setAudiences(TestUtil.createAttrCollection(new AudienceAttribute("a", "b")))
                .setBadges(TestUtil.createAttrCollection(new BadgeAttribute("a")))
                .setCreationDate(now)
                .setCurrentVisit(new CurrentVisit())
                .setDates(TestUtil.createAttrCollection(new DateAttribute("a", 0)))
                .setFlags(TestUtil.createAttrCollection(new FlagAttribute("a", false)))
                .setIsNewVisitor(false)
                .setMetrics(TestUtil.createAttrCollection(new MetricAttribute("a", 0)))
                .setProperties(TestUtil.createAttrCollection(new PropertyAttribute("a", null)))
                .build();

        Assert.assertEquals(now, visitorProfile.getCreationTimestamp());
        Assert.assertEquals(1, visitorProfile.getAudiences().size());
        Assert.assertEquals(1, visitorProfile.getBadges().size());
        Assert.assertEquals(1, visitorProfile.getDates().size());
        Assert.assertEquals(1, visitorProfile.getFlags().size());
        Assert.assertEquals(1, visitorProfile.getMetrics().size());
        Assert.assertEquals(1, visitorProfile.getProperties().size());
        Assert.assertFalse(visitorProfile.isNewVisitor());
    }

    public void testEquals() throws Throwable {
        final VisitorProfile oldVisitorProfile = new VisitorProfile.Builder().build();

        Assert.assertFalse(oldVisitorProfile.equals(null));
        Assert.assertFalse(oldVisitorProfile.equals("something"));
        Assert.assertTrue(oldVisitorProfile.equals(new VisitorProfile.Builder().build()));
        Assert.assertFalse(oldVisitorProfile.equals(createNewProfile()));
    }

    public void testToString() throws Throwable {
        final VisitorProfile oldVisitorProfile = new VisitorProfile.Builder().build();

        Assert.assertTrue(oldVisitorProfile.toString().equals(new VisitorProfile.Builder().build().toString()));
        Assert.assertFalse(oldVisitorProfile.toString().equals(createNewProfile().toString()));
    }

    public void testHashCode() throws Throwable {
        final VisitorProfile oldVisitorProfile = new VisitorProfile.Builder().build();
        final VisitorProfile newVisitorProfile = createNewProfile();

        Assert.assertEquals(oldVisitorProfile.hashCode(), new VisitorProfile.Builder().build().hashCode());
        Assert.assertTrue(oldVisitorProfile.hashCode() != newVisitorProfile.hashCode());
    }

    public VisitorProfile createNewProfile() {
        return new VisitorProfile.Builder()
                .setAudiences(TestUtil.createAttrCollection(new AudienceAttribute("a", "b")))
                .setBadges(TestUtil.createAttrCollection(new BadgeAttribute("a")))
                .setCreationDate(System.currentTimeMillis())
                .setCurrentVisit(new CurrentVisit())
                .setDates(TestUtil.createAttrCollection(new DateAttribute("a", 0)))
                .setFlags(TestUtil.createAttrCollection(new FlagAttribute("a", false)))
                .setIsNewVisitor(false)
                .setMetrics(TestUtil.createAttrCollection(new MetricAttribute("a", 0)))
                .setProperties(TestUtil.createAttrCollection(new PropertyAttribute("a", null)))
                .build();
    }
}
