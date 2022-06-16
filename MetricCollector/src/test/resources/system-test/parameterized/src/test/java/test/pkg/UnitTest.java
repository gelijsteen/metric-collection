package test.pkg;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class UnitTest {

    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{{0, 1}, {2, 3}});
    }

    private final int i;
    private final int j;

    public UnitTest(int i, int j) {
        this.i = i;
        this.j = j;
    }

    private final Unit sut = new Unit();

    @Test
    public void test1() {
        int result = sut.call(1);

        assertThat(result).isEqualTo(2);
    }
}
