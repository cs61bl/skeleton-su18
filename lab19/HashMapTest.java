import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashSet;

public class HashMapTest {

    @Test
    public void testConstructor() {
        // assert constructors are initialized, seem to work correctly, and
        // don't cause errors
        HashMap<String, String> dictionary = new HashMap<String, String>();
        assertEquals(0, dictionary.size());
        assertEquals(16, dictionary.capacity());

        dictionary = new HashMap<String, String>(10);
        assertEquals(0, dictionary.size());
        assertEquals(10, dictionary.capacity());

        // simply test that the constructor exists, resizeTest will do the rest
        dictionary = new HashMap<String, String>(10, 1);
        assertEquals(0, dictionary.size());
        assertEquals(10, dictionary.capacity());
    }

    @Test
    public void testClear() {
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("matt", "jon");
        assertTrue(h.containsKey("matt"));
        assertEquals("jon", h.get("matt"));
        assertEquals(1, h.size());
        h.clear();
        assertFalse(h.containsKey("matt"));
        assertEquals(0, h.size());
    }

    @Test
    public void testPut() {
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("michael", "brandon");
        assertTrue(h.containsKey("michael"));
        assertEquals("brandon", h.get("michael"));
        assertFalse(h.containsKey("trash"));
    }

    @Test
    public void testGet() {
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("jackson", "catherine");
        assertEquals("catherine", h.get("jackson"));
    }

    @Test
    public void testRemove() {
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("alex", "julianna");
        assertTrue(h.containsKey("alex"));
        assertEquals("julianna", h.get("alex"));
        h.remove("alex");
        assertFalse(h.containsKey("alex"));
    }

    @Test
    public void testResize() {
        HashMap<String, String> h = new HashMap<String, String>(2);
        assertEquals(h.capacity(), 2);
        h.put("angela", "neil");
        h.put("christine", "kevin");
        assertEquals(h.capacity(), 4);

        h = new HashMap<String, String>(10, 1);
        for (int i = 1; i <= 10; i += 1) {
            h.put(Integer.toString(i), Integer.toString(i));
        }
        assertEquals(10, h.size());
        assertEquals(10, h.capacity());
        h.put("11", "11");
        assertEquals(11, h.size());
        assertEquals(20, h.capacity());
        h.remove("11");
        assertEquals(10, h.size());
        assertEquals(20, h.capacity());
    }

    @Test
    public void basicFunctionalityTest() {
        HashMap<String, String> dictionary = new HashMap<String, String>();
        assertEquals(0, dictionary.size());
        assertEquals(16, dictionary.capacity());

        // can put objects in dictionary and get them
        dictionary.put("kelly", "lauren");
        assertTrue(dictionary.containsKey("kelly"));
        assertEquals("lauren", dictionary.get("kelly"));
        assertEquals(1, dictionary.size());

        // putting with existing key replaces key
        dictionary.put("kelly", "john");
        assertEquals(1, dictionary.size());
        assertEquals("john", dictionary.get("kelly"));
        assertEquals("john", dictionary.remove("kelly"));
        assertEquals(null, dictionary.get("kelly"));
        assertEquals(0, dictionary.size());

        // placing key in multiple times does not affect behavior
        HashMap<String, Integer> studentIDs = new HashMap<String, Integer>();
        studentIDs.put("christine", 12345);
        assertEquals(1, studentIDs.size());
        assertEquals(12345, studentIDs.get("christine").intValue());
        studentIDs.put("kevin", 345);
        assertEquals(2, studentIDs.size());
        assertEquals(12345, studentIDs.get("christine").intValue());
        assertEquals(345, studentIDs.get("kevin").intValue());
        studentIDs.put("kevin", 345);
        assertEquals(2, studentIDs.size());
        assertEquals(12345, studentIDs.get("christine").intValue());
        assertEquals(345, studentIDs.get("kevin").intValue());
        studentIDs.put("kevin", 345);
        assertEquals(2, studentIDs.size());
        assertEquals(12345, studentIDs.get("christine").intValue());
        assertEquals(345, studentIDs.get("kevin").intValue());
        assertTrue(studentIDs.containsKey("christine"));
        assertTrue(studentIDs.containsKey("kevin"));

        // ensure that containsKey does not always return true
        assertFalse(studentIDs.containsKey("joe"));
        assertFalse(studentIDs.containsKey("catherine"));
        studentIDs.put("alex", 612);

        // confirm hash map can handle values being the same
        assertEquals(345, studentIDs.get("kevin").intValue());
        studentIDs.put("evil kevin", 345);
        assertEquals(345, studentIDs.get("evil kevin").intValue());
        assertEquals(studentIDs.get("evil kevin"), studentIDs.get("kevin"));
    }

    @Test
    public void iteratorTest() {
        // replicate basic functionality test while building database
        HashMap<String, Integer> studentIDs = new HashMap<String, Integer>();
        studentIDs.put("christine", 12345);
        studentIDs.put("kevin", 345);
        assertTrue(studentIDs.containsKey("christine"));
        assertTrue(studentIDs.containsKey("kevin"));

        // ensure that containsKey does not always return true
        assertFalse(studentIDs.containsKey("joe"));
        assertFalse(studentIDs.containsKey("catherine"));
        assertFalse(studentIDs.containsKey("alex"));
        studentIDs.put("alex", 612);
        assertTrue(studentIDs.containsKey("alex"));

        // confirm hashMap can handle values being the same
        studentIDs.put("carlo", 12345);
        assertEquals(studentIDs.get("carlo"), studentIDs.get("christine"));

        HashSet<String> expected = new HashSet<String>();
        expected.add("christine");
        expected.add("kevin");
        expected.add("alex");
        expected.add("carlo");

        HashSet<String> output = new HashSet<String>();
        for (String name : studentIDs) {
            output.add(name);
        }
        assertEquals(expected, output);
    }

}
