package com.tealium.collect.testutil;

import com.tealium.collect.attribute.BaseAttribute;

import java.util.Collection;
import java.util.HashSet;

public final class TestUtil {
    private TestUtil() {
    }

    public static <T extends BaseAttribute> Collection<T> createAttrCollection(T attribute) {
        Collection<T> c = new HashSet<>(1);
        c.add(attribute);
        return c;
    }


}
