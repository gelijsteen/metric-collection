package nl.uva.yamp;

public class Class5 {

    public Class5() {
        this(1);
    }

    public Class5(int i) {
        this(i, 2);
    }

    public Class5(int i, int j) {
        System.out.println(i + j);
    }

    public int call() {
        return call(1);
    }

    public int call(int i) {
        return call(i, 2);
    }

    public int call(int i, int j) {
        return i + j;
    }
}
