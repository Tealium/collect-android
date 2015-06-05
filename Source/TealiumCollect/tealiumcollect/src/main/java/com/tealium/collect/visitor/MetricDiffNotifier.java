package com.tealium.collect.visitor;

import com.tealium.collect.TealiumCollect;
import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.MetricAttribute;

import java.util.EventListener;

final class MetricDiffNotifier extends AttributeDiffNotifier<MetricAttribute> implements Runnable {

    MetricDiffNotifier(AttributeGroup<MetricAttribute> oldAttributeGroup, AttributeGroup<MetricAttribute> newAttributeGroup) {
        super(oldAttributeGroup, newAttributeGroup);
    }

    @Override
    public void run() {
        TealiumCollect.OnMetricUpdateListener metricUpmetricListener;
        for (EventListener eventListener : TealiumCollect.getEventListeners()) {
            if (eventListener instanceof TealiumCollect.OnMetricUpdateListener) {
                metricUpmetricListener = (TealiumCollect.OnMetricUpdateListener) eventListener;

                for (MetricAttribute metric : this.getRemovedAttributes()) {
                    metricUpmetricListener.onMetricUpdate(metric, null);
                }

                for (MetricAttribute metric : this.getModifiedAttributes()) {
                    metricUpmetricListener.onMetricUpdate(
                            this.getOldAttributeGroup().get(metric.getId()),
                            metric);
                }

                for (MetricAttribute metric : this.getAddedAttributes()) {
                    metricUpmetricListener.onMetricUpdate(null, metric);
                }
            }
        }
    }
}
