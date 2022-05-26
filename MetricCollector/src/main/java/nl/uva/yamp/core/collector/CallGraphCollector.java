package nl.uva.yamp.core.collector;

import nl.uva.yamp.core.model.DataSet;

public interface CallGraphCollector {

    DataSet collect(DataSet dataSet);
}
