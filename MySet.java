package main.sourcecode.set;

public interface MySet<E> {

    boolean add(E e);

    boolean remove(Object o);

    boolean contains(Object o);

    boolean equals(Object o);

    boolean isEmpty();

    int size();

    void clear();
}
