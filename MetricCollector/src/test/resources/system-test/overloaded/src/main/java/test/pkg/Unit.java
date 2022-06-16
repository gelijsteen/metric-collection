package test.pkg;

public class Unit {

    private final int x;
    private final int y;

    public Unit() {
        this(1);
    }

    public Unit(int x) {
        this(x, 2);
    }

    public Unit(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int call() {
        return call(1);
    }

    public int call(int x) {
        return call(x, 2);
    }

    public int call(int x, int y) {
        return x * y;
    }
}
