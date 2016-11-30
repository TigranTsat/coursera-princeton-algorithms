import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Element<Item> first;
    private Element<Item> last;
    private int size;

    private class Element<Item> {
        private Item item;
        private Element<Item> next;
        private Element<Item> prev;

        public Element(Item item) {
            this.item = item;
        }
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (first == null) {
            first = new Element<Item>(item);
            last = first;
            first.prev = null;
            first.next = null;
        } 
        else {
            Element<Item> oldFirstElement = first;
            first = new Element<Item>(item);
            first.next = null;
            oldFirstElement.next = first;
            first.prev = oldFirstElement;
        }
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (last == null) {
            last = new Element<Item>(item);
            first = last;
            last.prev = null;
            last.next = null;
        } 
        else {
            Element<Item> lastOldElement = last;
            last = new Element<Item>(item);
            last.next = lastOldElement;
            last.prev = null;
            lastOldElement.prev = last;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        Item item;
        if (size == 1) {
            item = first.item;
            first = null;
            last = null;
        } 
        else {
            item = first.item;
            Element<Item> firstElement = first;
            first = first.prev;
            first.next = null;
            firstElement.next = null;
            firstElement.prev = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        Item item;
        if (size == 1) {
            item = last.item;
            first = null;
            last = null;
        } 
        else {
            item = last.item;
            Element<Item> lastElement = last;
            last = last.next;
            last.prev = null;
            lastElement.next = null;
            lastElement.prev = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator<Item>(this);
    }

    private class DequeIterator<Item> implements Iterator<Item> {
        private Element<Item> position;

        public DequeIterator(Deque<Item> deque) {
            position = (Element<Item>) deque.first;
        }

        @Override
        public boolean hasNext() {
            return position != null;
        }

        @Override
        public Item next() {
            if (position == null) {
                throw new NoSuchElementException();
            }
            Item item = position.item;
            position = position.prev;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // unit testing
    public static void main(String[] args) {
        Deque<String> deque1 = new Deque<String>();
        deque1.addFirst("hello");
        deque1.addLast("world");
        if (deque1.size() != 2) {
            throw new RuntimeException("Validation failed");
        }
        StringBuilder strBuilder = new StringBuilder("");
        for (String element : deque1) {
            strBuilder.append(element);
        }
        if (!"helloworld".equals(strBuilder.toString())) {
            throw new RuntimeException("Validation failed");
        }
        if (deque1.size() != 2) {
            throw new RuntimeException("Validation failed");
        }
        String str = deque1.removeLast();
        if (!"world".equals(str)) {
            throw new RuntimeException("Validation failed. Retrned: " + str);
        }
        str = deque1.removeLast();
        if (!"hello".equals(str)) {
            throw new RuntimeException("Validation failed");
        }
        if (deque1.size() != 0) {
            throw new RuntimeException("Validation failed");
        }
        // Push again
        deque1.addFirst("3");
        deque1.addFirst("2");
        deque1.addFirst("1");
        str = deque1.removeLast();
        if (!"3".equals(str)) {
            throw new RuntimeException("Validation failed");
        }
        str = deque1.removeLast();
        if (!"2".equals(str)) {
            throw new RuntimeException("Validation failed");
        }
        str = deque1.removeLast();
        if (!"1".equals(str)) {
            throw new RuntimeException("Validation failed");
        }
        // Push again
        deque1.addLast("a");
        deque1.addLast("b");
        deque1.addLast("c");
        if (deque1.size() != 3) {
            throw new RuntimeException("Validation failed");
        }
        str = deque1.removeFirst();
        if (!"a".equals(str)) {
            throw new RuntimeException("Validation failed");
        }
        str = deque1.removeFirst();
        if (!"b".equals(str)) {
            throw new RuntimeException("Validation failed");
        }
        str = deque1.removeFirst();
        if (!"c".equals(str)) {
            throw new RuntimeException("Validation failed");
        }
        System.out.println("Tests passed __");
    }
}
