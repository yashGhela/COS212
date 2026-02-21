@SuppressWarnings("unchecked")
public class SortedArray<T extends Comparable<T>> {
    public T[] arr;

    public SortedArray() {
        arr = (T[]) new Comparable[0];
    }

    @Override
    public String toString() {
        String res = "[";
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) {
                res += ",";
            }
            res += arr[i];
        }
        return res + "]";
    }

    public void addElement(T el) {
        T[] newArr = (T[]) new Comparable[arr.length + 1];
        boolean inserted = false;
        for (int i = 0, j = 0; i < arr.length; i++) {
            if (!inserted && arr[i].compareTo(el) > 0) {
                inserted = true;
                newArr[j++] = el;
            }
            newArr[j++] = arr[i];
        }
        if (!inserted) {
            newArr[newArr.length - 1] = el;
        }
        arr = newArr;
    }
}
