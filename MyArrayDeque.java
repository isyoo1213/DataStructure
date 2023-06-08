package main.sourcecode.queue;

import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements QueueInterface<E> {

    private static final int DEFAULT_CAPACITY = 64;
    private Object[] array;
    private int size;

    private int front;
    private int rear;

    public MyArrayDeque() {
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.front = 0;
        this. rear = 0;
    }

    public MyArrayDeque(int capacity) {
        this.array = new Object[capacity];
        this.size = 0;
        this.front = 0;
        this. rear = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object value) {
        int start = (front + 1) % array.length;

        for (int i = 0, index = start; i < size; i++, index = (index + 1) % array.length) {
            if (array[index].equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
        front = rear = size = 0;
    }

    // ** 이 resize()의 param에 대한 조건은 resize()를 호출하는 메서드에서 분기해서 들어오는 방식으로 구성할 예정
    private void resize(int newCapacity) {
        int arrayCapacity = array.length;
        Object[] newArray = new Object[newCapacity];

        // 새로 용량을 늘린 newArray에 자료를 복사할 때도 index 0에는 front가 위치하므로 i = 1 위치부터 데이터를 넣어주고, size와 같은 경우(마지막 index + 1)까지 반복
        // ** size와 capacity는 다름을 항상 인지
        for (int i = 1, j = front + 1; i <= size; i++, j++) {
            newArray[i] = array[j % arrayCapacity];
        }

        array = null;
        array = newArray;

        front = 0;
        //즉, 최소한 newArray의 capacity는 size+1
        rear = size;
    }

    @Override

    public boolean offer(E data) {
        return offerLast(data);
    }

    public boolean offerLast(E data) {
        //용적이 가득 찬 경우는 size/array.length로 접근하는 방식도 가능
        if (((rear + 1) % array.length) == front) {
            resize(array.length * 2);
        }

        rear = (rear + 1) % array.length;
        array[rear] = data;
        size ++;

        return true;
    }

    public boolean offerFirst(E data) {
        //용적이 가득 찬 경우를 front로 확인하는 방식 -> + array.length를 통해 음수 처리를 피한 것이 포인트
        if ((front - 1 + array.length) % array.length == rear) {
            resize(array.length * 2);
        }
        front = (front - 1 + array.length) / array.length;
        array[front] = data;
        size++;

        return true;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        front = (front + 1) % array.length;

        @SuppressWarnings("unchecked")
        E data = (E) array[front];

        array[front] = null;
        size--;

        if (array.length > DEFAULT_CAPACITY && size < (array.length) / 4) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }
        return data;
    }

    public E pollLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E data = (E) array[rear];
        array[rear] = null;
        rear = (rear -1 + array.length) % array.length;
        size--;

        if (array.length > DEFAULT_CAPACITY && size < (array.length / 4)) {
            resize(Math.min(DEFAULT_CAPACITY, (array.length / 2)));
        }
        return data;
    }

    public E remove() {
        return removeFirst();
    }

    public E removeFirst() {
        E data = pollFirst();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    public E removeLast() {
        E data = pollLast();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    public E peekFirst() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E data = (E) array[(front + 1) % array.length];
        return data;
    }

    public E peekLast() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E data = (E) array[rear];
        return data;
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        E data = peekFirst();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    public E getLast() {
        E data = peekLast();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

}
