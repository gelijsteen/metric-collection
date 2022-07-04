package nl.uva.meco;

import lombok.SneakyThrows;
import org.jacoco.agent.rt.IAgent;
import org.jacoco.agent.rt.RT;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

import java.util.ConcurrentModificationException;
import java.util.concurrent.atomic.AtomicBoolean;

public class JUnitListener extends RunListener {

    private final IAgent agent;
    private final AtomicBoolean isExecuting = new AtomicBoolean();

    public JUnitListener() {
        agent = RT.getAgent();
    }

    public JUnitListener(IAgent agent) {
        this.agent = agent;
    }

    @Override
    @SneakyThrows
    public void testStarted(Description description) {
        if (!isExecuting.compareAndSet(false, true)) {
            throw new ConcurrentModificationException("Parallel test execution not supported.");
        }
        agent.setSessionId("");
        agent.dump(true);
    }

    @Override
    @SneakyThrows
    public void testFinished(Description description) {
        agent.setSessionId(description.getClassName() + "#" + description.getMethodName());
        agent.dump(true);
        isExecuting.set(false);
    }
}
