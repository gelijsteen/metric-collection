package nl.uva.yamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ParameterizedUnitTest {

    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{{0, 1}, {2, 3}});
    }

    private final int i;
    private final int j;

    public ParameterizedUnitTest(int i, int j) {
        this.i = i;
        this.j = j;
    }

    private final Class1 sut = new Class1();

    @Test
    public void test1() {
        int result = sut.call2();

        assertThat(result).isEqualTo(1);
    }
}
