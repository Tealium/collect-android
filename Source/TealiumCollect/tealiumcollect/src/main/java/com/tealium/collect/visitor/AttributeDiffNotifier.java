package com.tealium.collect.visitor;

import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.BaseAttribute;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Validates and aggregates the incoming changes.
 */
abstract class AttributeDiffNotifier<T extends BaseAttribute> implements Runnable {

    private final AttributeGroup<T> oldAttributeGroup;
    private final AttributeGroup<T> newAttributeGroup;
    private final List<T> removedAttributes;
    private final List<T> addedAttributes;
    private final List<T> modifiedAttributes;

    public AttributeDiffNotifier(AttributeGroup<T> oldAttributeGroup, AttributeGroup<T> newAttributeGroup) {

        this.oldAttributeGroup = oldAttributeGroup;
        this.newAttributeGroup = newAttributeGroup;

        List<T> addedAttributes = new LinkedList<>();
        List<T> modifiedAttributes = new LinkedList<>();
        List<T> removedAttributes = new LinkedList<>();

        extractDifferences(
                oldAttributeGroup,
                newAttributeGroup,
                addedAttributes,
                modifiedAttributes,
                removedAttributes);

        this.addedAttributes = Collections.unmodifiableList(addedAttributes);
        this.modifiedAttributes = Collections.unmodifiableList(modifiedAttributes);
        this.removedAttributes = Collections.unmodifiableList(removedAttributes);
    }


    public AttributeGroup<T> getOldAttributeGroup() {
        return oldAttributeGroup;
    }

    public AttributeGroup<T> getNewAttributeGroup() {
        return newAttributeGroup;
    }

    public final List<T> getRemovedAttributes() {
        return removedAttributes;
    }

    public final List<T> getAddedAttributes() {
        return addedAttributes;
    }

    public final List<T> getModifiedAttributes() {
        return modifiedAttributes;
    }

    private static <T extends BaseAttribute> void extractDifferences(
            AttributeGroup<T> oldAttributeGroup,
            AttributeGroup<T> newAttributeGroup,
            List<T> addedAttributes,
            List<T> modifiedAttributes,
            List<T> removedAttributes) {

        if (oldAttributeGroup == null && newAttributeGroup == null) {
            return;
        }

        Iterator<T> i;
        T nextItem, extractedItem;

        if (oldAttributeGroup == null) {
            i = newAttributeGroup.iterator();
            while (i.hasNext()) {
                // Every attribute is new.
                addedAttributes.add(i.next());
            }
            return;
        }

        if (newAttributeGroup == null) {
            i = oldAttributeGroup.iterator();
            while (i.hasNext()) {
                // Every attribute is removed.
                removedAttributes.add(i.next());
            }
            return;
        }

        i = oldAttributeGroup.iterator();

        while (i.hasNext()) {
            nextItem = i.next();
            if ((extractedItem = newAttributeGroup.get(nextItem.getId())) == null) {
                removedAttributes.add(nextItem);
            } else if (!nextItem.equals(extractedItem)) {
                // Ids match, so values must differ.
                modifiedAttributes.add(extractedItem);
            }
        }

        i = newAttributeGroup.iterator();
        while (i.hasNext()) {
            nextItem = i.next();
            if (!oldAttributeGroup.containsId(nextItem)) {
                addedAttributes.add(nextItem);
            }
        }
    }
}
