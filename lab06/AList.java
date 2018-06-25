/**
 * Array-based list. Invariants:
 *
 *   addLast: The next item we want to add, will go into position size
 *   getLast: The item we want to return is in position size - 1
 *   size: The number of items in the list should be size.
 */
public class AList<Item> {
    private Item[] items;
    private int size;

    /** Creates an empty list. */
    public AList() {
        items = (Item[]) new Object[100];
        size = 0;
    }

    /** Resizes the underlying array to the target capacity. */
    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }

    /** Returns if the collection contains k. */
    public boolean contains(Item x) {
        // TODO
        return false;
    }

    /** Adds x to the end of the list. */
    public void addLast(Item x) {
        if (size == items.length) {
            resize(size * 2);
        }

        items[size] = x;
        size += 1;
    }

    /** Adds x to the list at the specified index. */
    public void add(int index, Item x) {
        if (index < 0 || index > size) {
            return;
        } else if (size == items.length) {
            resize(size * 2);
        }

        // FIXME
        for (int i = index + 1; i <= size; i += 1) {
            items[i] = items[i - 1];
        }
        items[index] = x;
        size += 1;
    }

    /** Returns the last item in the list. */
    public Item getLast() {
        return items[size - 1];
    }

    /** Returns the i-th item in the list, where 0 is the first item. */
    public Item get(int i) {
        return items[i];
    }

    /** Deletes item from back of the list and returns deleted item. */
    public Item removeLast() {
        Item x = getLast();
        items[size - 1] = null;
        size -= 1;
        return x;
    }

    /** Removes the first instance of the item from this list. */
    public void remove(Item x) {
        // TODO
    }
}
