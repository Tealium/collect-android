package com.tealium.collect.attribute;

/**
 * Base class for all AudienceStream Attributes.
 */
public abstract class BaseAttribute {

    private final String id;

    /**
     * All attributes have an id.
     *
     * @param id the attribute's id.
     * @throws java.lang.IllegalArgumentException when the given id is null or empty.
     */
    public BaseAttribute(String id) {

        if (id == null || id.length() == 0) {
            throw new IllegalArgumentException("id must be provided.");
        }

        this.id = id;
    }

    public final String getId() {
        return id;
    }

}
