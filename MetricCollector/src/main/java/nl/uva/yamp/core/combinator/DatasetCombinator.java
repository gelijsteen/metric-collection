package nl.uva.yamp.core.combinator;

import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Mutation;

import java.util.Set;

public interface DatasetCombinator {

    Set<CombinedData> combine(Set<Coverage> coverageData, Set<CallGraph> callGraphData, Set<Mutation> mutationData);
}
