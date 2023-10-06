package main.nonlinear.set;

public class NodeForSet<E> {
    final int hash;
    final E key;
    NodeForSet<E> next;

    public NodeForSet(int hash, E key, NodeForSet<E> next) {
        this.hash = hash;
        this.key = key;
        this.next = next;
    }
}
