import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int DEFAULT_SIZE = 1;
    private int size;
    private Object[] queue;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        queue = new Object[DEFAULT_SIZE];
    }

    // is the queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (size > queue.length - 1) {
            Object[] newQueue = new Object[queue.length * 2];
            for (int i = 0; i < size; i++) {
                newQueue[i] = queue[i];
            }
            queue = newQueue;
        }
        queue[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int randIndex = StdRandom.uniform(size);
        Item item = (Item) this.queue[randIndex];
        // Replace with last one
        queue[randIndex] = queue[size - 1];
        queue[size - 1] = null;
        size--;

        // Resize
        if (size >= 1 && size < queue.length / 3) {
            Object[] newQueue = new Object[queue.length / 3];
            for (int i = 0; i < size; i++) {
                newQueue[i] = queue[i];
            }
            queue = newQueue;
        }
        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int randIndex = StdRandom.uniform(size);
        return (Item) queue[randIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(this);
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Object[] dataArr;
        private int iteratorPos;

        public RandomizedQueueIterator(RandomizedQueue<Item> randQueue) {
            dataArr = new Object[randQueue.size];
            for (int i = 0; i < dataArr.length; i++) {
                dataArr[i] = randQueue.queue[i];
            }
            // Do shuffle
            for (int i = 1; i < dataArr.length; i++) {
                int swapPos = StdRandom.uniform(i + 1);
                swap(i, swapPos);
            }
            iteratorPos = 0;
        }

        @Override
        public boolean hasNext() {
            return iteratorPos < dataArr.length;
        }

        @Override
        public Item next() {
            if (iteratorPos > dataArr.length - 1) {
                throw new NoSuchElementException();
            }
            return (Item) dataArr[iteratorPos++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void swap(int i, int j) {
            Object item = dataArr[i];
            dataArr[i] = dataArr[j];
            dataArr[j] = item;
        }

    }

    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> randQueue1 = new RandomizedQueue<Integer>();
        randQueue1.enqueue(1);
        randQueue1.enqueue(2);
        randQueue1.enqueue(3);
        randQueue1.enqueue(4);
        randQueue1.enqueue(5);

        int counter = 0;
        for (Integer i : randQueue1) {
            counter++;
        }
        if (counter != 5) {
            throw new RuntimeException("Validation failed");
        }

        int item1 = randQueue1.dequeue();
        if (randQueue1.size() != 4) {
            throw new RuntimeException("Validation failed");
        }
        item1 = randQueue1.sample();
        int item2 = randQueue1.sample();
        int item3 = randQueue1.sample();
        if (randQueue1.size() != 4) {
            throw new RuntimeException("Validation failed");
        }
        if ((item1 == item2) && (item2 == item3)) {
            throw new RuntimeException("Very unlikely chance of happening");
        }
        randQueue1.dequeue();
        randQueue1.dequeue();
        randQueue1.dequeue();
        randQueue1.dequeue();
        if (randQueue1.size() != 0) {
            throw new RuntimeException("Validation failed");
        }

        int[] frequencies = new int[10];
        randQueue1.enqueue(3);
        randQueue1.enqueue(4);
        randQueue1.enqueue(5);
        randQueue1.enqueue(6);
        randQueue1.enqueue(7);
        int numberOfAttemps = 100;
        double avgFreq = (double) numberOfAttemps / (double) randQueue1.size();
        for (int j = 0; j < numberOfAttemps; j++) {
            for (int i : randQueue1) {
                frequencies[i]++;
            }
        }
        for (int i = 3; i <= 7; i++) {
            int freq = frequencies[i];
            if (freq < avgFreq * 0.6) {
                throw new RuntimeException("Bad frequency for " + i + " :" + Arrays.toString(frequencies));
            }
        }
        System.out.println("Frequencies = " + Arrays.toString(frequencies));
        // Test more frequencies

        int nRepeats = 3000;
        int[] posFreq = new int[3];
        RandomizedQueue<String> randQueue2 = new RandomizedQueue<String>();
        randQueue2.enqueue("A");
        randQueue2.enqueue("B");
        randQueue2.enqueue("C");
        for (int i = 0; i < nRepeats; i++) {
            int trial = 1;
            Iterator<String> iterator = randQueue2.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals("C")) {
                    posFreq[trial - 1]++;
                    break;
                }
                trial++;
            }
        }
        System.out.println("Freq 2 test completed. Val: " + Arrays.toString(posFreq));
        System.out.println("Tests passed");
    }
}