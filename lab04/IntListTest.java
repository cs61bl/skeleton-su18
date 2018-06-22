import org.junit.Test;
import static org.junit.Assert.*;

public class IntListTest {

    /**
     * Example test that verifies correctness of the IntList.of static method.
     * The main point of this is to convince you that assertEquals knows how to
     * handle IntLists just fine because we implemented IntList.equals.
     */
    @Test
    public void testOf() {
        IntList one = new IntList(1, null);
        IntList twoOne = new IntList(2, one);
        IntList threeTwoOne = new IntList(3, twoOne);

        IntList x = IntList.of(3, 2, 1);
        assertEquals(threeTwoOne, x);
    }

    @Test
    public void testAdd() {
        IntList a = IntList.of(1, 2, 3);
        assertEquals(a, IntList.of(1, 2, 3));
        a.add(4);
        assertEquals(a, IntList.of(1, 2, 3, 4));
        a.add(5);
        assertEquals(a, IntList.of(1, 2, 3, 4, 5));

        IntList single = IntList.of(1);
        single.add(2);
        assertEquals(single, IntList.of(1, 2));
    }

    @Test
    public void testSmallest() {
        assertEquals(6, IntList.of(63, 6, 6, 74, 7, 8, 52, 33, 43, 6, 6, 32).smallest());
        assertEquals(9, IntList.of(9).smallest());
        assertEquals(9, IntList.of(9, 9, 9, 9, 9, 9, 9, 9, 9, 9).smallest());
        assertEquals(1, IntList.of(10, 9, 8, 7, 6, 5, 4, 3, 2, 1).smallest());
    }

    @Test
    public void testSquaredSum() {
        assertEquals(14, IntList.of(1, 2, 3).squaredSum());
        assertEquals(1, IntList.of(1).squaredSum());
        assertEquals(5, IntList.of(1, 2).squaredSum());
        assertEquals(2, IntList.of(1, 1).squaredSum());
        assertEquals(18, IntList.of(3, 3).squaredSum());
    }

    @Test
    public void testDSquareList() {
        IntList L = IntList.of(1, 2, 3);
        assertEquals(IntList.of(1, 2, 3), L);

        IntList.dSquareList(L);
        assertEquals(IntList.of(1, 4, 9), L);
    }

    /**
     * Do not use the new keyword in your tests. You can create
     * lists using the handy IntList.of method.
     *
     * Make sure to include test cases involving lists of various sizes
     * on both sides of the operation. That includes the empty of, which
     * can be instantiated, for example, with
     * IntList empty = IntList.of().
     *
     * Keep in mind that dcatenate(A, B) is NOT required to leave A untouched.
     * Anything can happen to A.
     */

    @Test
    public void testCatenate() {
        IntList A = IntList.of(1, 2, 3);
        IntList B = IntList.of(4, 5, 6);

        IntList exp = IntList.of(1, 2, 3, 4, 5, 6);
        IntList res = IntList.catenate(A, B);
        // Check that correctly catenates
        assertEquals(exp, res);

        // Cannot modify A
        assertEquals(IntList.of(1, 2, 3), A);
    }

    @Test
    public void testDCatenate() {
        IntList A = IntList.of(1, 2, 3);
        IntList B = IntList.of(4, 5, 6);

        IntList exp = IntList.of(1, 2, 3, 4, 5, 6);
        IntList res = IntList.dcatenate(A, B);
        // Check that correctly catenates
        assertEquals(exp, res);

        // Check that A has been modified
        assertEquals(IntList.of(1, 2, 3, 4, 5, 6), A);
        A.first = 7;
        assertEquals(A.first, res.first);
    }
}
