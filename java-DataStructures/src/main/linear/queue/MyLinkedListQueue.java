package main.linear.queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class MyLinkedListQueue<E> implements QueueInterface<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    MyLinkedListQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return false;
        }
        return true;
    }

    public boolean contains(Object value) {
        Node<E> n = head;

        while (n != null) {
            if (n.data.equals(value)) {
                return true;
            }
            n = n.next;
        }
        return false;
    }

    public void clear() {
        for (Node<E> n = head; n != null; ) {
            Node<E> nextNode = n.next;
            n.data = null;
            n.next = null;
            n = nextNode;
        }
        size = 0;
        head = tail = null;
    }

    @Override
    public boolean offer(E data) {
        Node<E> newNode = new Node<>(data);
        if (size == 0) {
            head = newNode;
        } else {
            //circuly가 아니므로 data가 존재하지 않을 경우 tail.next(==head==tail)가 head를 가리키지 않도록 else로 분기
            tail.next = newNode;
        }
        tail = newNode;
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (head == null || tail == null || size == 0) {
            return null;
        }
        E data = head.data;
        head.data = null;

        Node<E> next = head.next;
        if (next == null) {
            tail = null;
        }
        head.next = null;
        head = next;
        size--;

        return data;
    }

    public E remove() {
        E data = poll();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    @Override
    public E peek() {
        if (head == null || tail == null || size == 0) {
            return null;
        }
        return head.data;
    }

    public E element() {
        E data = peek();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for (Node<E> n = head; n != null; n = n.next) {
            array[index] = n.data;
            index++;
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (size > a.length) {
            return a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int index = 0;
        Object[] result = a;
        for (Node<E> n = head; n != null; n = n.next) {
            result[index] = n.data;
            index++;
        }
        //return (T[]) result;
        return a;
    }


    public Object clone() {
        try {
            @SuppressWarnings("unchecked")
            MyLinkedListQueue<E> clone = (MyLinkedListQueue<E>) super.clone();
            clone.head = null;
            clone.tail = null;
            clone.size = 0;

            for (Node<E> n = head; n != null; n = n.next) {
                clone.offer(n.data);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public void sort() {
        sort(null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sort(Comparator<? super E> comparator) {
        Object[] array = this.toArray();
        Arrays.sort(array, (Comparator) comparator);

        int index = 0;
        for (Node<E> n = head; n != null; n = n.next) {
            n.data = (E) array[index];
        }
    }

    class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }
}
