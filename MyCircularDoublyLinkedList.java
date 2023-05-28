package main.sourcecode.list;

import java.util.NoSuchElementException;

public class MyCircularDoublyLinkedList<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyCircularDoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public Node<E> search(int index) {
        if (head == null) {
            throw new IndexOutOfBoundsException("리스트가 비어있습니다.");
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> n = head;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        return n;
    }

    public Node<E> efficientSearch(int index) {
        if (head == null) {
            throw new IndexOutOfBoundsException("리스트가 비어있습니다.");
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<E> n = head;
        if ((size / 2) >= index) {
            for (int i = 0; i < index; i++) {
                n = n.next;
            }
        } else {
            for (int i = size - 1; i > index; i--) {
                n = n.prev;
            }
        }
        return n;
    }

    public void addFirst(E value) {
        Node<E> first = head;
        // tail    head -> next ... tail
        Node<E> newNode = new Node<E>(null, value, first);
        //우선 circular에 대한 처리는 나중에 처리하는 것으로 가정하고 prev노드는 null로 생성
        head = newNode;
        size++;

        // List에 자료가 없을 경우
        if (first == null || tail == null) {
            tail = newNode;
            //circular 처리
            // 1. head -> next
            // 2. tail -> head
            newNode.next = head;
            // 3. head -> tail
            // 4. tail
            newNode.prev = head;
            //tail.next = head;
            //어차피 head/tail이 모두 newNode를 가리키므로 head/tail로 구분해서 처리할 필요가 없다
        }

        // List에 자료가 존재할 경우 circular 처리
        //tail -> head    next ... tail
        tail.next = head;
        //tail <- head    next ... tail
        head.prev = tail;
        //tail    head <- next ... tail
        first.prev = head;
    }

    public void addLast(E value) {
        Node<E> last = tail;
        //tail    head ... prev <- tail
        Node<E> newNode = new Node<>(last, value, null);
        //우선 circular에 대한 처리는 나중에 처리하는 것으로 가정하고 next노드는 null로 생성
        tail = newNode;

        // List에 자료가 없을 때 -> last가 null이므로 last의 필드를 사용하는 못하는 것에 대한 처리
        if (head == null || last == null) {
            head = newNode;
            //circular 처리
            //2. tail    head <- tail
            //1. tail <- head    tail
            newNode.prev = newNode;
            //3. tail -> head    tail
            //4. tail    head -> tail
            newNode.next = newNode;
        }

        // tail -> head ... prev    tail
        tail.next = head;
        // tail <- head ... prev    tail
        head.prev = tail;
        // tail    head ... prev -> tail
        last.next = tail;
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
        Node<E> next = efficientSearch(index);
        Node<E> newNode = new Node<>(next.prev, value, next);
        next.prev.next = newNode;
        next.prev = newNode;
        size++;
    }

    public E removeLast() {
        if (head == null || tail == null) {
            throw new NoSuchElementException();
        }
        E data = tail.data;
        Node<E> prev = tail.prev; //head and tail or prev

        tail.data = null; //head and tail
        tail.next = null; //head and tail
        tail.prev = null; //head and tail or prev

        //tail = prev;
        //Doubly처럼 처리할 경우, Circuly에서 요소가 1개 이상 존재하는 한, prev는 항상 값을 갖고 있으므로 이렇게 처리해주면 안된다

        //기존의 prev 값의 null여부로 분기하는 것이 아닌, size 자체로 분기
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            prev.next = head;
            head.prev = prev;
        }

        size--;
        return data;
    }

    private static class Node<E>{
        private E data;
        private Node<E> prev;
        private Node<E> next;

        public Node( Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}
