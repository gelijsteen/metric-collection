package nl.uva.yamp;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OverloadUnitTest {

    private final Class5 sut = new Class5();

    @Test
    public void test1() {
        new Class5();

        int result = sut.call();

        assertThat(result).isEqualTo(3);
    }

    @Test
    public void test2() {
        new Class5(1);

        int result = sut.call(1);

        new TestUtil().callClass1(new Class1());

        assertThat(result).isEqualTo(3);
    }

    @Test
    public void test3() {
        new Class5(1, 2);

        int result = sut.call(1, 2);

        assertThat(result).isEqualTo(3);
    }
}
