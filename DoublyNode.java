package main.sourcecode.set;

public class DoublyNode<E> {
    final int hash;
    final E key;

    DoublyNode<E> next; //for separating chaining
    DoublyNode<E> nextLink;
    DoublyNode<E> prevLink;

    public DoublyNode(int hash, E key, DoublyNode<E> next) {
        this.hash = hash;
        this.key = key;
        this.next = next;

        this.nextLink = null;
        this.prevLink = null;
    }
}
