package main.sourcecode.set;

public class MyHashSet<E> implements MySet<E> {

    private final int DEFAULT_CAPACITY = 1 << 4;

    private final static float LOAD_FACTOR = 0.75f;

    Node<E> [] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (Node<E>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private static final int hash(Object key) {
        int hash;
        if (key == null) {
            return 0;
        } else {
            return Math.abs(hash = key.hashCode()) ^ (hash >>> 16);
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        final Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<E> value = table[i];
            if (value == null) {
                continue;
            }
            table[i] = null;
            Node<E> nextNode;
            while (value != null) {
                int index = value.hash % newCapacity;
                if (newTable[index] != null) {
                    Node<E> tail = newTable[index];
                    while (tail.next != null) {
                        tail = tail.next;
                    }
                    nextNode = value.next;
                    value.next = null;
                    tail.next = value;
                } else {
                    nextNode = value.next;
                    value.next = null;
                    newTable[index] = value;
                }
                value = nextNode;
            }
        }
        table = null;
        table = newTable;
    }

    private E add(int hash, E key) {
        int index = hash % table.length;
        if (table[index] == null) {
            table[index] = new Node<E>(hash, key, null);
        } else {
            Node<E> temp = table[index];
            Node<E> prev = null;
            while (temp != null) {
                if ((temp.hash == hash) && (temp.key == key || temp.key.equals(key))) {
                    return key;
                }
                prev = temp;
                temp = temp.next;
            }
            prev.next = new Node<E>(hash, key, null);
        }
        size++;
        if (size >= LOAD_FACTOR * table.length) {
            resize();
        }
        return null;
    }

    @Override
    public boolean add(E e) {
        return add(hash(e), e) == null;
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
