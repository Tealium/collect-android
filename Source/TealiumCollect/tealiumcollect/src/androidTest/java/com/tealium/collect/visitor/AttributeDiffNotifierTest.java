package com.tealium.collect.visitor;

import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.AudienceAttribute;
import com.tealium.collect.attribute.BaseAttribute;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

public class AttributeDiffNotifierTest extends TestCase {

    public void testConstructor() throws Throwable {

        AttributeDiffNotifier adn = createAttrDiffNotifier(null, null);
        Assert.assertEquals(0, adn.getRemovedAttributes().size());
        Assert.assertEquals(0, adn.getModifiedAttributes().size());
        Assert.assertEquals(0, adn.getAddedAttributes().size());

        adn = createAttrDiffNotifier(createOldAudienceGroup(), null);
        Assert.assertEquals(1, adn.getRemovedAttributes().size());
        Assert.assertEquals(0, adn.getModifiedAttributes().size());
        Assert.assertEquals(0, adn.getAddedAttributes().size());

        adn = createAttrDiffNotifier(null, createNewAudienceGroup());
        Assert.assertEquals(0, adn.getRemovedAttributes().size());
        Assert.assertEquals(0, adn.getModifiedAttributes().size());
        Assert.assertEquals(1, adn.getAddedAttributes().size());

        adn = createAttrDiffNotifier(createOldAudienceGroup(), createNewAudienceGroup());
        Assert.assertEquals(1, adn.getRemovedAttributes().size());
        Assert.assertEquals(0, adn.getModifiedAttributes().size());
        Assert.assertEquals(1, adn.getAddedAttributes().size());

        adn = createAttrDiffNotifier(createOldAudienceGroup(), createUpdatedAudienceGroup());
        Assert.assertEquals(0, adn.getRemovedAttributes().size());
        Assert.assertEquals(1, adn.getModifiedAttributes().size());
        Assert.assertEquals(0, adn.getAddedAttributes().size());
    }

    private static AudienceAttribute createNewAudience() {
        return new AudienceAttribute("new_audience", "New Audience");
    }

    private static AudienceAttribute createUpdatedAudience() {
        return new AudienceAttribute("old_audience", "New Name");
    }

    private static AudienceAttribute createOldAudience() {
        return new AudienceAttribute("old_audience", "Old Audience");
    }

    private static AttributeGroup<AudienceAttribute> createOldAudienceGroup() {
        Set<AudienceAttribute> audienceSet = new HashSet<>(2);
        audienceSet.add(createOldAudience());
        return new AttributeGroup<>(audienceSet);
    }

    private static AttributeGroup<AudienceAttribute> createUpdatedAudienceGroup() {
        Set<AudienceAttribute> audienceSet = new HashSet<>();
        audienceSet.add(createUpdatedAudience());
        return new AttributeGroup<>(audienceSet);
    }

    private static AttributeGroup<AudienceAttribute> createNewAudienceGroup() {
        Set<AudienceAttribute> audienceSet = new HashSet<>();
        audienceSet.add(createNewAudience());
        return new AttributeGroup<>(audienceSet);
    }

    private static <T extends BaseAttribute> AttributeDiffNotifier createAttrDiffNotifier(
            AttributeGroup<T> oldAttrs,
            AttributeGroup<T> newAttrs) {

        return new AttributeDiffNotifier<T>(oldAttrs, newAttrs) {
            @Override
            public void run() {

            }
        };
    }
}
