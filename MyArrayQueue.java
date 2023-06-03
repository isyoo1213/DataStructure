package main.sourcecode.queue;

public class MyArrayQueue<E> implements QueueInterface<E>{

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
        return null;
    }

    @Override
    public E peek() {
        return null;
    }
}
