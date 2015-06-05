package com.tealium.collect.visitor;

import com.tealium.collect.attribute.DateAttribute;
import com.tealium.collect.attribute.FlagAttribute;
import com.tealium.collect.attribute.MetricAttribute;
import com.tealium.collect.attribute.PropertyAttribute;
import com.tealium.collect.testutil.TestUtil;

import junit.framework.Assert;
import junit.framework.TestCase;

@SuppressWarnings({"ObjectEqualsNull", "EqualsBetweenInconvertibleTypes"})
public class CurrentVisitTest extends TestCase {

    public void testConstructor() throws Throwable {
        CurrentVisit cv = new CurrentVisit(0, null, null, null, null, 1, 2);
        Assert.assertEquals(0, cv.getCreationTimestamp());
        Assert.assertEquals(0, cv.getDates().size());
        Assert.assertEquals(0, cv.getFlags().size());
        Assert.assertEquals(0, cv.getMetrics().size());
        Assert.assertEquals(0, cv.getProperties().size());
        Assert.assertEquals(1, cv.getLastEventTimestamp());
        Assert.assertEquals(2, cv.getTotalEventCount());
    }

    public void testEquals() throws Throwable {
        final CurrentVisit oldVisit = new CurrentVisit();

        Assert.assertFalse(oldVisit.equals(null));
        Assert.assertFalse(oldVisit.equals("something"));
        Assert.assertTrue(oldVisit.equals(new CurrentVisit()));
        Assert.assertFalse(oldVisit.equals(createNewCurrentVisit()));
    }

    public void testToString() throws Throwable {
        final CurrentVisit oldVisit = new CurrentVisit();

        Assert.assertTrue(oldVisit.toString().equals(new CurrentVisit().toString()));
        Assert.assertFalse(oldVisit.toString().equals(createNewCurrentVisit().toString()));
    }

    public void testHashCode() throws Throwable {
        final CurrentVisit oldVisit = new CurrentVisit();
        final CurrentVisit currentVisit = createNewCurrentVisit();

        Assert.assertEquals(oldVisit.hashCode(), new CurrentVisit().hashCode());
        Assert.assertTrue(oldVisit.hashCode() != currentVisit.hashCode());
    }

    private CurrentVisit createNewCurrentVisit() {
        return new CurrentVisit(
                1,
                TestUtil.createAttrCollection(new DateAttribute("a", 0)),
                TestUtil.createAttrCollection(new FlagAttribute("a", false)),
                TestUtil.createAttrCollection(new MetricAttribute("a", 0)),
                TestUtil.createAttrCollection(new PropertyAttribute("a", null)),
                1,
                1);
    }

}


