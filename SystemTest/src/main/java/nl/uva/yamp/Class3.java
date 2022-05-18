package nl.uva.yamp;

import java.util.Optional;

public class Class3 {

    public Class3() {
        if (this.getClass().getName().equals("Fietswiel")) {
            new Class1().call2();
        }
    }

    public int call() {
        if (this.getClass().getName().equals("Fietswiel")) {
            Class4 class4 = new Class4();
            class4.call();
            call();
        }
        return Optional.of(1).orElseGet(() -> new Class2().call());
    }
}
