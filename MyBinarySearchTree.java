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

    private Node<E> getSuccessorAndUnlink(Node<E> node) {
        Node<E> currentParent = node;
        Node<E> current = node.right;
        if (current.left == null) {
            currentParent.right = current.right;
            if (currentParent.right != null) {
                currentParent.right.parent = currentParent;
            }
            current.right = null;
            return current;
        }

        while (current.left != null) {
            currentParent = current; //대체할 노드의 오른쪽 자식 노드의 유무를 확인 후 *** 대체할 노드의 자리에 올려주기 위해 탐색노드의 부모노드를 계속 유지
            current = current.left;
        }

        currentParent.left = current.right; //대체할 노드의 부모에서, 대체할 노드의 오른쪽 자식 노드를 연결
        if (currentParent.left != null) {
            currentParent.left.parent = currentParent; // 대체할 노드의 오른쪽 자식 노드가 null이 아니라면, 대체할 노드의 부모노드를 향한 방향으로도 parent로 연결
        }

        current.right = null; //대체할 노드에서 오른쪽 자식 노드의 연결을 해제
        return current;
    }

    private Node<E> deleteNode(Node<E> node) {
        if (node != null) {
            //자식 노드가 없을 경우
            if (node.left == null && node.right == null) {
                if (node == root) {
                    root = null;
                } else {
                    node = null;
                }
                return null;
            }
            //자식 노두가 모두 있을 경우
            if (node.left != null && node.right != null) {
                Node<E> relpaceNode = getSuccessorAndUnlink(node);
                node.value = relpaceNode.value;
            //왼쪽 노드만 존재하는 경우
            } else if (node.left != null) {
                if (node == root) {
                    node = node.left;
                    root = node;
                    root.parent = null;
                } else {
                    node = node.left;
                }
            //오른쪽 노드만 존재하는 경우
            } else {
                if (node == root) {
                    node = node.right;
                    root = node;
                    root.parent = null;
                } else {
                    node = node.right;
                }
            }
        }
        return node;
    }

}
