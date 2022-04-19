package nl.uva.yamp;

import lombok.SneakyThrows;
import org.jacoco.agent.rt.IAgent;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class JUnitListenerTest {

    @Mock
    private IAgent agent;
    @InjectMocks
    private JUnitListener sut;
    private JUnitCore jUnitCore;

    @Before
    public void setUp() {
        jUnitCore = new JUnitCore();
        jUnitCore.addListener(sut);
    }

    @Test
    @SneakyThrows
    public void whenTestSuccess_expectJacocoAgentUpdated() {
        jUnitCore.run(TestSuccess.class);

        InOrder inOrder = inOrder(agent);
        inOrder.verify(agent).setSessionId("");
        inOrder.verify(agent).dump(true);
        inOrder.verify(agent).setSessionId("nl.uva.yamp.JUnitListenerTest$TestSuccess#test");
        inOrder.verify(agent).dump(true);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @SneakyThrows
    public void whenTestMultipleTests_expectJacocoAgentUpdated() {
        jUnitCore.run(TestMultipleTests.class);

        verify(agent, times(2)).setSessionId("");
        verify(agent, times(4)).dump(true);
        verify(agent).setSessionId("nl.uva.yamp.JUnitListenerTest$TestMultipleTests#test1");
        verify(agent).setSessionId("nl.uva.yamp.JUnitListenerTest$TestMultipleTests#test2");
        verifyNoMoreInteractions(agent);
    }

    @Test
    @SneakyThrows
    public void whenTestFailure_expectJacocoAgentUpdated() {
        jUnitCore.run(TestFailure.class);

        InOrder inOrder = inOrder(agent);
        inOrder.verify(agent).setSessionId("");
        inOrder.verify(agent).dump(true);
        inOrder.verify(agent).setSessionId("nl.uva.yamp.JUnitListenerTest$TestFailure#test");
        inOrder.verify(agent).dump(true);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void whenTestIgnored_expectNoJacocoAgentInteraction() {
        jUnitCore.run(TestIgnored.class);

        verifyNoInteractions(agent);
    }

    @Test
    @SneakyThrows
    public void whenTestException_expectJacocoAgentUpdated() {
        jUnitCore.run(TestException.class);

        InOrder inOrder = inOrder(agent);
        inOrder.verify(agent).setSessionId("");
        inOrder.verify(agent).dump(true);
        inOrder.verify(agent).setSessionId("nl.uva.yamp.JUnitListenerTest$TestException#test");
        inOrder.verify(agent).dump(true);
        inOrder.verifyNoMoreInteractions();
    }

    public static class TestSuccess {
        @Test
        public void test() {
            assertThat(true).isTrue();
        }
    }

    public static class TestMultipleTests {
        @Test
        public void test1() {
            assertThat(true).isTrue();
        }

        @Test
        public void test2() {
            assertThat(true).isTrue();
        }
    }

    public static class TestFailure {
        @Test
        public void test() {
            assertThat(false).isTrue();
        }
    }

    public static class TestIgnored {
        @Test
        @Ignore
        public void test() {
            assertThat(true).isTrue();
        }
    }

    public static class TestException {
        @Test
        public void test() {
            assertThat(true).isTrue();
            throw new IllegalArgumentException();
        }
    }
}