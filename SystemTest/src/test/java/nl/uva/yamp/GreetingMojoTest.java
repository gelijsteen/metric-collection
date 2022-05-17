package nl.uva.yamp;

import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GreetingMojoTest {

    private final GreetingMojo sut = new GreetingMojo();

    @Test
    public void test1() {
        boolean result = sut.execute();

        assertThat(result).isTrue();
    }

    @Test
    public void test2() {
        boolean result = sut.execute();

        assertThat(result).isTrue();
    }

    @Test
    @Ignore
    public void test3() {
        assertThat("").isEqualTo("");
    }
}
