package com.tealium.collect.attribute;

import org.json.JSONObject;

import java.util.Locale;

/**
 * Immutable representation of an AudienceStream Flag Attribute.
 */
public final class FlagAttribute extends BaseAttribute {

    private final boolean value;
    private volatile int hashCode;

    /**
     * @param id    the id of this attribute.
     * @param value the value of this flag.
     * @throws java.lang.IllegalArgumentException when id is null or empty.
     */
    public FlagAttribute(String id, boolean value) {
        super(id);
        this.value = value;
        this.hashCode = 0;
    }

    /**
     * @return the value of this flag.
     */
    public boolean getValue() {
        return value;
    }

    /**
     * @return a human-readable description of this flag attribute.
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Flag : { id:%s, value:%b }",
                JSONObject.quote(this.getId()), this.value);
    }

    /**
     * @return a hashcode unique to this Flag Attribute.
     */
    @Override
    public int hashCode() {
        int result = this.hashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + this.getId().hashCode();
            result = 31 * result + (this.value ? 1 : 0);
            this.hashCode = result;
        }
        return result;
    }

    /**
     * @param o an object to compare to.
     * @return whether this instance is equal to another object, if true this intance's hashcode is
     * equal to the other's.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!FlagAttribute.class.equals(o.getClass())) {
            return false;
        }

        FlagAttribute other = (FlagAttribute) o;

        return this.getId().equals(other.getId()) && this.value == other.value;
    }
}
