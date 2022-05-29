package nl.uva.yamp.core.enricher;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.model.DataSet;

import javax.inject.Inject;
import java.util.Set;

@Slf4j
@NoArgsConstructor(onConstructor = @__(@Inject))
public class DisjointMutatantEnricher {

    public Set<DataSet> enrich(Set<DataSet> dataSets) {
        return dataSets;
    }
}
