package main.sourcecode.list;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayListTest {

    public static void main(String[] args) {
        MyObject[] objArray = new MyObject[3];
        objArray[0] = new MyObject(1, "azuma");
        objArray[1] = new MyObject(2, "lily");
        objArray[2] = new MyObject(3, "komachi");

        List<MyObject> objList = Arrays.asList(objArray);
        //실제로는 Arrays의 내부 private static 클래스인 ArrayList<E>의 인스턴스를 반환
        // - 이 ArrayLIst 클래스는 AbstractList를 상속받고 있으며, 이는 List 인터페이스를 구현한 클래스 >> collection의 ArrayList보다 적은 메서드를 가지고 있다.
        // - 단순히 E[]를 받아서 필드로 저장하고, 이를 내부적으로 처리할 수 있는 메서드를 가지고 있는 형식
        //static 클래스인 이유 - 내부에 static 필드를 가지고 있음(private static final long serialVersionUID = -2764017481108945198L;)
        //즉, outer인 Arrays 클래스의 인스턴스와는 상관없음

        //objList.add(new MyObject(4, "test"));
        // *** List 인터페이스를 구현한 AbstractList 클래스는 add()/remove() 메서드를 가지고 있다
        // -> 즉, Arrays의 ArrayList는 List 인터페이스를 구현하는 것이 아닌, AbstractList를 '상속'하고 있고 AbstractList의 add()/remove() 메서드는 무조건 오류를 발생시킨다
        // -> 실행하면 UnsupportedOperationException이 발생함
        // *** 즉, AbstractList를 구현한 클래스인 Arrays의 ArrayList는, set()/add()/remove()등을 동작가능하도록 override하지 않으면, 인자로 받는 배열의 크기가 고정되어 있음

        //ArrayList<MyObject> objArrayList = (ArrayList) objList;
        //Arrays의 ArrayList와 ArrayList의 필드는 서로 다르고 상속관계가 아님
        //ArrayList가 Arrays 보다 더 넓은 범위의 필드를 가지고 있음
        // + Arrays의 ArrayList 인스턴스를 받아 ArrayList를 생성하는 방식으로 사용 가능

        ArrayList<MyObject> objArrayList = new ArrayList<>(objList);
        // *** collection의 ArrayList는 AbstractList의 set()/add()/remove() 등의 메서드들을 override하고 있음

        // 그렇다면 List와 array, arrayList의 명확한 경계선은 어디일까
        // 1. array
        // array는 primative처럼 자바가 기본적으로 제공하는 '자료형'으로서의 느낌이 강함
        // - but, 실제로는 class 처럼 참조하지만, 실제 class를 사용해서 생성하는 메모리 영역이 아님
        // - but, 기본 자료형과는 다르게 참조된 메모리 공간 내부에 다른 class들의 인스턴스들을 담을 수 있음
        // ->> 어찌됐건, array라는 클래스를 통해 인스턴스를 확보하는 것은 아님
        // 2. list
        // list는 추상적인 개념으로 접근하는 경향이 있음 by interface/abstract class
        // '빈 틈이 없는' + '순서가 존재하는' 이라는 list의 기본적인 전제만 충족시킨다면, 이를 구현하는 방식에 자유를 부여하는 것 같음
        // 3. ArrayList
        // List 인터페이스를 구현한 AbstractList 클래스를 상속한 클래스
        // 내부적으로 array 자료형을 사용하지만, 클래스의 인스턴스는 List의 기능을 수행
        // 즉 내부적으로 사용되는 array의 index에 고정된 데이터를, 메서드를 통해 list의 기본 전제를 충족시키는 형태로 구현하는 클래스
        // JAVA가 제공하는 pure한 List 형태의 자료형은 존재하지 않는다. 구현할 뿐이다.


       for (MyObject obj : objArrayList) {
            System.out.println(obj);
        }

        for (int i = 0; i < objArrayList.size(); i++) {
            System.out.println(objArrayList.get(i));
        }

    }

}
