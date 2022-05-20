package test.pkg;

public class Direct {

    public int call(int x) {
        Indirect indirect = new Indirect();
        return indirect.call(x);
    }
}
