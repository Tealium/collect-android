package com.tealium.collect.visitor;


import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.BadgeAttribute;

import java.util.EventListener;

final class BadgeDiffNotifier extends AttributeDiffNotifier<BadgeAttribute> implements Runnable {

    BadgeDiffNotifier(AttributeGroup<BadgeAttribute> oldAttributeGroup, AttributeGroup<BadgeAttribute> newAttributeGroup) {
        super(oldAttributeGroup, newAttributeGroup);
    }

    @Override
    public void run() {

        TealiumCollect.OnBadgeUpdateListener badgeUpdateListener;

        for(EventListener eventListener : TealiumCollect.getEventListeners()) {

            if (eventListener instanceof TealiumCollect.OnBadgeUpdateListener) {
                badgeUpdateListener = (TealiumCollect.OnBadgeUpdateListener) eventListener;

                for (BadgeAttribute badge : this.getRemovedAttributes()) {
                    badgeUpdateListener.onBadgeUpdate(badge, null);
                }

                for (BadgeAttribute badge : this.getModifiedAttributes()) {
                    badgeUpdateListener.onBadgeUpdate(
                            this.getOldAttributeGroup().get(badge.getId()),
                            badge);
                }

                for (BadgeAttribute badge : this.getAddedAttributes()) {
                    badgeUpdateListener.onBadgeUpdate(null, badge);
                }
            }
        }
    }
}
