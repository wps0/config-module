package pl.wieczorekp.configmodule.tests;

import org.junit.Before;
import org.junit.Test;
import pl.wieczorekp.configmodule.ConfigEntry;
import pl.wieczorekp.configmodule.ConfigEntryHashMap;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ConfigEntryHashMapTest {
    private ConfigEntryHashMap map;

    @Before
    public void setUp() {
         map = new ConfigEntryHashMap();
    }

    @Test
    public void size() {
        assertEquals("empty map should have 0 size", 0, map.size());
        map.getBooleans().put("ala", new ConfigEntry<>("boolEntry"));
        assertEquals("map should have 1 size", 1, map.size());
    }

    @Test
    public void isEmpty() {
        assertTrue("map should be empty", map.isEmpty());
    }

    @Test
    public void containsKey() {
        map.getStrings().put("ala", new ConfigEntry<>("string"));
        assertTrue("map should contain specified key", map.containsKey("ala"));
        assertFalse("map should not contain specified key", map.containsKey("kot"));
    }

    @Test
    public void containsValue() {
        ConfigEntry<Integer> anInt = new ConfigEntry<>("int");
        map.getIntegers().put("int", anInt);
        assertTrue("map should contain object added before", map.containsValue(anInt));
        assertFalse("map should contain object created now with the same value", map.containsValue(new ConfigEntry<>("int")));
        assertFalse("map should not contain random object", map.containsValue("ala"));
    }

    @Test
    public void get() {
        map.getBooleans().put("ala", new ConfigEntry<>("boolEntry"));
        assertNotNull("map should return non-null object", map.get("ala"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void put() {
        map.put("ala", "a");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove() {
        map.put("ala", "a");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void putAll() {
        map.put("ala", "a");
    }

    @Test
    public void clear() {
        map.clear();
    }

    @Test
    public void keySet() {
        map.getBooleans().put("ala", new ConfigEntry<>("boolEntry"));
        assertNotNull("ToDo: map should return a set of keys", map.keySet());
    }

    @Test
    public void values() {
        map.getBooleans().put("ala", new ConfigEntry<>("boolEntry"));
        assertNotNull("ToDo: map should return a collection of values", map.values());
    }

    @Test
    public void entrySet() {
        fail("ToDo: This test has yet to be implemented");
    }

    @Test
    public void getIntegers() {
        assertTrue("map should return Map object", map.getIntegers() instanceof Map);
    }

    @Test
    public void getBooleans() {
        assertTrue("map should return Map object", map.getBooleans() instanceof Map);
    }

    @Test
    public void getStrings() {
        assertTrue("map should return Map object", map.getStrings() instanceof Map);
    }

    @Test
    public void getObjects() {
        assertTrue("map should return Map object", map.getObjects() instanceof Map);
    }

    @Test
    public void makeHashMap() {
        HashMap<Object, ConfigEntry<Object>> resultMap = ConfigEntryHashMap.makeHashMap(new ConfigEntry<>("Ala", "Makota"));
        assertEquals("returned HashMap should contain created before Entry object", "Makota", resultMap.get("Ala").getValue());
    }
}
