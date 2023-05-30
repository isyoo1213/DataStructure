package main.sourcecode.stack;

import java.util.Arrays;

public class MyStack<E> implements StackInterface<E> {

    private static final int DEFAULT_CAPACITY = 10; //최소(기본) Capacity
    private static final Object[] EMPTY_ARRAY = {};
    //private static final Object[] EMPTY_ARRAY = new Object[0];

    private Object[] array;
    private int size;

    public MyStack() {
        this.array =EMPTY_ARRAY;
        this.size = 0;
    }

    public MyStack(int capacity) {
        this.array = new Object[capacity];
        this.size = 0;
    }

    private void resize() {
        if (Arrays.equals(array, EMPTY_ARRAY)) {
            array = new Object[DEFAULT_CAPACITY];
            return;
        }

        int arrayCapacity = array.length;

        if (size == arrayCapacity) {
            int newCapacity = arrayCapacity * 2;

            array = Arrays.copyOf(array, newCapacity);
            return;
        }
        if (size < arrayCapacity / 2) {
            int newCapacity = (arrayCapacity / 2);
            array = Arrays.copyOf(array, Math.max(DEFAULT_CAPACITY, newCapacity));
        }
    }

    @Override
    public E push(E data) {
        if (size == array.length) {
            resize();
        }
        array[size] = data;
        size++;
        return data;
    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public int search(Object value) {
        return 0;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean empty() {
        return false;
    }
}
