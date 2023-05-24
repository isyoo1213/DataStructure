package main.sourcecode.list;

import java.util.NoSuchElementException;
import java.util.Objects;

public class MyDoublyLinkedList<E> {

    private Node<E> head;
    private Node<E> tail;

    private int size;

    public MyDoublyLinkedList(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    private Node<E> search(int index) {
        Node<E> n;

        if ((size / 2) >= index) {
            n = head;
            for (int i = 0; i < index; i++) {
                n = n.next;
            }
        } else {
            n = tail;
            for (int i = size - 1; i > index; i--) {
                n = n.prev;
            }
        }
        return n;
    }

    public void addFirst(E value) {
        Node<E> first = head;
        Node<E> newNode = new Node<>(null, value, first);

        head = newNode;

        if (first == null) {
            tail = newNode;
        } else {
            first.prev = newNode;
        }
        size++;
    }

    public void addLast(E value) {
        Node<E> last = tail;
        Node<E> newNode = new Node<>(tail, value, null);

        tail = newNode;

        if (tail == null) {
            head = newNode;
        } else {
            last.next = newNode;
        }
        size++;
    }

    public boolean add(E value) {
        addLast(value);
        return true;
    }

    public void add(int index, E value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            addFirst(value);
            return;
        }
        if (index == size) {
            addLast(value);
            return;
        }
        //여기까지 List의 size에 대한 분기가 대략적으로 결정됨 -> (size >= 2 && index >= 1 && index < size) 가정하는 분기
        // 1. size가 0일 경우 index처리가 불가능하므로 무조건 오류 발생
        // 2. size가 1일 경우 index는 0 or 1(size)이므로 모두 처리 가능
        // 3. size가 2 이상일 경우 + index가 0일 경우 addFirst()호출

        Node<E> prevNode = search(index - 1);
        Node<E> nextNode = prevNode.next;
        Node<E> newNode = new Node<>(prevNode, value, nextNode);

        prevNode.next = newNode;
        nextNode.prev = newNode;
        size++;
    }

    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }

        E data = head.data;
        Node<E> first = head.next;

        head.data = null;
        head.next = null;

        head = first;

        if (first == null) {
            tail = null;
        } else {
            first.prev = null;
            //head.prev = null;
        }
        size--;
        return data;
    }

    public E remove() {
        return removeFirst();
    }

    public E removeLast() {
        if (head == null && tail == null) {
            throw new NoSuchElementException();
        }
        E data = tail.data;
        Node<E> last = tail.prev;

        tail.data = null;
        tail.prev = null;
        tail = last;
        //last가 null이든 아니든 tail 처리 가능하므로 다음 if 분기에서 tail에 대한 처리 코드를 줄일 수 있음

        if (last == null) {
            head = null;
        } else {
            last.next = null;
        }
        size--;
        return data;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return removeFirst();
        }
        if (index == size - 1) {
            return removeLast();
        }

        Node<E> delNode = search(index);
        E data = delNode.data;
        Node<E> prevNode = delNode.prev;
        Node<E> nextNode = delNode.next;

        delNode.data = null;
        delNode.prev = null;
        delNode.next = null;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        size--;
        return data;
    }

    public boolean remove(Object value) {
        Node<E> delNode = null;
        Node<E> i = head;

        while (i != null) {
            if (Objects.equals(value, i)) {
                delNode = i;
                break;
            } else {
                i = i.next;
            }
        }

        // 1. List에 요소가 0개일 경우 or 찾으려는 node가 없을 경우
        // *** i == null 이면, 즉 List에 자료 자체가 없으면 delNode는 무조건 null이 되므로 i기준으로 따로 분기하지 않아도 된다
        if (delNode == null) {
            return false;
        }

        E data = delNode.data;
        Node<E> prevNode = delNode.prev;
        Node<E> nextNode = delNode.next;

        //3. List에 요소가 1개 이상일 경우
        // *** 1개일 경우 removeFirst()에서 head와 tail에 대한 처리 모두 가능하므로 정상적으로 처리 가능
        if (delNode == head) {
            removeFirst();
            return true;
        }
        if (delNode == tail) {
            removeLast();
            return true;
        }

        delNode.data = null;
        delNode.prev = null;
        delNode.next = null;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        size--;

        return true;
    }

    private static class Node<E>{
        private E data;
        private Node<E> prev;
        private Node<E> next;

        Node(Node<E> prev, E data, Node<E> next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }

}
