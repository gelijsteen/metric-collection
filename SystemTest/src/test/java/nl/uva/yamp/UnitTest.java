package nl.uva.yamp;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnitTest {

    private final Class1 sut = new Class1();

    @Test
    public void test1() {
        int result = sut.call1();

        assertThat(result).isEqualTo(1);
        assertUtil(result);
    }

    @Test
    public void test2() {
        int result = sut.call2();

        assertThat(result).isEqualTo(1);
    }

    private void assertUtil(int i) {
        assertThat(i).isNotZero();
    }
}
