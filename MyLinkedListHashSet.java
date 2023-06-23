package main.sourcecode.set;

public class MyLinkedListHashSet<E> implements MySet<E> {

    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;

    DoublyNode<E>[] table;
    private int size;

    private DoublyNode<E> head;
    private DoublyNode<E> tail;

    @SuppressWarnings("unchecked")
    public MyLinkedListHashSet() {
        table = (DoublyNode<E>[]) new DoublyNode[DEFAULT_CAPACITY];
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

    private void linkLastNode(DoublyNode<E> node) {
        DoublyNode<E> lastNode = tail;
        tail = node;
        if (lastNode == null) {
            head = node;
        } else {
            node.prevLink = lastNode;
            lastNode.nextLink = node;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        final DoublyNode<E>[] newTable = (DoublyNode<E>[]) new DoublyNode[newCapacity];
        for (int i = 0; i < table.length; i++) {
            DoublyNode<E> value = table[i];
            if (value == null) {
                continue;
            }
            table[i] = null;
            DoublyNode<E> nextNode;
            while (value != null) {
                int index = value.hash % newCapacity;
                if (newTable[index] != null) {
                    DoublyNode<E> newTableTail = newTable[index];
                    while (newTableTail.next != null) {
                        newTableTail = newTableTail.next;
                    }
                    nextNode = value.next;
                    value.next = null;
                    newTableTail.next = value;
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
        DoublyNode<E> newNode = new DoublyNode<>(hash, key, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            DoublyNode<E> tempNode = table[index];
            DoublyNode<E> prevNode = null;

            while (tempNode != null) {
                if ((tempNode.hash == hash) && (tempNode.key == key || tempNode.key.equals(key))) {
                    return key;
                }
                prevNode = tempNode;
                tempNode = tempNode.next;
            }
            prevNode.next = newNode;
        }
        size++;
        linkLastNode(newNode);

        if (size >= LOAD_FACTOR * table.length) {
            resize();
        }
        return null;
    }

    @Override
    public boolean add(E e) {
        return add(hash(e), e) == null;
    }

    private void unlinkNode(DoublyNode<E> o) {
        DoublyNode<E> prevNode = o.prevLink;
        DoublyNode<E> nextNode = o.nextLink;

        if (prevNode == null) {
            head = nextNode;
        } else {
            prevNode.nextLink = nextNode;
            o.prevLink = null;
        }
        if (nextNode == null) {
            tail = prevNode;
        } else {
            nextNode.prevLink = prevNode;
            o.nextLink = null;
        }
    }

    private Object remove(int hash, Object key) {
        int index = hash % table.length;
        DoublyNode<E> node = table[index];
        DoublyNode<E> removeNode = null;
        DoublyNode<E> prevNode = null;

        if (node == null) {
            return null;
        }
        while (node != null) {
            if (node.hash == hash && (node.key == key || node.key.equals(key))) {
                removeNode = node;
                if (prevNode == null) {
                    table[index] = node.next;
                } else {
                    prevNode.next = node.next;
                }
                unlinkNode(node);
                node = null;
                size--;
                break;
            }
            prevNode = node;
            node = node.next;
        }
        return removeNode;
    }

    @Override
    public boolean remove(Object o) {
        return remove(hash(o), o) != null;
    }

    @Override
    public boolean contains(Object o) {
        int index = hash(o) % table.length;
        DoublyNode<E> tempNode = table[index];
        if (tempNode == null) {
            return false;
        }
        while (tempNode != null) {
            if (tempNode.hash == hash(o) && tempNode.key == o || tempNode.key.equals(o)) {
                return true;
            }
            tempNode = tempNode.next;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        if (table != null && size > 0) {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            size = 0;
        }
        head = tail = null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MyLinkedListHashSet)) {
            return false;
        }
        MyLinkedListHashSet<E> oSet;

        try {
            oSet = (MyLinkedListHashSet<E>) o;
            if (oSet.size != size()) {
                return false;
            }
            for (int i = 0; i < oSet.table.length; i++) {
                DoublyNode<E> oNode = oSet.table[i];
                while (oNode != null) {
                    if (!contains(oNode)) {
                        return false;
                    }
                }
                oNode = oNode.next;
            }
        } catch (ClassCastException e) {
            return false;
        }
        return true;
    }
}
