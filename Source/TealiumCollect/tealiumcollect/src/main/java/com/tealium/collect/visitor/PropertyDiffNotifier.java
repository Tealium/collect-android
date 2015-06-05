package com.tealium.collect.visitor;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.PropertyAttribute;

import java.util.EventListener;

final class PropertyDiffNotifier extends AttributeDiffNotifier<PropertyAttribute> implements Runnable {

    PropertyDiffNotifier(AttributeGroup<PropertyAttribute> oldAttributeGroup, AttributeGroup<PropertyAttribute> newAttributeGroup) {
        super(oldAttributeGroup, newAttributeGroup);
    }

    @Override
    public void run() {
        TealiumCollect.OnPropertyUpdateListener propertyUpdateListener;
        for (EventListener eventListener : TealiumCollect.getEventListeners()) {
            if (eventListener instanceof TealiumCollect.OnPropertyUpdateListener) {
                propertyUpdateListener = (TealiumCollect.OnPropertyUpdateListener) eventListener;

                for (PropertyAttribute property : this.getRemovedAttributes()) {
                    propertyUpdateListener.onPropertyUpdate(property, null);
                }

                for (PropertyAttribute property : this.getModifiedAttributes()) {
                    propertyUpdateListener.onPropertyUpdate(
                            this.getOldAttributeGroup().get(property.getId()),
                            property);
                }

                for (PropertyAttribute property : this.getAddedAttributes()) {
                    propertyUpdateListener.onPropertyUpdate(null, property);
                }
            }
        }
    }
}
