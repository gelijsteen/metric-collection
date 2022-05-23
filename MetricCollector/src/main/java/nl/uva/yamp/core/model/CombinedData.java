package nl.uva.yamp.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class CombinedData {

    @NonNull
    private final TestCase testCase;
    @NonNull
    private final Set<CombinedConstructor> constructors;
    @NonNull
    private final Set<CombinedMethod> methods;
    @NonNull
    private final Integer mutationScore;

    public Set<CombinedMethod> getIndirectMethods() {
        return Stream.concat(
            constructors.stream().flatMap(this::collectMethods),
            methods.stream().flatMap(this::collectMethods)
        ).collect(Collectors.toSet());
    }

    private Stream<CombinedMethod> collectMethods(CombinedConstructor constructorNode) {
        return Stream.concat(
            constructorNode.getConstructors().stream().flatMap(this::collectMethods),
            constructorNode.getMethods().stream().flatMap(this::collectMethods)
        );
    }

    private Stream<CombinedMethod> collectMethods(CombinedMethod methodNode) {
        return Stream.concat(
            Stream.of(methodNode),
            Stream.concat(
                methodNode.getConstructors().stream().flatMap(this::collectMethods),
                methodNode.getMethods().stream().flatMap(this::collectMethods)
            )
        );
    }
}
