package main.sourcecode.heap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class MyArrayMinHeap<E> {

    private final Comparator<? super E> comparator;
    private static final int DEFAULT_CAPACITY = 10;
    private int size;
    private Object[] array;

    public MyArrayMinHeap() {
        this(null);
    }

    public MyArrayMinHeap(Comparator<? super E> comparator) {
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    public MyArrayMinHeap(int capacity) {
        this(capacity, null);
    }

    public MyArrayMinHeap(int capacity, Comparator<? super E> comparator) {
        this.array = new Object[capacity];
        this.size = 0;
        this.comparator = comparator;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int getParentIndex(int index) {
        return index/2;
    }

    private int getLeftChildIndex(int index) {
        return index * 2;
    }

    private int getRightChildIndex(int index) {
        return (index * 2) + 1;
    }

    private void resize(int newCapacity) {
        Object[] newArray = new Object[newCapacity];
        for (int index = 1; index <= size; index++) {
            newArray[index] = array[index];
        }
        this.array = null;
        this.array = newArray;
    }

    public void add(E value) {
        if (size + 1 == array.length) {
            resize(array.length * 2);
        }
        //마지막 data의 인덱스 + 1에 해당하는 index를 넘겨주고 해당 위치부터 sift를 통해 parent와 비교하며 자리를 잡아가는 방식
        siftUp(size + 1, value);
        size++;
    }

    public void siftUp(int index, E target) {
        if (this.comparator != null) {
            siftUpComparator(index, target, comparator);
        } else {
            siftUpComparable(index, target);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftUpComparator(int index, E target, Comparator<? super E> comparator) {
        while (index > 1) {
            int parentIndex = getParentIndex(index);
            Object parentVal = array[parentIndex];
            if (comparator.compare(target, (E) parentVal) >= 0) {
                break;
            }
            array[index] = parentVal;
            index = parentIndex;
        }
        array[index] = target;
    }

    @SuppressWarnings("unchecked")
    private void siftUpComparable(int index, E target) {
        Comparable<? super E> comparable = (Comparable<? super E>) target;
        while (index > 1) {
            int parentIndex = getParentIndex(index);
            Object parentVal = array[parentIndex];
            if (comparable.compareTo((E) parentVal) >= 0) {
                break;
            }
            array[index] = parentVal;
            index = parentIndex;
        }
        array[index] = comparable;
    }

    @SuppressWarnings("unchecked")
    public E remove() {
        if (array[1] == null) { //비어있을 경우
            throw new NoSuchElementException();
        }
        E result = (E) array[1];
        E target = (E) array[size];
        array[size] = null;

        siftDown(1, target);

        return result;
    }

    public void siftDown(int index, E target) {
        if (this.comparator != null) {
            siftDownComparator(index, target, this.comparator);
        } else {
            siftDownComparable(index, target);
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDownComparator(int index, E target, Comparator<? super E> comparator) {
        array[index] = null;

        int parentIndex = index;
        // *** 왼쪽 노드를 기준으로 비교 및 반복이 이루어지도록 세팅 (편의 상 + 실제 데이터가 적재되는 순서도 배열의 왼쪽부터 오른쪽, 즉 트리의 왼쪽부터 적재하도록 기준을 잡았으므로 직관적이게끔)

        //자식 레벨 노드의 left/right 중 우선은 left를 기준으로 잡고 시작 -> 이후 right와 비교해 더 작은 값을 child로 선택하므로, 굳이 변수 이름에 left를 붙이지 않음
        int childIndex;
        //*** 아래처럼 하지 않는 이유 - while문의 조건에서 'parent'의 값을 자식 노드의 인덱스로 증가시키는 방식으로 구성
        //-> getParent()를 통해 자식 노드의 index를 가져오기 위한 방법으로 구성하지만, 아래처럼 childIndex를 직접 변경시키는 방식으로 구성하는 것도 가능
        //int childIndex = getLeftChildIndex(index);

        while ((childIndex = getLeftChildIndex(parentIndex)) <= size) {
            int rightChildIndex = getRightChildIndex(index);
            Object childVal = array[childIndex];
            // *** rightChildIndex는 array의 범위를 벗어날 수 있음 ex) leftChild가 array의 마지막일 경우
            //Object rightChildVal = array[rightChildIndex];

            //*** 이 경우는 자식 레벨 노드의 left/right가 같은 값을 가질 경우, left 노드를 선택하게 됨 (left 노드가 array의 인덱스 상 앞부분이므로 자식의 자식 노드 레벨에서도 앞부분을 차지하고 왼쪽이라는 기준에 부합)
            if (rightChildIndex <= size && comparator.compare((E) childVal, (E) array[rightChildIndex]) > 0) {
                childIndex = rightChildIndex;
                childVal = array[rightChildIndex];
            }

            //*** 부등호가 같은 경우는 어차피 똑같은 값이 들어가게 되므로 상관없다
            if (comparator.compare(target, (E) childVal) <= 0) {
                break;
            }
            array[parentIndex] = childVal;
            parentIndex = childIndex;
        }
        array[parentIndex] = target;

        //** size를 지금 줄이면 while문 내의 조건에서 size에 영향을 주지 않을까?
        //-> size가 홀수일 경우 -> size - 1 == 짝수 == leftChild ----> 왼쪽 자식 노드를 구할 경우에는 전혀 문제가 없다
        //-> size가 짝수인 경우 -> 트리 자체는 왼쪽 자식 노드까지만 가지고 있으므로 자식 노드의 left/right를 비교하는 작업 자체가 필요 없으므로 반복문 내에 접근할 필요가 없다
        size--;

        if (array.length > DEFAULT_CAPACITY && size < array.length / 4) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDownComparable(int index, E target) {
        Comparable<? super E> comparable = (Comparable<? super E>) target;

        array[index] = null;

        int parentIndex = index;
        int childIndex;
        while ((childIndex = getLeftChildIndex(parentIndex)) <= size) {
            int rightChildIndex = getRightChildIndex(parentIndex);
            Object childVal = array[childIndex];
            if (rightChildIndex <= size && ((Comparable<? super E>) childVal).compareTo((E) array[rightChildIndex]) > 0){
                childIndex = rightChildIndex;
                childVal = array[rightChildIndex];
            }
            if (comparable.compareTo((E) childVal) <= 0) {
                break;
            }
            array[parentIndex] = childVal;
            parentIndex = childIndex;
        }
        array[parentIndex] = comparable;

        //** size를 지금 줄이면 while문 내의 조건에서 size에 영향을 주지 않을까?
        //-> size가 홀수일 경우 -> size - 1 == 짝수 == leftChild ----> 왼쪽 자식 노드를 구할 경우에는 전혀 문제가 없다
        //-> size가 짝수인 경우 -> 트리 자체는 왼쪽 자식 노드까지만 가지고 있으므로 자식 노드의 left/right를 비교하는 작업 자체가 필요 없으므로 반복문 내에 접근할 필요가 없다
        size--;

        if (array.length > DEFAULT_CAPACITY && size < array.length / 4) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }
    }

    @SuppressWarnings("unchecked")
    public E peek(){
        E data = (E) array[1];
        if (data == null) {
            throw new NoSuchElementException();
        }
        return data;
    }

    public Object[] toArray() {
        //다른 처리 없이 그냥 간단하게 index 0을 포함한 array를 반환하는 방식으로만 구성
        return Arrays.copyOf(array, size + 1);
    }

}
