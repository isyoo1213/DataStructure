package main.sourcecode.heap;

import java.util.Comparator;

public class MyArrayMinHeap<E> {

    private final Comparator<? super E> comparator;
    private static final int DEFAULT_CAPACITY = 10;
    private int size;
    private Object[] array;

    public MyArrayMinHeap() {
        this(null);
    }

    public MyArrayMinHeap(Comparator<? super E> comparator) {
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    public MyArrayMinHeap(int capacity) {
        this(capacity, null);
    }

    public MyArrayMinHeap(int capacity, Comparator<? super E> comparator) {
        this.array = new Object[capacity];
        this.size = 0;
        this.comparator = comparator;
    }

    private int getParentIndex(int index) {
        return index/2;
    }

    private int getLeftChildIndex(int index) {
        return index * 2;
    }

    private int getRightChildIndex(int index) {
        return (index * 2) + 1;
    }

    private void resize(int newCapacity) {
        Object[] newArray = new Object[newCapacity];
        for (int index = 1; index <= size; index++) {
            newArray[index] = array[index];
        }
        this.array = null;
        this.array = newArray;
    }

    public void add(E value) {
        if (size + 1 == array.length) {
            resize(array.length * 2);
        }
        //마지막 data의 인덱스 + 1에 해당하는 index를 넘겨주고 해당 위치부터 sift를 통해 parent와 비교하며 자리를 잡아가는 방식
        siftUp(size + 1, value);
        size++;
    }

    public void siftUp(int index, E target) {
        if (this.comparator != null) {
            siftUpComparator(index, target, comparator);
        } else {
            siftUpComparable(index, target);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftUpComparator(int index, E target, Comparator<? super E> comparator) {
        while (index > 1) {
            int parentIndex = getParentIndex(index);
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
            int parentIndex = getParentIndex(index);
            Object parentVal = array[parentIndex];
            if (comparable.compareTo((E) parentVal) >= 0) {
                break;
            }
            array[index] = parentVal;
            index = parentIndex;
        }
        array[index] = comparable;
    }
}
