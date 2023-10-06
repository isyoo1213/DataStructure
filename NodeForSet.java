package main.sourcecode.set;

public class NodeForSet<E> {
    final int hash;
    final E key;
    Node<E> next;

    public NodeForSet(int hash, E key, Node<E> next) {
        this.hash = hash;
        this.key = key;
        this.next = next;
    }
}
