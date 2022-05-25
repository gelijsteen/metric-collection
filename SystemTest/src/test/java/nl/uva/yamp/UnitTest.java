package nl.uva.yamp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnitTest {

    private final Class1 sut = new Class1();

    @Before
    public void setUp() {
        new Class3();
    }

    @After
    public void tearDown() {
        new Class4();
    }

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
