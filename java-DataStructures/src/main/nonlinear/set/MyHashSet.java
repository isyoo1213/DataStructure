package main.nonlinear.set;

import java.util.Arrays;

public class MyHashSet<E> implements MySet<E> {

    private final int DEFAULT_CAPACITY = 1 << 4;

    private final static float LOAD_FACTOR = 0.75f;

    NodeForSet<E> [] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        table = (NodeForSet<E>[]) new NodeForSet[DEFAULT_CAPACITY];
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
        final NodeForSet<E>[] newTable = (NodeForSet<E>[]) new NodeForSet[newCapacity];
        for (int i = 0; i < table.length; i++) {
            NodeForSet<E> value = table[i];
            if (value == null) {
                continue;
            }
            table[i] = null;
            NodeForSet<E> nextNode;
            while (value != null) {
                int index = value.hash % newCapacity;
                if (newTable[index] != null) {
                    NodeForSet<E> tail = newTable[index];
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
            table[index] = new NodeForSet<E>(hash, key, null);
        } else {
            NodeForSet<E> temp = table[index];
            NodeForSet<E> prev = null;
            while (temp != null) {
                if ((temp.hash == hash) && (temp.key == key || temp.key.equals(key))) {
                    return key;
                }
                prev = temp;
                temp = temp.next;
            }
            prev.next = new NodeForSet<E>(hash, key, null);
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

    private Object remove(int hash, Object key) {
        int index = hash % table.length;
        NodeForSet<E> node = table[index];
        NodeForSet<E> removeNode = null;
        NodeForSet<E> prev = null;
        if (node == null) {
            return null;
        }
        while (node != null) {
            // *** hash() 함수 처리 이전의 실제 hash가 양수/음수일 경우, hash()처리된 hash가 같고, 이렇게 hash()처리된 hash를 Node의 hash필드로 사용하므로 단순히 hash비교만 해주어서는 안된다
            // *** + key의 비교에 있어, hashCode()를 적절히 overridng한 경우라면 ==과 equals()처리가 동일하지만, hashCode() 처리를 적절히 수행하지 않는 사용자 클래스 자료형이 존재할 수 있으므로,
            // call by value와 call by reference 중 하나만 만족하더라도 같은 객체로 판단하도록 구성한다
            if (node.hash == hash && (node.key == key || node.key.equals(key))) {
                removeNode = node;
                if (prev == null) {
                    table[index] = node.next;
                    node = null;
                } else {
                    prev.next = node.next;
                    node = null;
                }
                size--;
                break;
            }
            prev = node;
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
        NodeForSet<E> temp = table[index];
        if (temp == null || o == null) {
            return false;
        }
        while (temp != null) {
            if (temp.key == o || temp.key.equals(o)) {
                return true;
            }
            temp = temp.next;
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
        // *** 기본기
        // 배열의 참조변수 선언은 null로 초기화되지만, new를 통해 생성할 경우 참조변수는 0번째 인덱스의 메모리 주소를 가진다
        // + 중요한 점은, *** 실제 인덱스들의 주소에 데이터를 할당해주지 않아도 참조주소 자체는 null이 아닌 0번째 인덱스의 메모리 주소를 가지고 있다는 점이다.
        // + 현재 table이 null이 가능한 경우는 clear()를 통해 '명시적'으로 null을 할당한 경우에만 국한된다.
        // 왜냐하면, 생성자에서 table이 기본적으로 new 생성자를 통해 생성된 상태이므로, 자연적으로 null로 초기화되는 경우의 수는 없다.
        // 따라서 실제로는, table에 자료가 적재되어있는 경우를 판단하기 위해서는 단순히 null이 아닌 size에 대한 판별까지 or가 아닌 and로 구성해주어야한다.
        // + for문을 돌리는 기준이 table.length이므로, 빈 배열 또한 기본 capacity만큼 null 할당 반복이 일어나므로 명확하게 처리해주는 것이 좋음
        if (table != null && size > 0) {

            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            size = 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MyHashSet)) {
            return false;
        }
        MyHashSet<E> oSet;
        try {
            oSet = (MyHashSet<E>) o;
            if (oSet.size() != size) {
                return false;
            }
            for (int i = 0; i < oSet.table.length; i++) {
                NodeForSet<E> oTemp = oSet.table[i];
                while (oTemp != null) {
                    if (!contains(oTemp)) {
                        return false;
                    }
                    oTemp = oTemp.next;
                }
            }
        } catch (ClassCastException e) {
            return false;
        }
        return true;
    }

    public Object[] toArray() {
        Object[] ret = new Object[size];
        int index = 0;
        for (int i = 0; i < table.length; i++) {
            NodeForSet<E> n = table[i];
            while (n != null) {
                ret[index] = n.key;
                index++;
                n = n.next;
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        Object[] copy = toArray();
        if (a.length < size) {
            return (T[]) Arrays.copyOf(copy, size, a.getClass());
        }
        System.arraycopy(copy, 0, a, 0, size);
        return a;
    }
}
