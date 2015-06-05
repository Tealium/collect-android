package com.tealium.collect.visitor;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.AudienceAttribute;

import java.util.EventListener;

final class AudienceDiffNotifier extends AttributeDiffNotifier<AudienceAttribute> {

    AudienceDiffNotifier(
            AttributeGroup<AudienceAttribute> oldAttributeGroup,
            AttributeGroup<AudienceAttribute> newAttributeGroup) {
        super(oldAttributeGroup, newAttributeGroup);
    }

    /**
     * Notifies the listeners added by
     * that implement {@link TealiumCollect.OnAudienceUpdateListener}.
     */
    @Override
    public void run() {
        TealiumCollect.OnAudienceUpdateListener audienceUpdateListener;
        for (EventListener eventListener : TealiumCollect.getEventListeners()) {
            if (eventListener instanceof TealiumCollect.OnAudienceUpdateListener) {
                audienceUpdateListener = (TealiumCollect.OnAudienceUpdateListener) eventListener;

                for (AudienceAttribute audience : this.getRemovedAttributes()) {
                    audienceUpdateListener.onAudienceUpdate(audience, null);
                }

                for (AudienceAttribute audience : this.getModifiedAttributes()) {
                    // Won't be any modified if oldAttributeGroup was null.
                    audienceUpdateListener.onAudienceUpdate(
                            this.getOldAttributeGroup().get(audience.getId()),
                            audience);
                }

                for (AudienceAttribute audience : this.getAddedAttributes()) {
                    audienceUpdateListener.onAudienceUpdate(null, audience);
                }
            }
        }
    }
}
