package main.sourcecode.list;

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
