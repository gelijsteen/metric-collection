package test.pkg;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnitTest {

    private final Direct sut = new Direct();

    @Test
    public void test1() {
        int result = sut.call(1);

        assertThat(result).isZero();
    }
}
