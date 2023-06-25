package main.sourcecode.tree;

import java.util.Comparator;

public class MyBinarySearchTree<E> {

    private Node<E> root;
    private int size;

    private final Comparator<? super E> comparator;

    public MyBinarySearchTree() {
        this(null);
    }

    public MyBinarySearchTree(Comparator<? super E> comparator) {
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
    }

    public boolean add(E value) {
        if (comparator == null) {
            return addUsingComparable(value) == null;
        }
        return addusingComparator(value, this.comparator) == null;
    }

    private E addUsingComparable(E value) {
        Node<E> current = root;

        if (current == null) {
            root = new Node<E>(value);
            size++;
            return null;
        }

        Comparable<? super E> compValue = (Comparable<? super E>) value;
        int compResult;
        Node<E> currentParent; //다음 순회를 위한 변수

        do {
            currentParent = current;
            compResult = compValue.compareTo(current.value);
            if (compResult < 0) {
                current = current.left;
            } else if (compResult > 0) {
                current = current.right;
            } else {
                return value;
            }
        } while (current != null);
        //순회 완료 - 즉, 우선적으로 null 유무를 떠나서 left/right 중 value가 들어갈 위치가 결정됨

        Node<E> newNode = new Node<E>(value, currentParent);
        if (compResult < 0) {
            current.left = newNode;
        } else {
            current.right = newNode;
        }
        size++;
        return null;
    }

    private E addusingComparator(E value, Comparator<? super E> comparator) {
        Node<E> current = root;
        if (current == null) {
            root = new Node<>(value, null);
            size++;
            return null;
        }

        Node<E> currentParent;
        int compResult;

        do {
            currentParent = current;
            compResult = comparator.compare(value, current.value);
            if (compResult < 0) {
                current = current.left;
            } else if (compResult > 0) {
                current = current.right;
            } else {
                return value;
            }
        } while (current != null);

        Node<E> newNode = new Node<E>(value, currentParent);
        if (compResult < 0) {
            currentParent.left = newNode;
        } else {
            currentParent.right = newNode;
        }
        size++;
        return null;
    }
}
