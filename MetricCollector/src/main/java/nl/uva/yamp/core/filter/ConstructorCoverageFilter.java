package nl.uva.yamp.core.filter;

import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;

import java.util.Set;
import java.util.stream.Collectors;

public class ConstructorCoverageFilter implements CoverageFilter {

    @Override
    public Coverage apply(Coverage coverage) {
        return Coverage.builder()
            .testMethod(coverage.getTestMethod())
            .coveredMethods(getCoveredMethods(coverage))
            .build();
    }

    private Set<Method> getCoveredMethods(Coverage coverage) {
        return coverage.getCoveredMethods()
            .stream()
            .filter(this::isNotConstructor)
            .collect(Collectors.toSet());
    }

    private boolean isNotConstructor(Method method) {
        return !method.getMethodName().equals("<init>");
    }
}
