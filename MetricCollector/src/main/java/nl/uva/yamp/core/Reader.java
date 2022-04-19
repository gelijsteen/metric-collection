package nl.uva.yamp.core;

import nl.uva.yamp.core.model.Coverage;

import java.util.Set;

public interface Reader {

    Set<Coverage> read();
}
