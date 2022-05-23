package nl.uva.yamp.collector.callgraph.javassist;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.collector.CallGraphCollector;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.CoverageConstructor;
import nl.uva.yamp.core.model.CoverageMethod;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class JavassistCallGraphCollector implements CallGraphCollector {

    private final Path projectDirectory;
    private final ResultMapper resultMapper;

    @Override
    @SneakyThrows
    public CallGraph collect(Coverage coverage) {
        ClassPool classPool = new ClassPool();
        classPool.insertClassPath(projectDirectory.resolve("target").resolve("classes").toString());
        classPool.insertClassPath(projectDirectory.resolve("target").resolve("test-classes").toString());

        CtClass testClass = classPool.get(coverage.getTestCase().getFullyQualifiedClassName());

        CtBehavior testMethod = Arrays.stream(testClass.getDeclaredMethods())
            .filter(ctMethod -> ctMethod.getMethodInfo().getName().equals(coverage.getTestCase().getMethodName()))
            .findFirst()
            .orElseThrow();

        CallGraphNode root = CallGraphNode.builder()
            .behavior(testMethod)
            .build();

        buildCallGraph(coverage, root);

        return resultMapper.map(coverage.getTestCase(), root);
    }

    @SneakyThrows
    private void buildCallGraph(Coverage coverage, CallGraphNode caller) {
        caller.getBehavior().instrument(new ExprEditor() {
            @Override
            public void edit(MethodCall methodCall) {
                try {
                    addCallToCallGraph(coverage, caller, methodCall.getMethod());
                } catch (NotFoundException e) {
                    log.debug("Method not found.", e);
                }
            }

            @Override
            public void edit(NewExpr newExpr) {
                try {
                    addCallToCallGraph(coverage, caller, newExpr.getConstructor());
                } catch (NotFoundException e) {
                    log.debug("Constructor not found.", e);
                }
            }

            @Override
            public void edit(ConstructorCall constructorCall) {
                try {
                    addCallToCallGraph(coverage, caller, constructorCall.getConstructor());
                } catch (NotFoundException e) {
                    log.debug("Constructor not found.", e);
                }
            }
        });
    }

    @SneakyThrows
    private void addCallToCallGraph(Coverage coverage, CallGraphNode caller, CtBehavior ctBehavior) {
        CallGraphNode callee = CallGraphNode.builder()
            .parent(caller)
            .behavior(ctBehavior)
            .build();

        // Filter non-test class.
        ClassPool classPool = new ClassPool();
        classPool.insertClassPath(projectDirectory.resolve("target").resolve("classes").toString());
        try {
            classPool.get(callee.getBehavior().getDeclaringClass().getName());
        } catch (NotFoundException e) {
            log.debug("{} is a test class.", ctBehavior.getLongName());
            return;
        }

        // filter non-covered class.
        boolean b;
        if (callee.getBehavior().getMethodInfo().isMethod()) {
            b = coverage.getMethods().stream()
                .map(CoverageMethod::getFullyQualifiedMethodName)
                .anyMatch(name -> name.equals(callee.getFullyQualifiedMethodName()));
        } else if (callee.getBehavior().getMethodInfo().isConstructor()) {
            b = coverage.getConstructors().stream()
                .map(CoverageConstructor::getFullyQualifiedClassName)
                .anyMatch(name -> name.equals(callee.getBehavior().getDeclaringClass().getName()));
        } else {
            b = false;
        }
        if (!b) {
            log.debug("{} not covered.", ctBehavior.getLongName());
            return;
        }

        if (isPresentInHierarchy(caller, callee)) {
            log.debug("{} called recursively.", ctBehavior.getLongName());
        } else {
            caller.getNodes().add(callee);
            buildCallGraph(coverage, callee);
        }
    }

    private boolean isPresentInHierarchy(CallGraphNode caller, CallGraphNode callee) {
        if (caller.equals(callee)) {
            return true;
        }
        if (caller.getParent() != null) {
            return isPresentInHierarchy(caller.getParent(), callee);
        }
        return false;
    }
}
