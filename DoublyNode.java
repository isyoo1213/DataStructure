package main.sourcecode.set;

public class DoublyNode<E> {
    final int hash;
    final E key;

    Node<E> next; //for separating chaining
    Node<E> nextLink;
    Node<E> prevLink;

    public DoublyNode(int hash, E key, Node<E> next) {
        this.hash = hash;
        this.key = key;
        this.next = next;

        this.nextLink = null;
        this.prevLink = null;
    }
}
