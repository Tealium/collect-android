package com.tealium.collect;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilTest extends TestCase {

    public void testMapToJSONObject() throws Exception {

        Map<String, Object> parent = new HashMap<>();
        parent.put("child-map", new HashMap<String, String>());
        parent.put("child-obj", new Object());
        parent.put("child-list", new ArrayList<>());
        parent.put("child-arr", new String[]{});

        JSONObject o = Util.JSON.mapToJSONObject(parent);
        Assert.assertEquals(parent.size(), o.length());
        Assert.assertTrue(o.get("child-map") instanceof JSONObject);
        Assert.assertTrue(o.get("child-obj") instanceof String);
        Assert.assertTrue(o.get("child-list") instanceof JSONArray);
        Assert.assertTrue(o.get("child-arr") instanceof JSONArray);
    }

    public void testCollectionToJSONArray() throws Exception {

        final Object o = new Object();

        final List<Object> parent = new ArrayList<>(1);
        parent.add(o);

        final JSONArray array = Util.JSON.collectionToJSONArray(parent);

        Assert.assertEquals(parent.size(), array.length());
        Assert.assertNotSame(o, array.get(0));
        Assert.assertTrue(array.get(0) instanceof String);
    }

    public void testArrayToJSONArray() throws Exception {

        try {
            Util.JSON.arrayToJSONArray(new Object());
            Assert.fail();
        } catch (IllegalArgumentException ignored) {

        }

        final Object[] source = new Object[]{new Object()};

        final JSONArray array = Util.JSON.arrayToJSONArray(source);
        Assert.assertEquals(source.length, array.length());
        Assert.assertNotSame(source[0], array.get(0));
        Assert.assertTrue(array.get(0) instanceof String);
    }

    public void testDeepCopyJSONArray() throws Exception {

        final JSONArray src = new JSONArray();
        src.put(new Object());
        src.put(new HashMap<>());
        src.put(new ArrayList<>());
        src.put(new String[]{});

        final JSONArray copy = Util.JSON.deepCopyJSONArray(src);

        Assert.assertEquals(src.length(), copy.length());
        Assert.assertNotSame(src.get(0), copy.get(0));
        Assert.assertNotSame(src.get(1), copy.get(1));
        Assert.assertNotSame(src.get(2), copy.get(2));
        Assert.assertNotSame(src.get(3), copy.get(3));
        Assert.assertTrue(copy.get(0) instanceof String);
        Assert.assertTrue(copy.get(1) instanceof JSONObject);
        Assert.assertTrue(copy.get(2) instanceof JSONArray);
        Assert.assertTrue(copy.get(3) instanceof JSONArray);
    }

    public void testDeepCopyJSONObject() throws Exception {

        JSONObject src = new JSONObject()
                .put("child-map", new HashMap<String, String>())
                .put("child-obj", new Object())
                .put("child-list", new ArrayList<>())
                .put("child-arr", new String[]{});

        JSONObject o = Util.JSON.deepCopyJSONObject(src);
        Assert.assertEquals(src.length(), o.length());
        Assert.assertTrue(o.get("child-map") instanceof JSONObject);
        Assert.assertTrue(o.get("child-obj") instanceof String);
        Assert.assertTrue(o.get("child-list") instanceof JSONArray);
        Assert.assertTrue(o.get("child-arr") instanceof JSONArray);
    }
}
