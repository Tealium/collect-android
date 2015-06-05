package com.tealium.collect.attribute;

import org.json.JSONObject;

import java.util.Locale;

/**
 * Immutable representation of an AudienceStream Metric Attribute.
 */
public class MetricAttribute extends BaseAttribute {

    private final double value;
    private volatile int hashCode;

    /**
     * @param id    of the Metric
     * @param value of the Metric.
     * @throws java.lang.IllegalArgumentException when id is null or empty.
     */
    public MetricAttribute(String id, double value) {
        super(id);
        this.value = value;
    }

    /**
     * @return this Metric's value.
     */
    public double getValue() {
        return value;
    }

    /**
     * @return a hashcode unique to this Metric.
     */
    @Override
    public int hashCode() {
        int result = this.hashCode;
        if (result == 0) {

            final long l = Double.doubleToLongBits(this.value);

            result = 17;
            result = 31 * result + this.getId().hashCode();
            result = 31 * result + (int) (l ^ (l >>> 32));
            this.hashCode = result;
        }
        return result;
    }

    /**
     * @return a human-readable representation of this Metric.
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Metric : { id:%s, value:%f }",
                JSONObject.quote(this.getId()), this.value);
    }

    /**
     * @param o an object to compare this instance against.
     * @return whether this instance is equal to the other object. If true, this instance's
     * hashcode will be equal to the other's.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!MetricAttribute.class.equals(o.getClass())) {
            return false;
        }

        MetricAttribute other = (MetricAttribute) o;

        return this.getId().equals(other.getId()) && this.value == other.value;
    }
}
