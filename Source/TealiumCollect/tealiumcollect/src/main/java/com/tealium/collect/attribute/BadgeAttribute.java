package com.tealium.collect.attribute;


import org.json.JSONObject;

import java.util.Locale;

/**
 * Immutable representation of an AudienceStream Badge Attribute.
 */
public final class BadgeAttribute extends BaseAttribute {

    private volatile int hashCode;

    /**
     * @param id of the Badge
     * @throws java.lang.IllegalArgumentException when id is null or empty.
     */
    public BadgeAttribute(String id) {
        super(id);
        this.hashCode = 0;
    }

    /**
     * @return a hashcode unique to this Badge.
     */
    @Override
    public int hashCode() {
        int result = this.hashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + this.getId().hashCode();
            this.hashCode = result;
        }
        return result;
    }

    /**
     * @param o the object to compare against.
     * @return whether this Badge is equal to another. If true, their hashCodes will be equal
     * as well.
     */
    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (!BadgeAttribute.class.equals(o.getClass())) {
            return false;
        }

        return this.getId().equals(((BadgeAttribute) o).getId());
    }

    /**
     * @return a human-readable description of this Badge.
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Badge : { id:%s }", JSONObject.quote(this.getId()));
    }
}
