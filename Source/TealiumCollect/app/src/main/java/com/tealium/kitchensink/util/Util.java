package com.tealium.kitchensink.util;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

public final class Util {
    private Util() {
    }

    public static Map<String, Object> describe(Object object) {
        if (object == null) {
            return null;
        }

        boolean isProperty, hasNoParams;
        Object value;

        Map<String, Object> description = new TreeMap<>();

        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            isProperty = method.getName().startsWith("is") || method.getName().startsWith("get");
            hasNoParams = method.getParameterTypes().length == 0;
            if (isProperty && hasNoParams) {
                try {
                    value = method.invoke(object);
                    description.put(method.getName(), value == null ? "(null)" : value.toString());
                } catch (Throwable t) {
                    description.put(method.getName(), t.getMessage());
                }
            }
        }

        return description;
    }


}
