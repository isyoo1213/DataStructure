package main.sourcecode.queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class MyArrayQueue<E> implements QueueInterface<E>, Cloneable{

    private static final int DEFAULT_CAPACITY = 64;

    private Object[] array;
    private int size;

    private int front;
    private int rear;

    public MyArrayQueue() {
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    public MyArrayQueue(int capacity) {
        this.array = new Object[capacity];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
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

    private void resize(int newCapacity) {
        //parameter로 넘어오는 newCapacity에 대한 검증로직 필요 ex) newCapacity >= array.size + 1 처럼 기존의 array에 유요한 data보다 1 더 큰 capacity로 만들 수 있게끔 (front 포함해야하므로)
        int arrayCapacity = array.length;
        Object[] newArray = new Object[newCapacity];

        //front 위치공간은 1칸을 비우므로 index는 0이 아닌 1부터 시작
        // + 실제 length는 size와 같다(index 0은 front를 위한 빈 자리) + capacity는 null을 포함한 용량이므로 size와 같거나 클 수밖에 없다
        // -> rear를 고려하지 않고, 기존의 배열을 front+1부터 size만큼 복사하는 개념
        for (int i = 1, j = front + 1; i <= size; i++, j++) {
            //front가 array의 마지막 capacity에 있을 경우 (index는 capacity - 1) -> 실제 자료(front + 1, 즉 array의 마지막 index + 1) 는 index = 0에 저장되어 있으므로 이를 capacity로 나눠준다
            newArray[i] = array[j % arrayCapacity];
        }

        this.array = null;
        this.array = newArray;
    }

    @Override
    public boolean offer(E item) {
        if ((rear + 1) % array.length == front) {
            resize(array.length * 2);
        }
        rear = (rear + 1) % array.length;
        array[rear] = item;
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        front = (front + 1) % array.length;

        @SuppressWarnings("unchecked")
        E data = (E) array[front];

        array[front] = null;
        size--;

        if (array.length > DEFAULT_CAPACITY && size < (array.length / 4)) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }
        return data;
    }

    public E remove() {
        E data = poll();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        front = (front + 1) % array.length;

        @SuppressWarnings("unchecked")
        E data = (E) array[front];

        return data;
    }

    public E element() {
        E data = peek();
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    public Object[] toArray() {
        return toArray(new Object[size]);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] array) {
        final T[] res;
        if (array.length < size) {
            if (front <= rear) {
                return (T[]) Arrays.copyOfRange(this.array, front + 1, rear + 1, array.getClass());
                //arrayQueue의 size만큼(capacity가 아니다)의 length를 가지는 array를 반환
                //by Araays.copyOfRange() 내부의 Math.min(original - from, to - from)
            }
            res = (T[]) Arrays.copyOfRange(this.array, 0, size, array.getClass());
            int rearPartLength = this.array.length - front - 1; //length와 index

            // ** rearPartLength가 0일 경우 - front가 마지막 index에 존재하고 실제 데이터는 index 0부터 포함되어있는 경우
            // 즉, array의 뒷부분에 실질적인 데이터가 들어가있는 경우를 분기 -> 해당 부분 복사 필요
            if (rearPartLength > 0) {
                System.arraycopy(this.array, front + 1, res, 0, rearPartLength);
            }
            // rearPartLength가 0인 경우 + 앞부분 복사하기
            System.arraycopy(this.array, 0, res, rearPartLength, rear + 1);
            //메서드 뜯어보면서 params의 의미 따져보면 정확함

            return res;
        }

        if (front <= rear) {
            System.arraycopy(this.array, front + 1, array, 0,  size);
        } else {
            int rearpartLength = this.array.length - front - 1;
            if (rearpartLength > 0) {
                System.arraycopy(this.array, front + 1, array, 0, rearpartLength);
            }
            System.arraycopy(this.array, 0, array, rearpartLength, rear+1);
        }
        return array;
    }

    //*** arrayQueue 자체를 가리키는 객체는 깊은 복사가 되지만, 내부의 요소들은 기존 arrayQueue의 필드와 같은 주소를 참조한다
    @Override
    public Object clone() {
        try {
            @SuppressWarnings("unchecked")
            MyArrayQueue<E> cloneArrayQueue = (MyArrayQueue<E>) super.clone();
            cloneArrayQueue.array = Arrays.copyOf(array, array.length);
            return cloneArrayQueue;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public void sort() {
        //** sort(comparator)에서 toArray() 호출을 통해 array의 size에 맞게끔 배열을 다시 resizing하므로 null값인 요소를 고려하지 않아도 됨
        sort(null);
    }

    public void sort(Comparator<? super E> comparator) {
        //capacity와 size의 차이로 인한 null 요소를 방지하기 위해 size만큼의 사이즈 배열을 반환하는 toArray()로 배열화
        // * arrayQueue가 꽉 차있다고 하더라도, front의 존재로 최소 1개의 인덱스는 null을 가지고 있다
        Object[] res = toArray();

        //Comparator<? super E> comparator를 param으로 받는 sorting을 한번에 처리하기 위해 Object[]을 E[]로 캐스팅 해준다
        Arrays.sort((E[]) res, 0, size, comparator);

        //front의 존재로 array의 0 인덱스를 사용할 수 없으므로 이를 처리하기 위한 과정
        clear();

        // *** res.length는 size + 1 임을 꼭 인식해야 한다
        // 즉, front를 포함하기 위해 array의 capacity는 size + 1 만큼 복사됨
        System.arraycopy(res, 0, array, 1, res.length);

        this.rear = this.size = res.length;
        //array.length == res.length + 1 == size + 1
        // -> array의 rear는 가장 마지막 인덱스, 즉 size이자 res.length와 같다
    }
}
