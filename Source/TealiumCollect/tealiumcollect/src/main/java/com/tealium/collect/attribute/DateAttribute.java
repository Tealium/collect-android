package com.tealium.collect.attribute;

import org.json.JSONObject;

import java.util.Locale;

/**
 * Immutable representation of an AudienceStream Date Attribute.
 */
public final class DateAttribute extends BaseAttribute {

    private final long time;
    private volatile int hashCode;

    /**
     * @param id          of the Date
     * @param timestampMs of the Date in ms since epoch.
     * @throws java.lang.IllegalArgumentException when id id null or empty.
     */
    public DateAttribute(String id, long timestampMs) {
        super(id);
        this.time = timestampMs;
    }

    /**
     * @return the date of this attribute in ms since epoch.
     */
    public long getTime() {
        return time;
    }

    /**
     * @return a human-readable description of this Date.
     */
    @Override
    public String toString() {
        return String.format(
                Locale.ROOT,
                "Date : { id:%s, timestamp_ms:%d }",
                JSONObject.quote(this.getId()),
                this.time);
    }

    /**
     * @return a hashcode unique to this Date Attribute.
     */
    @Override
    public int hashCode() {
        int result = this.hashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + this.getId().hashCode();
            // Effective Java
            result = 31 * result + (int) (this.time ^ (this.time >>> 32));
            this.hashCode = result;
        }
        return result;
    }

    /**
     * @param o the object to compare against.
     * @return whether this Date is equal to another. If true, their hashCodes will be equal
     * as well.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!DateAttribute.class.equals(o.getClass())) {
            return false;
        }

        DateAttribute other = (DateAttribute) o;

        return this.getId().equals(other.getId()) && this.time == other.time;
    }
}
