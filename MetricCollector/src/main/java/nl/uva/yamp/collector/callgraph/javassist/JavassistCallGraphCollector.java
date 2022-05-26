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
import nl.uva.yamp.core.model.DataSet;
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
    public DataSet collect(DataSet dataSet) {
        ClassPool classPool = new ClassPool();
        classPool.insertClassPath(projectDirectory.resolve("target").resolve("classes").toString());
        classPool.insertClassPath(projectDirectory.resolve("target").resolve("test-classes").toString());

        Set<CtBehavior> behaviors = getBehaviors(dataSet, classPool);

        Set<Constructor> directConstructors = getDirectConstructors(behaviors);
        Set<Method> directMethods = getDirectMethods(behaviors);

        Set<Constructor> constructors = getConstructors(dataSet, directConstructors);
        Set<Method> methods = getMethods(dataSet, directMethods);

        return DataSet.builder()
            .testCase(dataSet.getTestCase())
            .mutationScore(dataSet.getMutationScore())
            .constructors(constructors)
            .methods(methods)
            .testConstructors(dataSet.getTestConstructors())
            .testMethods(dataSet.getTestMethods())
            .build();
    }

    @NotNull
    private Set<CtBehavior> getBehaviors(DataSet dataSet, ClassPool classPool) {
        Set<CtBehavior> constructorBehaviors = dataSet.getTestConstructors().stream()
            .map(constructor -> getBehaviors(dataSet, classPool, constructor))
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
        Set<CtBehavior> constructorMethods = dataSet.getTestMethods().stream()
            .map(method -> getBehaviors(dataSet, classPool, method))
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
        return Stream.concat(constructorBehaviors.stream(), constructorMethods.stream()).collect(Collectors.toSet());
    }

    @SneakyThrows
    private Set<CtBehavior> getBehaviors(DataSet dataSet, ClassPool classPool, Constructor constructor) {
        CtClass ctClass = classPool.get(constructor.getFullyQualifiedClassName());
        return Arrays.stream(ctClass.getDeclaredConstructors())
            .filter(ctConstructor -> isCoveredConstructor(dataSet, ctConstructor))
            .map(this::buildCallGraph)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

    private boolean isCoveredConstructor(DataSet dataSet, CtConstructor ctConstructor) {
        return dataSet.getTestConstructors().stream()
            .anyMatch(constructor -> isSignatureMatch(ctConstructor, constructor));
    }

    private boolean isSignatureMatch(CtConstructor ctConstructor, Constructor constructor) {
        String signature = ctConstructor.getDeclaringClass().getName() + " " + ctConstructor.getMethodInfo().getDescriptor();
        return constructor.getSignature().equals(signature);
    }

    @SneakyThrows
    private Set<CtBehavior> getBehaviors(DataSet dataSet, ClassPool classPool, Method method) {
        CtClass ctClass = classPool.get(method.getFullyQualifiedClassName());
        return Arrays.stream(ctClass.getDeclaredMethods())
            .filter(ctMethod -> isCoveredMethod(dataSet, ctMethod))
            .map(this::buildCallGraph)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

    private boolean isCoveredMethod(DataSet dataSet, CtMethod ctMethod) {
        return dataSet.getTestMethods().stream()
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

    private Set<Constructor> getConstructors(DataSet dataSet, Set<Constructor> directConstructors) {
        return dataSet.getConstructors().stream()
            .map(constructor -> directConstructors.stream()
                .map(Constructor::getSignature)
                .anyMatch(signature -> signature.equals(constructor.getSignature())) ? constructor.withDirect(true) : constructor)
            .collect(Collectors.toSet());
    }

    private Set<Method> getMethods(DataSet dataSet, Set<Method> directMethods) {
        return dataSet.getMethods().stream()
            .map(method -> directMethods.stream()
                .map(Method::getSignature)
                .anyMatch(signature -> signature.equals(method.getSignature())) ? method.withDirect(true) : method)
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
