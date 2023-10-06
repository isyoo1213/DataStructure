package main.linear.stack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class MyStack<E> implements StackInterface<E>, Cloneable {

    private static final int DEFAULT_CAPACITY = 10; //최소(기본) Capacity
    private static final Object[] EMPTY_ARRAY = {};
    //private static final Object[] EMPTY_ARRAY = new Object[0];

    private Object[] array;
    private int size;

    public int getSize() {
        return size;
    }

    public MyStack() {
        this.array =EMPTY_ARRAY;
        this.size = 0;
    }

    public MyStack(int capacity) {
        this.array = new Object[capacity];
        this.size = 0;
    }

    private void resize() {
        if (Arrays.equals(array, EMPTY_ARRAY)) {
            array = new Object[DEFAULT_CAPACITY];
            return;
        }

        int arrayCapacity = array.length;

        if (size == arrayCapacity) {
            int newCapacity = arrayCapacity * 2;

            array = Arrays.copyOf(array, newCapacity);
            return;
        }
        if (size < arrayCapacity / 2) {
            int newCapacity = (arrayCapacity / 2);
            array = Arrays.copyOf(array, Math.max(DEFAULT_CAPACITY, newCapacity));
        }
    }

    @Override
    public E push(E data) {
        if (size == array.length) {
            resize();
        }
        array[size] = data;
        size++;
        return data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E pop() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        E data = (E) array[size - 1];
        array[size -1] = null;

        size--;
        resize();
        return data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E peek() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) array[size-1];
    }

    @Override
    public int search(Object value) {
        if (value == null) {
            for (int index = size - 1; index > 0; index--) {
                if (array[index] == null) {
                    return size - index;
                    //자기 자신을 가리킬 때, 즉 거리는 0이 아닌 1부터 시작하므로 size에서 1이 적은 index를 빼서 반환
                }
            }
        } else {
            for (int index = size - 1; index > 0; index--) {
                if (array[index] == value) {
                    return size - index;
                }
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
        resize();
    }

    @Override
    public boolean empty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() throws CloneNotSupportedException {
        MyStack<E> cloneStack = (MyStack<E>) super.clone();
        MyStack<?> wildCloneStack = (MyStack<?>) super.clone();

        cloneStack.array = new Object[size];
        System.arraycopy(array, 0, cloneStack.array, 0, size);
        cloneStack.resize();
        return cloneStack;
    }

    public Object[] toArray() {
        return Arrays.copyOf(array, size); //주의할 것은 객체 배열이므로 Arrays.copyOf()를 사용하든, System.arraycopy()를 사용하든 '얕은 복사'에 지나지 않는다.
    }

    //이미 생성되어있는 배열에 현재 Stack이 가지고 있는 배열에 복사해주고 싶을 경우
    //*** 단, 상속 관계에 있거나, Wrapper와 같이 유연한 캐스팅이 가능한 경우에만 사용할 수 있음
    public <T> T[] toArray(T[] a) {
        //param으로 전달되는 배열의 크기는 알 수 없으므로 분기
        if (a.length < size) {
            return (T[]) Arrays.copyOf(array, size, a.getClass());
            //copyOf() 메서드 내부 뜯어보면 골때리게 머리아프니 나중에 보자
        }
        System.arraycopy(array, 0, a, 0, size);
        return a;
    }

    public void sort() {
        array = this.toArray();
        Arrays.sort(array);
    }

    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> comp) {
        Arrays.sort((E[])array, comp);
    }
}
