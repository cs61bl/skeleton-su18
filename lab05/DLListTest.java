import org.junit.Test;
import static org.junit.Assert.*;

public class DLListTest {

    @Test
    public void testDLListAdd() {
        DLList<Integer> l = new DLList<>();

        l.add(0, 2);
        l.add(0, 1);
        assertEquals(DLList.of(1, 2), l);
        assertEquals(2, l.size());

        l.add(1, 4);
        assertEquals(DLList.of(1, 4, 2), l);
        assertEquals(3, l.size());

        l.add(3, 1);
        assertEquals(DLList.of(1, 4, 2, 1), l);
        assertEquals(4, l.size());
    }

    @Test
    public void testDLListRemove() {
        DLList<Integer> l = DLList.of(1, 4, 2, 1);

        l.remove(1);
        assertEquals(DLList.of(4, 2, 1), l);
        assertEquals(3, l.size());

        l.remove(2);
        assertEquals(DLList.of(4, 1), l);
        assertEquals(2, l.size());
    }
}
