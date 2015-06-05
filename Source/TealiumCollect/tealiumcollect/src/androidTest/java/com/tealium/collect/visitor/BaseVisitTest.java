package com.tealium.collect.visitor;

import com.tealium.collect.attribute.DateAttribute;
import com.tealium.collect.attribute.FlagAttribute;
import com.tealium.collect.attribute.MetricAttribute;
import com.tealium.collect.attribute.PropertyAttribute;
import com.tealium.collect.testutil.TestUtil;

import junit.framework.Assert;
import junit.framework.TestCase;


@SuppressWarnings({"ObjectEqualsNull", "EqualsBetweenInconvertibleTypes"})
public class BaseVisitTest extends TestCase {

    public void testConstructor() throws Throwable {
        BaseVisit cv = new BaseVisit(0, null, null, null, null);
        Assert.assertEquals(0, cv.getCreationTimestamp());
        Assert.assertEquals(0, cv.getDates().size());
        Assert.assertEquals(0, cv.getFlags().size());
        Assert.assertEquals(0, cv.getMetrics().size());
        Assert.assertEquals(0, cv.getProperties().size());
    }

    public void testEquals() throws Throwable {
        final BaseVisit visit = new BaseVisit();

        Assert.assertFalse(visit.equals(null));
        Assert.assertFalse(visit.equals("something"));
        Assert.assertTrue(visit.equals(new BaseVisit(0, null, null, null, null)));
        Assert.assertFalse(visit.equals(createNewBaseVisit()));
    }

    public void testHashCode() throws Throwable {
        final BaseVisit oldVisit = new BaseVisit();
        final BaseVisit currentVisit = createNewBaseVisit();

        Assert.assertEquals(oldVisit.hashCode(), new BaseVisit(0, null, null, null, null).hashCode());
        Assert.assertTrue(oldVisit.hashCode() != currentVisit.hashCode());
    }

    private BaseVisit createNewBaseVisit() {
        return new BaseVisit(
                1,
                TestUtil.createAttrCollection(new DateAttribute("a", 0)),
                TestUtil.createAttrCollection(new FlagAttribute("a", false)),
                TestUtil.createAttrCollection(new MetricAttribute("a", 0)),
                TestUtil.createAttrCollection(new PropertyAttribute("a", null)));
    }
}
