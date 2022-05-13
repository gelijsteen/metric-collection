package nl.uva.yamp.core.filter;

import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;

public class MethodCommaCoverageFilter implements CoverageFilter {

    @Override
    public Coverage apply(Coverage coverage) {
        return Coverage.builder()
            .testMethod(Method.builder()
                .packageName(coverage.getTestMethod().getPackageName())
                .className(coverage.getTestMethod().getClassName())
                .methodName(coverage.getTestMethod().getMethodName().replace(',', ';').replace('\n', ';'))
                .build())
            .coveredMethods(coverage.getCoveredMethods())
            .build();
    }
}
