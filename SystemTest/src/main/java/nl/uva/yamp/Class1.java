package nl.uva.yamp;

public class Class1 {

    private final Class2 class2 = new Class2();

    public int call1() {
        return class2.call();
    }

    public int call2() {
        Class3 class3 = new Class3();
        return class3.call();
    }
}
