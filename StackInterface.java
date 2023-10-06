package main.sourcecode.stack;

public interface StackInterface<E> {
    E push(E data);

    E pop();

    E peek();

    int search(Object value);

    int size();

    void clear();

    boolean empty();
}
