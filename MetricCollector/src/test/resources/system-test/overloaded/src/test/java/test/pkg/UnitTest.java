package test.pkg;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnitTest {

    private final Unit sut = new Unit();

    @Test
    public void test1() {
        int result = sut.call();

        assertThat(result).isEqualTo(2);
    }
}
