import org.junit.Test;
import static org.junit.Assert.*;

public class AListTest {

    @Test
    public void testAddRemoveContains() {
        AList<Integer> aList = new AList<>();
        for (Integer i = 0; i < 100; i += 1) {
            aList.add(i, i);
            assertTrue(aList.contains(i));
        }
        assertEquals(100, aList.size());

        for (Integer i = 0; i < 100; i += 1) {
            assertEquals(i, aList.get(0));
            aList.remove(i);
            assertFalse(aList.contains(i));
        }
        assertEquals(0, aList.size());
    }
}
