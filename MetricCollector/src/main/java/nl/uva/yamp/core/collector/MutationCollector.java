package nl.uva.yamp.core.collector;

import nl.uva.yamp.core.model.DataSet;

public interface MutationCollector {

    DataSet collect(DataSet dataSet);
}
