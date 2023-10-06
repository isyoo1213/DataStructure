package main.sourcecode.list;

import java.util.Arrays;
import java.util.Objects;

public class MySinglyLinkedList<E> {
    private Node<E> head;
    private Node<E> tail;
    //addLast()와 같은 메서드를 활용할 수 있도록 노드 순회에 효율성을 위해 필드로 선언

    private int size;

    public MySinglyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    private Node<E> search(int index) {
        Node<E> next = head;

        // i < index인 이유
        // next 자체는 다음 노드를 가리키므로 탐색하고자 하는 노드 index의 바로 전 노드 까지만 탐색해야 한다
        // 첫 노드는 head 필드에서 가져올 수 있으므로 index는 1 이상의 int로 활용
        for (int i = 0; i < index; i++) {
            next = next.next;
        }

        return next;
    }

    public void addFirst(E value) {
        Node<E> first = head;
        Node<E> newNode = new Node<>(value, first);
        size++;
        head = newNode;

        if (first == null) {
            tail = newNode;
        }
    }

    public void addLast(E value) {
        Node<E> last = tail;
        Node<E> newNode = new Node<>(value, null);
        size++;
        tail = newNode;

        if (last == null) {
            head = newNode;
        } else {
            last.next = newNode;
        }
    }

    public void add(int index, E value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            addFirst(value);
            return;
        }
        if (index == size - 1) {
            addLast(value);
            return;
        }

        Node<E> prevNode = search(index - 1);
        Node<E> nextNode = prevNode.next;
        Node<E> newNode = new Node<>(value, nextNode);
        prevNode.next = newNode;
        size++;
    }

    public E removeFirst() {
        //1. list에 아무런 데이터가 없을 경우
        if (head == null) {
            throw new IndexOutOfBoundsException();
        }

        //2. list에 2개 이상의 데이터가 있을 경우
        //head에 대한 처리 O / tail에 대한 처리 X

        //삭제시 백업 용
        E returnValue = head.data;

        //head에 연결된 node 참조를 임시 저장
        Node<E> first = head.next;

        head.next = null;
        head.data = null;

        head = first;

        //3. list에 head만 존재하는 경우
        //위에서 head에 대한 처리가 완료 됐고, tail에 대한 처리만 추가적으로 진행
        if (head == null) {
            tail = null; //head에 대한 처리는 완료됐으므로 tail에 대한 처리만
        }

        size--;
        return returnValue;
    }

    public E remove() {
        return removeFirst();
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // 1. head일 경우
        if (index == 0) {
            return removeFirst();
        }

        // 2. head/tail이 아닐 경우
        Node<E> prevNode = search(index - 1);
        Node<E> delNode = prevNode.next;
        Node<E> nextNode = delNode.next;
        E returnValue = delNode.data; //삭제 전 데이터 백업

        delNode.data = null;
        delNode.next = null;

        prevNode.next = nextNode;

        //3. tail일 경우
        if (size > 1 && index == size - 1) {
            tail = prevNode;
        }

        size--;

        return returnValue;
    }

    public boolean remove(E value) {

        //1.리스트에 데이터가 없을 경우
        if (head == null) {
            throw new RuntimeException();
        }

        //2. 리스트에 데이터가 있을 경우
        //singly는 단방향으로만 연결되어있으므로, 요소를 순회할 때 직접 prev node에 대한 정보를 저장해주어야함
        Node<E> prevNode = null;
        Node<E> delNode = null;
        Node<E> nextNode = null;

        //head부터 param으로 전달받은 객체 탐색 시작
        Node<E> i = head;
        while (i != null) {
            if (Objects.equals(i.data, value)) {
                delNode = i;
                break;
            }
            //객체 비교 후 다음 탐색 순회를 위한 prev node와 i node 설정
            prevNode = i;
            i = i.next;
        }
        // 객체가 없을 경우
        if (delNode == null) {
            return false;
        }
        // 1. 객체가 head일 경우
        if (delNode == head) {
            removeFirst();
            return true;
        }

        // 2. 객체가 head/tail이 아닐 경우
        nextNode = delNode.next;

        delNode.data = null;
        delNode.next = null;

        // 3. 객체가 tail일 경우
        if (delNode == tail) {
            tail = prevNode;
        }

        size--;

        //마지막으로 객체 삭제 후 prev/next node 참조 연결
        prevNode.next = nextNode;
        return true;
    }

    public E removeLast() {
        return remove(size - 1);
    }

    // *** static으로 선언한 이유
    // outer의 this 인스턴스에 대한 참조와 메모리 누수를 예방하기 위해
    // + outer에 대한 참조가 필요하지 않을 경우에만 static 키워드가 가능
    // *** static 클래스라고 해서 인스턴스가 static으로 생성된다는 의미가 아님을 인지
    // -> 이는 outer에서 필드로 사용할 경우에 결정되는 부분
    private static class Node<E> {
        private E data;
        private Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return search(index).data;
    }

    public void set(int index, E value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> replaceNode = search(index);
        replaceNode.data = null;
        replaceNode.data = value;
    }

    @Override
    public String toString() {
        if (head == null) {
            return "[]";
        }
        Object[] array = new Object[size];

        int index = 0;
        Node<E> n = head;
        while (n != null) {
            array[index] = (E) n.data;
            index++;
            n = n.next;
        }

        return Arrays.toString(array);
    }
}
