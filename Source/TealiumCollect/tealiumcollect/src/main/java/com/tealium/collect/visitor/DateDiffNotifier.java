package com.tealium.collect.visitor;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.DateAttribute;

import java.util.EventListener;

final class DateDiffNotifier extends AttributeDiffNotifier<DateAttribute> implements Runnable {

    DateDiffNotifier(AttributeGroup<DateAttribute> oldAttributeGroup, AttributeGroup<DateAttribute> newAttributeGroup) {
        super(oldAttributeGroup, newAttributeGroup);
    }

    @Override
    public void run() {
        TealiumCollect.OnDateUpdateListener dateUpdateListener;
        for (EventListener eventListener : TealiumCollect.getEventListeners()) {
            if (eventListener instanceof TealiumCollect.OnDateUpdateListener) {
                dateUpdateListener = (TealiumCollect.OnDateUpdateListener) eventListener;

                for (DateAttribute date : this.getRemovedAttributes()) {
                    dateUpdateListener.onDateUpdate(date, null);
                }

                for (DateAttribute date : this.getModifiedAttributes()) {
                    dateUpdateListener.onDateUpdate(
                            this.getOldAttributeGroup().get(date.getId()),
                            date);
                }

                for (DateAttribute date : this.getAddedAttributes()) {
                    dateUpdateListener.onDateUpdate(null, date);
                }
            }
        }
    }
}
