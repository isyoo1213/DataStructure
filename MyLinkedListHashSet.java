package main.sourcecode.set;

public class MyLinkedListHashSet<E> implements MySet<E> {

    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;

    Node<E>[] table;
    private int size;

    private Node<E> head;
    private Node<E> tail;

    @SuppressWarnings("unchecked")
    public MyLinkedListHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        head = null;
        tail = null;
    }

    private static final int hash(Object key) {
        int hash;
        if (key == null) {
            return 0;
        } else {
            return Math.abs(hash = key.hashCode()) ^ (hash >>> 16);
        }
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {

    }
}
