package com.tealium.collect.attribute;


import org.json.JSONObject;

import java.util.Locale;

/**
 * Immutable representation of an AudienceStream Property (AKA Trait) Attribute.
 */
public class PropertyAttribute extends BaseAttribute {

    private final String value;
    private volatile int hashCode;

    /**
     * @param id    of the Property
     * @param value of the Property.
     * @throws java.lang.IllegalArgumentException when id is null or empty.
     */
    public PropertyAttribute(String id, String value) {
        super(id);
        this.value = value;
    }

    /**
     * @return the value of this Property.
     */
    public String getValue() {
        return value;
    }

    /**
     * @return a hashcode unique to this Property.
     */
    @Override
    public int hashCode() {
        int result = this.hashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + this.getId().hashCode();
            if(this.value != null) {
                result = 31 * result + this.value.hashCode();
            }
            this.hashCode = result;
        }
        return result;
    }

    /**
     * @return a human-readable description of this Property.
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Property : { id:%s, value:%s }",
                JSONObject.quote(this.getId()), JSONObject.quote(this.value));
    }

    /**
     * @param o the object to compare against.
     * @return whether this Property is equal to another. If true, their hashCodes will be equal
     * as well.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!PropertyAttribute.class.equals(o.getClass())) {
            return false;
        }

        PropertyAttribute other = (PropertyAttribute) o;

        if (!this.getId().equals(other.getId())) {
            return false;
        }

        final boolean isThisValueNull = this.value == null;
        final boolean isOtherValueNull = other.value == null;

        if (isThisValueNull && isOtherValueNull) {
            // both are null.
            return true;
        }

        if (isThisValueNull ^ isOtherValueNull) {
            // One or the other is null.
            return false;
        }

        return this.value.equals(other.value);
    }
}
