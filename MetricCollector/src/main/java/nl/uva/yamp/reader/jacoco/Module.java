package nl.uva.yamp.reader.jacoco;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
class Module {

    @NonNull
    private final String targetDirectory;
    @NonNull
    private final String name;
}
