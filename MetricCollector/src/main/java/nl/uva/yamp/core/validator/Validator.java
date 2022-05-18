package nl.uva.yamp.core.validator;

import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.Coverage;

import java.util.Set;

public interface Validator {

    void validate(Set<Coverage> coverages, Set<CallGraph> callGraphs);
}
