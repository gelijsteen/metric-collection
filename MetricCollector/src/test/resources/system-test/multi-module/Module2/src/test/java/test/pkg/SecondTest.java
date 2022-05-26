package test.pkg;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SecondTest {

    private final Second sut = new Second();

    @Test
    public void test1() {
        int result = sut.call(1);

        assertThat(result).isEqualTo(1);
    }
}
