package main.sourcecode.queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class MyMinHeapPriorityQueue<E> implements QueueInterface<E> {

    private final Comparator<? super E> comparator;
    private static final int DEFAULT_CAPACITY = 10;
    private int size;
    private Object[] array;

    public MyMinHeapPriorityQueue() {
        this(null);
    }

    public MyMinHeapPriorityQueue(Comparator<? super E> comparator) {
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    public MyMinHeapPriorityQueue(int capacity) {
        this(capacity, null);
    }

    public MyMinHeapPriorityQueue(int capacity, Comparator<? super E> comparator) {
        this.array = new Object[capacity];
        this.size = 0;
        this.comparator = comparator;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object value) {
        for (int i = 1; i <= size; i++) {
            if (array[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
        size = 0;
    }

    private int getParent(int index) {
        return index / 2;
    }

    private int getLeftChild(int index) {
        return index * 2;
    }

    private int getRightChild(int index) {
        return (index * 2) + 1;
    }

    public void resize(int newCapacity) {
        Object[] newArray = new Object[newCapacity];
        for (int i = 1; i <= size; i++) {
            newArray[i] = array[i];
        }
        this.array = null;
        this.array = newArray;
    }

    @SuppressWarnings("unchecked")
    private void siftUpComparator(int index, E target, Comparator<? super E> comparator) {
        while (index > 1) {
            int parentIndex = getParent(index);
            Object parentVal = array[parentIndex];
            if (comparator.compare(target, (E) parentVal) >= 0) {
                break;
            }
            array[index] = parentVal;
            index = parentIndex;
        }
        array[index] = target;
    }

    private void siftUpComparable(int index, E target) {
        Comparable<? super E> comparable = (Comparable<? super E>) target;
        while (index > 1) {
            int parentIndex = getParent(index);
            Object parentVal = array[parentIndex];
            if (comparable.compareTo((E) parentVal) >= 0) {
                break;
            }
            array[index] = parentVal;
            index = parentIndex;
        }
        array[index] = target;
    }

    private void siftUp(int index, E target) {
        if (comparator != null) {
            siftUpComparator(index, target, this.comparator);
        } else {
            siftUpComparable(index, target);
        }
    }

    @Override
    public boolean offer(E value) {
        if (size + 1 == array.length) {
            resize(array.length * 2);
        }
        siftUp(size + 1, value);
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void siftDownComparator(int index, E target, Comparator<? super E> comparator) {
        array[index] = null;
        int parentIndex = index;
        int childIndex;

        while ((childIndex = getLeftChild(parentIndex)) <= size) {
            int rightChildIndex = getRightChild(parentIndex);
            Object childVal = array[childIndex];
            if (rightChildIndex <= size && comparator.compare((E) childVal, (E) array[rightChildIndex]) > 0) {
                childIndex = rightChildIndex;
                childVal = array[childIndex];
            }
            if (comparator.compare((E) childVal, target) >= 0) {
                break;
            }
            array[parentIndex] = childVal;
            parentIndex = childIndex;
        }
        array[parentIndex] = target;
        if (array.length > DEFAULT_CAPACITY && size < array.length / 4) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDownComparable(int index, E target) {
        Comparable<? super E> comparable = (Comparable<? super E>) target;
        array[index] = null;
        int parentIndex = index;
        int childIndex;
        //** bit shift 연산자를 사용해서 getLeft()를 대체
        while ((childIndex = parentIndex << 1) <= size) {
            int rightChildIndex = childIndex + 1;
            Object childVal = array[childIndex];
            if (rightChildIndex <= size && ((Comparable<? super E>) childVal).compareTo((E) array[rightChildIndex]) > 0) {
                childIndex = rightChildIndex;
                childVal = array[childIndex];
            }
            if (comparable.compareTo((E) childVal) <= 0) {
                break;
            }
            array[parentIndex] = childVal;
            parentIndex = childIndex;
        }
        array[parentIndex] = target;
        if (array.length > DEFAULT_CAPACITY && size < array.length / 4) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }
    }

    private void siftDown(int index, E target) {
        if (this.comparator != null) {
            siftDownComparator(index, target, this.comparator);
        } else {
            siftDownComparable(index, target);
        }
    }

    @SuppressWarnings("unchecked")
    public E remove() {
        if (array[1] == null) {
            throw new NoSuchElementException();
        }
        E result = (E) array[1];
        E target = (E) array[size];

        array[size] = null;
        size--;
        siftDown(1, target);
        return result;
    }

    @Override
    public E poll() {
        if (array[1] == null) {
            return null;
        }
        return remove();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peek() {
        if (array[1] == null) {
            throw new NoSuchElementException();
        }
        return (E) array[1];
    }

    public Object[] toArray() {
        return toArray(null);
    }

    @SuppressWarnings("unchecked")
    public <T> T[]  toArray(T[] a) {
        if (a.length <= size) {
            return (T[]) Arrays.copyOfRange(array, 1, size + 1, a.getClass());
        }
        System.arraycopy(array, 1, a, 0, size);
        return a;
    }

    public Object clone() {
        try {
            MyMinHeapPriorityQueue<?> clone = (MyMinHeapPriorityQueue<?>) super.clone();
            clone.array = new Object[size + 1];
            for (int i = 0; i <= size; i++) {
                clone.array[i] = array[i];
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}
