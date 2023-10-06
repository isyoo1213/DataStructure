package main.nonlinear.tree;

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

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Object o) {
        if (comparator == null) {
            return containsUsingComparable(o);
        }
        return containsUsingComparator(o, comparator);
    }

    private boolean containsUsingComparable(Object o) {
        @SuppressWarnings("unchecked")
        Comparable<? super E> value = (Comparable<? super E>) o;
        Node<E> node = root;
        while (node != null) {
            int res = value.compareTo(node.value);
            if (res < 0) {
                node = node.left;
            } else if (res == 0) {
                return true;
            } else {
                node = node.right;
            }
        }
        return false;
    }

    private boolean containsUsingComparator(Object o, Comparator<? super E> comparator) {
        @SuppressWarnings("unchecked")
        E value = (E) o;
        Node<E> node = root;
        while (node != null) {
            int res = comparator.compare(value, node.value);
            if (res < 0) {
                node = node.left;
            } else if (res == 0) {
                return true;
            } else {
                node = node.right;
            }
        }
        return false;
    }

    public void clear() {
        size = 0;
        root = null;
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

    private E removeUsingComparable(Object value) {
        @SuppressWarnings("unchecked")
        E oldValue = (E) value;
        Node<E> parent = null;
        Node<E> current = root;
        boolean hasLeft = false;

        if (root == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Comparable<? super E> compValue = (Comparable<? super E>) value;

        do {
            int resComp = compValue.compareTo(current.value);
            if (resComp == 0) {
                break;
            }
            parent = current;
            if (resComp < 0) {
                hasLeft = true;
                current = current.left;
            } else {
                hasLeft = false;
                current = current.right;
            }
        } while (current != null);

        if (current == null) {
            return null;
        }

        if (parent == null) {
            deleteNode(current);
            size--;
            return oldValue;
        }

        //삭제할 노드가 왼쪽 노드라면
        if (hasLeft) {
            parent.left = deleteNode(current);
            if (parent.left != null) {
                parent.left.parent = parent;
            }
        } else {
            parent.right = deleteNode(current);
            if (parent.right != null) {
                parent.right.parent = parent;
            }
        }

        size--;
        return oldValue;
    }

    private E removeUsingComparator(Object value, Comparator<? super E> comparator) {
        @SuppressWarnings("unchecked")
        E oldValue = (E) value;
        Node<E> parent = null;
        Node<E> current = root;
        boolean hasLeft = false;

        if (root == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        E compValue = (E) value;

        do {
            int resComp = comparator.compare(compValue, current.value);
            if (resComp == 0) {
                break;
            }

            parent = current;
            if (resComp < 0) {
                hasLeft = true;
                current = current.left;
            } else {
                hasLeft = false;
                current = current.right;
            }
        } while (current != null);

        if (current == null) {
            return null;
        }

        if (parent == null) {
            deleteNode(current);
            size--;
            return oldValue;
        }

        if (hasLeft) {
            parent.left = deleteNode(current);
            if (parent.left != null) {
                parent.left.parent = parent;
            }
        } else {
            parent.right = deleteNode(current);
            if (parent.right != null) {
                parent.right.parent = parent;
            }
        }
        size--;
        return oldValue;
    }

    public E remove(Object o) {
        if (root == null) {
            return null;
        }
        if (comparator == null) {
            return removeUsingComparable(o);
        } else {
            return removeUsingComparator(o, this.comparator);
        }
    }

    /**
     * 전위 순회
     */
    public void preorder() {
        preorder(this.root);
    }

    public void preorder(Node<E> o) {
        //null이 아닐 때까지 재귀적으로 순회
        if (o != null) {
            System.out.println(o.value + ""); //부모 노드
            preorder(o.left);
            preorder(o.right);
        }
    }

    /**
     * 중위 순회
     */
    public void inorder() {
        inorder(this.root);
    }

    public void inorder(Node<E> o) {
        if (o != null) {
            inorder(o.left);
            System.out.println(o.value + "");
            inorder(o.right);
        }
    }

    /**
     * 후위 순회
     */
    public void postorder() {
        postorder(this.root);
    }

    public void postorder(Node<E> o) {
        if (o != null) {
            postorder(o.left);
            postorder(o.right);
            System.out.println(o.value + "");
        }
    }
}
