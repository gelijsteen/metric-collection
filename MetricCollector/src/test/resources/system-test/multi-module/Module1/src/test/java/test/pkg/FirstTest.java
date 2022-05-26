package test.pkg;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FirstTest {

    private final First sut = new First();

    @Test
    public void test1() {
        int result = sut.call(1);

        assertThat(result).isEqualTo(1);
    }
}
