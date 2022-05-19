package nl.uva.yamp.collector.coverage.jacoco;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.nio.file.Path;

@Getter
@Builder
@ToString
@EqualsAndHashCode
class TargetDirectory {

    @NonNull
    private final Path path;
    @NonNull
    private final String moduleName;
}
