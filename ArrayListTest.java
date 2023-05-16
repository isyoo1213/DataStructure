package main.sourcecode.list;

import main.sourcecode.arrays.MyObject;

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
        //static 클래스인 이유 - 내부에 static 필드를 가지고 있음(private static final long serialVersionUID = -2764017481108945198L;)
        //즉, outer인 Arrays 클래스의 인스턴스와는 상관없음

        //objList.add(new MyObject(4, "test"));
        //Arrays의 ArrayList는 크기가 고정되어 있음

        //ArrayList<MyObject> objArrayList = (ArrayList) objList;
        //Arrays의 ArrayList와 ArrayList의 필드는 서로 다르고 상속관계가 아님
        //ArrayList가 Arrays 보다 더 넓은 범위의 필드를 가지고 있음
        // + Arrays의 ArrayList 인스턴스를 받아 ArrayList를 생성하는 방식으로 사용 가능

        ArrayList<MyObject> objArrayList = new ArrayList<>(objList);

        for (MyObject obj : objArrayList) {
            System.out.println(obj);
        }

        for (int i = 0; i < objArrayList.size(); i++) {
            System.out.println(objArrayList.get(i));
        }

    }

}
