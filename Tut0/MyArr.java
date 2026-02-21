@SuppressWarnings("unchecked")
public class MyArr<T> {
    public T[] arr;

    public MyArr() {
        arr = (T[]) new Object[0];
    }

    public void addElement(T el) {
        T[] newArr = (T[]) new Object[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }
        newArr[arr.length] = el;
        arr = newArr;
    }
}
