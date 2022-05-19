package nl.uva.yamp.core.collector;

import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Mutation;

public interface MutationCollector {

    Mutation collect(Coverage coverage);
}
