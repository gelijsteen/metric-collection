package nl.uva.yamp.core.collector;

import nl.uva.yamp.core.model.Coverage;

public interface CallGraphCollector {

    Coverage collect(Coverage coverage);
}
