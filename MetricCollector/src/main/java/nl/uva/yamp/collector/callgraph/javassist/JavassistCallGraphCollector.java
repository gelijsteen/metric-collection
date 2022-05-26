package nl.uva.yamp.collector.callgraph.javassist;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.collector.CallGraphCollector;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class JavassistCallGraphCollector implements CallGraphCollector {

    private final Path projectDirectory;

    @Override
    @SneakyThrows
    public Coverage collect(Coverage coverage) {
        ClassPool classPool = new ClassPool();
        classPool.insertClassPath(projectDirectory.resolve("target").resolve("classes").toString());
        classPool.insertClassPath(projectDirectory.resolve("target").resolve("test-classes").toString());

        Set<CtBehavior> behaviors = getBehaviors(coverage, classPool);

        Set<Constructor> directConstructors = getDirectConstructors(behaviors);
        Set<Method> directMethods = getDirectMethods(behaviors);

        Set<Constructor> constructors = coverage.getConstructors().stream()
            .map(constructor -> directConstructors.contains(constructor) ? constructor.withDirect(true) : constructor)
            .collect(Collectors.toSet());
        Set<Method> methods = coverage.getMethods().stream()
            .map(method -> directMethods.contains(method) ? method.withDirect(true) : method)
            .collect(Collectors.toSet());

        return Coverage.builder()
            .testCase(coverage.getTestCase())
            .mutationScore(coverage.getMutationScore())
            .constructors(constructors)
            .methods(methods)
            .testConstructors(coverage.getTestConstructors())
            .testMethods(coverage.getTestMethods())
            .build();
    }

    @NotNull
    private Set<CtBehavior> getBehaviors(Coverage coverage, ClassPool classPool) {
        Set<CtBehavior> constructorBehaviors = coverage.getTestConstructors().stream()
            .map(coverageConstructor -> getBehaviors(coverage, classPool, coverageConstructor))
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
        Set<CtBehavior> constructorMethods = coverage.getTestMethods().stream()
            .map(coverageMethod -> getBehaviors(coverage, classPool, coverageMethod))
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
        return Stream.concat(constructorBehaviors.stream(), constructorMethods.stream()).collect(Collectors.toSet());
    }

    @SneakyThrows
    private Set<CtBehavior> getBehaviors(Coverage coverage, ClassPool classPool, Constructor constructor) {
        CtClass ctClass = classPool.get(constructor.getFullyQualifiedClassName());
        return Arrays.stream(ctClass.getDeclaredConstructors())
            .filter(ctConstructor -> isCoveredConstructor(coverage, ctConstructor))
            .map(this::buildCallGraph)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

    private boolean isCoveredConstructor(Coverage coverage, CtConstructor ctConstructor) {
        return coverage.getTestConstructors().stream()
            .anyMatch(constructor -> isSignatureMatch(ctConstructor, constructor));
    }

    private boolean isSignatureMatch(CtConstructor ctConstructor, Constructor constructor) {
        String signature = ctConstructor.getDeclaringClass().getName() + " " + ctConstructor.getMethodInfo().getDescriptor();
        return constructor.getSignature().equals(signature);
    }

    @SneakyThrows
    private Set<CtBehavior> getBehaviors(Coverage coverage, ClassPool classPool, Method method) {
        CtClass ctClass = classPool.get(method.getFullyQualifiedClassName());
        return Arrays.stream(ctClass.getDeclaredMethods())
            .filter(ctMethod -> isCoveredMethod(coverage, ctMethod))
            .map(this::buildCallGraph)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

    private boolean isCoveredMethod(Coverage coverage, CtMethod ctMethod) {
        return coverage.getTestMethods().stream()
            .anyMatch(method -> isSignatureMatch(ctMethod, method));
    }

    private boolean isSignatureMatch(CtMethod ctMethod, Method method) {
        String signature = ctMethod.getDeclaringClass().getName() + "." + ctMethod.getMethodInfo().getName() + " " + ctMethod.getMethodInfo().getDescriptor();
        return method.getSignature().equals(signature);
    }

    private Set<Constructor> getDirectConstructors(Set<CtBehavior> behaviors) {
        return behaviors.stream()
            .filter(behavior -> behavior.getMethodInfo().isConstructor())
            .map(behavior -> Constructor.builder()
                .packageName(behavior.getDeclaringClass().getPackageName())
                .className(behavior.getDeclaringClass().getSimpleName())
                .descriptor(behavior.getMethodInfo().getDescriptor())
                .loc(0)
                .direct(false)
                .build())
            .collect(Collectors.toSet());
    }

    private Set<Method> getDirectMethods(Set<CtBehavior> behaviors) {
        return behaviors.stream()
            .filter(behavior -> behavior.getMethodInfo().isMethod())
            .map(behavior -> Method.builder()
                .packageName(behavior.getDeclaringClass().getPackageName())
                .className(behavior.getDeclaringClass().getSimpleName())
                .methodName(behavior.getName())
                .descriptor(behavior.getMethodInfo().getDescriptor())
                .loc(0)
                .direct(false)
                .build())
            .collect(Collectors.toSet());
    }

    @SneakyThrows
    private Set<CtBehavior> buildCallGraph(CtBehavior behavior) {
        Set<CtBehavior> behaviors = new HashSet<>();

        behavior.instrument(new ExprEditor() {
            @Override
            public void edit(MethodCall methodCall) {
                try {
                    behaviors.add(methodCall.getMethod());
                } catch (NotFoundException e) {
                    // Caught silently.
                }
            }

            @Override
            public void edit(NewExpr newExpr) {
                try {
                    behaviors.add(newExpr.getConstructor());
                } catch (NotFoundException e) {
                    // Caught silently.
                }
            }

            @Override
            public void edit(ConstructorCall constructorCall) {
                try {
                    behaviors.add(constructorCall.getConstructor());
                } catch (NotFoundException e) {
                    // Caught silently.
                }
            }
        });

        return behaviors;
    }
}
