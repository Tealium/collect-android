package com.tealium.collect.attribute;

import org.json.JSONObject;

import java.util.Locale;

/**
 * Immutable representation of an AudienceStream Audience Attribute.
 */
public final class AudienceAttribute extends BaseAttribute {

    private final String name;
    private volatile int hashCode;

    /**
     * @param id   of the Audience
     * @param name of the Audience.
     * @throws java.lang.IllegalArgumentException when id or name are null or empty.
     */
    public AudienceAttribute(String id, String name) {
        super(id);

        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("name must be provided.");
        }

        this.name = name;
    }

    /**
     * @return a hashcode unique to this Audience.
     */
    @Override
    public int hashCode() {
        int result = this.hashCode;
        if (result == 0) {
            result = 17;
            result = 31 * result + this.getId().hashCode();
            result = 31 * result + this.name.hashCode();
            this.hashCode = result;
        }
        return result;
    }

    /**
     * @return a human-readable description of this Audience.
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Audience : { id:%s, name:%s }",
                JSONObject.quote(this.getId()), JSONObject.quote(this.name));
    }

    /**
     * @param o the object to compare against.
     * @return whether this Audience is equal to another. If true, their hashCodes will be equal
     * as well.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!AudienceAttribute.class.equals(o.getClass())) {
            return false;
        }

        AudienceAttribute other = (AudienceAttribute) o;

        return this.getId().equals(other.getId()) && this.name.equals(other.name);
    }

    /**
     * @return this Audience's name.
     */
    public String getName() {
        return name;
    }
}
