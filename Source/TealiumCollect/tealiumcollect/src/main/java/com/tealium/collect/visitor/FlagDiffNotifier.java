package com.tealium.collect.visitor;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.FlagAttribute;

import java.util.EventListener;

/**
 * Created by chadhartman on 1/26/15.
 */
final class FlagDiffNotifier extends AttributeDiffNotifier<FlagAttribute> implements Runnable {

    FlagDiffNotifier(AttributeGroup<FlagAttribute> oldAttributeGroup, AttributeGroup<FlagAttribute> newAttributeGroup) {
        super(oldAttributeGroup, newAttributeGroup);
    }

    @Override
    public void run() {
        TealiumCollect.OnFlagUpdateListener flagUpdateListener;
        for (EventListener eventListener : TealiumCollect.getEventListeners()) {
            if (eventListener instanceof TealiumCollect.OnFlagUpdateListener) {
                flagUpdateListener = (TealiumCollect.OnFlagUpdateListener) eventListener;

                for (FlagAttribute flag : this.getRemovedAttributes()) {
                    flagUpdateListener.onFlagUpdate(flag, null);
                }

                for (FlagAttribute flag : this.getModifiedAttributes()) {
                    flagUpdateListener.onFlagUpdate(
                            this.getOldAttributeGroup().get(flag.getId()),
                            flag);
                }

                for (FlagAttribute flag : this.getAddedAttributes()) {
                    flagUpdateListener.onFlagUpdate(null, flag);
                }
            }
        }
    }
}
