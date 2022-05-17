package nl.uva.yamp.core.mutation;

import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Mutation;

public interface MutationReader {

    Mutation read(Coverage coverage);
}
