package nl.uva.yamp.core.enricher;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Mutation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DisjointMutatantEnricherTest {

    private final DisjointMutatantEnricher sut = new DisjointMutatantEnricher();

    @Test
    void whenEmptyDataSets_expectEmptyResult() {
        Set<DataSet> result = sut.enrich(Set.of());

        assertThat(result).isEmpty();
    }

    @Test
    void whenEmptyMutants_expectUnmodifiedResult() {
        Set<DataSet> input = Set.of(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of())
                .build()
        );

        Set<DataSet> result = sut.enrich(input);

        assertThat(result).isEqualTo(input);
    }

    @Test
    void whenSingleSurvivingMutant_expectUnmodifiedResult() {
        Set<DataSet> input = Set.of(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .killed(false)
                        .build()
                ))
                .build()
        );

        Set<DataSet> result = sut.enrich(input);

        assertThat(result).isEqualTo(input);
    }

    @Test
    void whenSingleKilledMutant_expectMutantMarkedDisjoint() {
        Set<DataSet> input = Set.of(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .killed(true)
                        .build()
                ))
                .build()
        );

        Set<DataSet> result = sut.enrich(input);

        assertThat(result).containsExactlyInAnyOrder(CoreTestData.dataSetBuilder()
            .mutations(Set.of(
                CoreTestData.mutationBuilder()
                    .killed(true)
                    .disjoint(true)
                    .build()
            ))
            .build());
    }

    @Test
    void whenDuplicateKilledMutant_expectAtLeastOneMarkedDisjoint() {
        Set<DataSet> input = Set.of(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(true)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(true)
                        .build()
                ))
                .build()
        );

        Set<DataSet> result = sut.enrich(input);

        assertThat(result)
            .flatExtracting(DataSet::getMutations)
            .extracting(Mutation::getDisjoint)
            .contains(true);
    }

    @Test
    void whenTwoDataSetsWithIdenticalMutant_expectBothMarkedDisjoint() {
        Set<DataSet> input = Set.of(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .killed(true)
                        .build()
                ))
                .build(),
            CoreTestData.dataSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .methodName("method-name")
                    .build())
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .killed(false)
                        .build()
                ))
                .build()
        );

        Set<DataSet> result = sut.enrich(input);

        assertThat(result).containsExactlyInAnyOrder(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .killed(true)
                        .disjoint(true)
                        .build()
                ))
                .build(),
            CoreTestData.dataSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .methodName("method-name")
                    .build())
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .killed(false)
                        .disjoint(true)
                        .build()
                ))
                .build()
        );
    }

    @Test
    void whenTwoDataSets_andOneWithOneWithoutMutant_expectOneMutantMarkedDisjoint() {
        Set<DataSet> input = Set.of(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .killed(true)
                        .build()
                ))
                .build(),
            CoreTestData.dataSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .methodName("method-name")
                    .build())
                .mutations(Set.of())
                .build()
        );

        Set<DataSet> result = sut.enrich(input);

        assertThat(result).containsExactlyInAnyOrder(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .killed(true)
                        .disjoint(true)
                        .build()
                ))
                .build(),
            CoreTestData.dataSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .methodName("method-name")
                    .build())
                .mutations(Set.of())
                .build()
        );
    }

    @Test
    void whenSubsumedMutant_expectSubsumingMutantMarkedDisjoint() {
        Set<DataSet> input = Set.of(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(true)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(false)
                        .build()
                ))
                .build(),
            CoreTestData.dataSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .methodName("method-name")
                    .build())
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(true)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(true)
                        .build()
                ))
                .build()
        );

        Set<DataSet> result = sut.enrich(input);

        assertThat(result).containsExactlyInAnyOrder(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(true)
                        .disjoint(false)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(false)
                        .disjoint(true)
                        .build()
                ))
                .build(),
            CoreTestData.dataSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .methodName("method-name")
                    .build())
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(false)
                        .disjoint(false)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(true)
                        .disjoint(true)
                        .build()
                ))
                .build()
        );
    }

    @Test
    void whenSubsumedMutant_andNonMatchingMutants_expectSubsumingMutantMarkedDisjoint() {
        Set<DataSet> input = Set.of(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(true)
                        .build()
                ))
                .build(),
            CoreTestData.dataSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .methodName("method-name")
                    .build())
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(true)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(true)
                        .build()
                ))
                .build()
        );

        Set<DataSet> result = sut.enrich(input);

        assertThat(result).containsExactlyInAnyOrder(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(true)
                        .disjoint(false)
                        .build()
                ))
                .build(),
            CoreTestData.dataSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .methodName("method-name")
                    .build())
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(false)
                        .disjoint(false)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(true)
                        .disjoint(true)
                        .build()
                ))
                .build()
        );
    }

    @Test
    void whenTwoNonSubsumingMutants_expectBothMarkedDisjoint() {
        Set<DataSet> input = Set.of(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(true)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(false)
                        .build()
                ))
                .build(),
            CoreTestData.dataSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .methodName("method-name")
                    .build())
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(false)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(true)
                        .build()
                ))
                .build()
        );

        Set<DataSet> result = sut.enrich(input);

        assertThat(result).containsExactlyInAnyOrder(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(true)
                        .disjoint(true)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(false)
                        .disjoint(true)
                        .build()
                ))
                .build(),
            CoreTestData.dataSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .methodName("method-name")
                    .build())
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(false)
                        .disjoint(true)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(true)
                        .disjoint(true)
                        .build()
                ))
                .build()
        );
    }

    @Test
    void whenThreeIdenticalMutants_expectAtLeastOneMarkedDisjoint() {
        Set<DataSet> input = Set.of(
            CoreTestData.dataSetBuilder()
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(true)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(true)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(3)
                        .killed(true)
                        .build()
                ))
                .build(),
            CoreTestData.dataSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .methodName("method-name")
                    .build())
                .mutations(Set.of(
                    CoreTestData.mutationBuilder()
                        .lineNumber(1)
                        .killed(false)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(2)
                        .killed(false)
                        .build(),
                    CoreTestData.mutationBuilder()
                        .lineNumber(3)
                        .killed(false)
                        .build()
                ))
                .build()
        );

        Set<DataSet> result = sut.enrich(input);

        assertThat(result)
            .flatExtracting(DataSet::getMutations)
            .extracting(Mutation::getDisjoint)
            .contains(true);
    }
}