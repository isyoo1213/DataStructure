package main.linear.queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class MyLinkedListDeque<E> implements QueueInterface<E>, Cloneable{

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size==0;
    }

    public boolean contains(Object value) {
        for (Node<E> n = head; n != null;) {
            if (n.data.equals(value)) {
                return true;
            }
        }
        return false;
    }

    //** for loop에 n = n.next로 n을 변경할 경우, 이미 블럭 내에서 n에 대한 정보 삭제를 수행하므로 null이 할당되므로 유의해야 함

    public void clear() {
        for (Node<E> n = head; n != null;) {
            Node<E> next = n.next;
            n.data = null;
            n.next = null;
            n.prev = null;

            n = next;
        }
        size = 0;
        head = tail = null;
    }

    public MyLinkedListDeque() {
        head = null;
        tail = null;
        size = 0;
    }

    public boolean offerFirst(E value) {
        Node<E> newNode = new Node<>(value);
        newNode.next = head;
        if (head != null) {
            head.prev = newNode;
        }
        head = newNode;
        size++;
        if (head.next == null) {
            tail = head;
        }
        return true;
    }

    public boolean offerLast(E value) {
        Node<E> newNode = new Node<>(value);
        newNode.prev = tail;

        if (tail != null) {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
        if (tail.prev == null) {
            head = newNode;
        }
        return true;
    }

    @Override
    public boolean offer(E value) {
        return offerLast(value);
    }

    public E pollFirst() {
        if (size == 0 || head == null || tail == null) {
            return null;
        }
        E data = head.data;
        Node<E> nextNode = head.next;

        head.data = null;
        head.next = null;
        size--;

        head = nextNode;
        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }
        return data;
    }

    public E pollLast() {
        if (size == 0 || head == null || tail == null) {
            return null;
        }
        E data = tail.data;
        Node<E> prevNode = tail.prev;

        tail.prev = null;
        tail.data = null;
        size--;
        tail = prevNode;

        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }
        return data;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    public E remove() {
        return removeFirst();
    }

    public E removeFirst() {
        E data = poll();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    public E removeLast() {
        E data = pollLast();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    public E peekFirst() {
        if (size == 0 || head == null || tail == null) {
            return null;
        }
        E data = head.data;
        return data;
    }

    public E peekLast() {
        if (size == 0 || head == null || tail == null) {
            return null;
        }
        E data = tail.data;
        return data;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    public E getFirst() {
        E data = peek();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    public E getLast() {
        E data = peekLast();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    public E element() {
        return getFirst();
    }

    public Object[] toArray() {
        Object[] objArray = new Object[size];
        int index = 0;
        for (Node<E> n = head; n != null; n = n.next) {
            objArray[index] = (E) n.data;
            index++;
        }
        return objArray;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int index = 0;
        Object[] result = a;
        for (Node<E> n = head; n != null; n = n.next) {
            result[index] = n.data;
            index++;
        }
        return a;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            MyLinkedListDeque<E> newDeque = (MyLinkedListDeque<E>) super.clone();
            newDeque.head = null;
            newDeque.tail = null;
            newDeque.size = 0;

            for (Node<E> n = head; n != null; n = n.next) {
                newDeque.offer(n.data);
            }
            return newDeque;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sort(Comparator<? super E> comparator) {
        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) comparator);

        int index = 0;
        for (Node<E> n = head; n != null; n = n.next) {
            n.data = (E) a[index];
            index++;
        }
    }

    public void sort() {
        sort(null);
    }

    private static class Node<E> {

        E data;
        Node<E> next;
        Node<E> prev;

        Node(E data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}
