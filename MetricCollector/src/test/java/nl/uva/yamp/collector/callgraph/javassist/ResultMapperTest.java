package nl.uva.yamp.collector.callgraph.javassist;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import nl.uva.yamp.collector.CollectorTestData;
import nl.uva.yamp.core.model.CallGraph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ResultMapperTest {

    private final ResultMapper sut = new ResultMapper();

    @Test
    void happyFlow() {
        CallGraphNode build = CallGraphNode.builder()
            .behavior(FakeCtBehavior.create("test.pkg.UnitTest", "test1"))
            .build();
        build.getNodes().add(CallGraphNode.builder()
            .behavior(FakeCtBehavior.create("test.pkg.UnitTest", "<init>"))
            .build());
        build.getNodes().add(CallGraphNode.builder()
            .behavior(FakeCtBehavior.create("test.pkg.UnitTest", "test1"))
            .build());

        CallGraph result = sut.map(CollectorTestData.testCaseBuilder().build(), build);

        assertThat(result).isEqualTo(CollectorTestData.callGraphBuilder().build());
    }

    private static class FakeCtBehavior extends CtBehavior {

        private FakeCtBehavior(CtClass clazz, MethodInfo minfo) {
            super(clazz, minfo);
        }

        private static FakeCtBehavior create(String fullyQualifiedClassName, String methodName) {
            return new FakeCtBehavior(new FakeCtClass(fullyQualifiedClassName), new FakeMethodInfo(methodName));
        }

        @Override
        public String getLongName() {
            return methodInfo.getName();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String getName() {
            return methodInfo.getName();
        }
    }

    private static class FakeCtClass extends CtClass {

        private FakeCtClass(String name) {
            super(name);
        }

        @Override
        public boolean isFrozen() {
            return false;
        }
    }

    private static class FakeMethodInfo extends MethodInfo {

        private FakeMethodInfo(String methodname) {
            super(new ConstPool(""), methodname, "");
        }
    }
}