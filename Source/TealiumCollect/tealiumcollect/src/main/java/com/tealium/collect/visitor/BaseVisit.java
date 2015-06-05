package com.tealium.collect.visitor;

import com.tealium.collect.attribute.AttributeGroup;
import com.tealium.collect.attribute.DateAttribute;
import com.tealium.collect.attribute.FlagAttribute;
import com.tealium.collect.attribute.MetricAttribute;
import com.tealium.collect.attribute.PropertyAttribute;

import java.util.Collection;

class BaseVisit {

    private volatile int hashCode;

    private final long creationDate;
    private final AttributeGroup<FlagAttribute> flags;
    private final AttributeGroup<MetricAttribute> metrics;
    private final AttributeGroup<PropertyAttribute> properties;
    private final AttributeGroup<DateAttribute> dates;

    BaseVisit() {
        this.creationDate = 0;
        this.flags = new AttributeGroup<>();
        this.metrics = new AttributeGroup<>();
        this.dates = new AttributeGroup<>();
        this.properties = new AttributeGroup<>();
    }

    BaseVisit(long creationDate,
              Collection<DateAttribute> dates,
              Collection<FlagAttribute> flags,
              Collection<MetricAttribute> metrics,
              Collection<PropertyAttribute> properties) {

        this.creationDate = creationDate;

        this.flags = new AttributeGroup<>(flags);
        this.metrics = new AttributeGroup<>(metrics);
        this.properties = new AttributeGroup<>(properties);
        this.dates = new AttributeGroup<>(dates);
    }

    /**
     * @return the creation date.
     */
    public final long getCreationTimestamp() {
        return creationDate;
    }

    /**
     * @return the flags associated for this visitor.
     */
    public final AttributeGroup<FlagAttribute> getFlags() {
        return flags;
    }

    /**
     * @return the metrics associated for this visitor.
     */
    public final AttributeGroup<MetricAttribute> getMetrics() {
        return metrics;
    }

    /**
     * @return the properties (traits) associated for this visitor.
     */
    public final AttributeGroup<PropertyAttribute> getProperties() {
        return properties;
    }

    /**
     * @return the dates associated for this visitor.
     */
    public final AttributeGroup<DateAttribute> getDates() {
        return dates;
    }

    @Override
    public int hashCode() {
        int result = this.hashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + (int) (this.creationDate ^ (this.creationDate >>> 32));
            result = 31 * result + this.dates.hashCode();
            result = 31 * result + this.flags.hashCode();
            result = 31 * result + this.metrics.hashCode();
            result = 31 * result + this.properties.hashCode();
            this.hashCode = result;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || !(o instanceof BaseVisit)) {
            return false;
        }

        BaseVisit visit = (BaseVisit) o;

        return this.creationDate == visit.creationDate &&
                this.flags.equals(visit.flags) &&
                this.metrics.equals(visit.metrics) &&
                this.properties.equals(visit.properties) &&
                this.dates.equals(visit.dates);
    }
}
