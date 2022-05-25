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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        CallGraphNode constructorRoot = Arrays.stream(testClass.getDeclaredConstructors())
            .map(ctMethod -> CallGraphNode.builder()
                .behavior(ctMethod)
                .build())
            .findFirst()
            .map(callGraphNode -> {
                buildCallGraph(coverage, callGraphNode, callGraphNode.getBehavior());
                return callGraphNode;
            })
            .orElseThrow();

        Set<CallGraphNode> methods = Arrays.stream(testClass.getDeclaredMethods())
            .map(ctMethod -> CallGraphNode.builder()
                .behavior(ctMethod)
                .build())
            .filter(ctMethod -> coverage.getTestMethods().stream()
                .anyMatch(coverageMethod -> coverageMethod.getSignature().equals(ctMethod.getSignature())))
            .map(callGraphNode -> {
                buildCallGraph(coverage, callGraphNode, callGraphNode.getBehavior());
                return callGraphNode;
            })
            .collect(Collectors.toSet());

        return resultMapper.map(coverage.getTestCase(), Stream.concat(Stream.of(constructorRoot), methods.stream()).collect(Collectors.toSet()));
    }

    @SneakyThrows
    private void buildCallGraph(Coverage coverage, CallGraphNode caller, CtBehavior behavior) {
        behavior.instrument(new ExprEditor() {
            @Override
            public void edit(MethodCall methodCall) {
                try {
                    addCallToCallGraph(coverage, caller, methodCall.getMethod());
                } catch (NotFoundException e) {
                    // log.debug("Method not found.", e);
                }
            }

            @Override
            public void edit(NewExpr newExpr) {
                try {
                    addCallToCallGraph(coverage, caller, newExpr.getConstructor());
                } catch (NotFoundException e) {
                    // log.debug("Constructor not found.", e);
                }
            }

            @Override
            public void edit(ConstructorCall constructorCall) {
                try {
                    addCallToCallGraph(coverage, caller, constructorCall.getConstructor());
                } catch (NotFoundException e) {
                    // log.debug("Constructor not found.", e);
                }
            }
        });
    }

    @SneakyThrows
    private void addCallToCallGraph(Coverage coverage, CallGraphNode caller, CtBehavior behavior) {
        CallGraphNode callee = CallGraphNode.builder()
            .parent(caller)
            .behavior(behavior)
            .build();

        boolean t;
        if (callee.getBehavior().getMethodInfo().isMethod()) {
            t = coverage.getTestMethods().stream()
                .map(CoverageMethod::getSignature)
                .anyMatch(name -> name.equals(callee.getSignature()));
        } else if (callee.getBehavior().getMethodInfo().isConstructor()) {
            t = coverage.getTestConstructors().stream()
                .map(CoverageConstructor::getSignature)
                .anyMatch(name -> name.equals(callee.getSignature()));
        } else {
            t = false;
        }
        if (t) {
            log.debug("{} is a test class.", behavior.getLongName());
            buildCallGraph(coverage, caller, callee.getBehavior());
            return;
        }

        // filter non-covered class.
        boolean b;
        if (callee.getBehavior().getMethodInfo().isMethod()) {
            b = coverage.getMethods().stream()
                .map(CoverageMethod::getSignature)
                .anyMatch(name -> name.equals(callee.getSignature()));
        } else if (callee.getBehavior().getMethodInfo().isConstructor()) {
            b = coverage.getConstructors().stream()
                .map(CoverageConstructor::getSignature)
                .anyMatch(name -> name.equals(callee.getSignature()));
        } else {
            b = false;
        }
        if (!b) {
            log.debug("{} not covered.", behavior.getLongName());
            return;
        }

        if (isPresentInHierarchy(caller, callee)) {
            log.debug("{} called recursively.", behavior.getLongName());
        } else {
            caller.getNodes().add(callee);
            buildCallGraph(coverage, callee, callee.getBehavior());
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
