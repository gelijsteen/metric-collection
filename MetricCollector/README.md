### MetricCollector

Command-line tool to collect a set of metrics for the test cases within a given Maven project.

#### Prerequisites

*Environment*: Requires Java version `11.x` and Maven version `3.x`.

*Subject project*: Contains at least one `pom.xml` file (Maven multi-module projects are supported) and a `jacoco.exec` file generated using `JacocoListener` module. 

#### Initial Setup

To obtain the required dependencies and have the dependency injection framework (Dagger) generate its code, run the following command in this directory:

```shell
mvn clean test-compile
```

#### Running the tool

Requires a single argument: the path to the root directory of the subject project.

```shell
java -jar MetricCollector-exec.jar /path/to/project
```

Optionally accepts a 2nd argument: `-D` to skip mutation tests.

```shell
java -jar MetricCollector-exec.jar /path/to/project -D
```

#### Running the test suite

Running the JUnit tests with Maven (e.g. `mvn clean test`) will not work, due to the fact that the mutation score collector spawns additional Maven processes. To run the JUnit tests, directly execute them using JUnit (or use your IDE).

#### Configuration options

The application requires an `application.yml` file containing the application configuration. The following three configuration options are available:

| Key                          | Default Value | Description                                                                                                                                                                                                                                      |
|------------------------------|---------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| parallelExecution.numThreads | 2             | Number of threads to use for coverage collection and mutation testing. Use `1` to disable parallel execution. Due to the additional processes spawned by mutation testing, we advice to use `#CPU threads available / 3` for this configuration. |
| disjointMutants.repetitions  | 8             | The number of repetitions in the disjoint mutant calculation.                                                                                                                                                                                    |
| writer.csv.outputFile        | output.csv    | Location of the file to write the output to, in CSV format.                                                                                                                                                                                      |

**Note**: The current implementation requires the `application.yml` and `pitest-template.xml` files to be present in the working directory to execute properly. These files can be found in the `src/main/resources/` directory.

#### Collected Metrics

The following list of metrics are collected by default. Note that the mutation-related metrics will default to `0` when the `-D` command line argument is specified.

| Metric         | Description                                                                                 |
|----------------|---------------------------------------------------------------------------------------------|
| rTDATA         | Number of constructors directly covered by test case                                        |
| IMC            | Number of methods indirectly covered by test case                                           |
| ICC            | Number of classes indirectly covered by test case                                           |
| IPC            | Number of packages indirectly covered by test case                                          |
| DPHC           | Similar to _IPC_, excluding child packages                                                  |
| rDirectness    | Ratio between the LOC of directly covered methods and the LOC of indirectly covered methods |
| tLOC           | Number of test lines-of-code covered by test case                                           |
| aLOC           | Number of application lines-of-code covered by test case                                    |
| DEV            | Classification of test according to developer(s)                                            |
 | NKM            | Number of killed mutants                                                                    |
| NSM            | Number of surviving mutants                                                                 |
| dNKM           | Similar to _NKM_, excluding equivalent and subsumed mutants                                 |
| dNSM           | Similar to _NSM_, excluding equivalent and subsumed mutants                                 |
| MutationScore  | Ratio between the number of mutants killed and the total number of mutants                  |
| dMutationScore | Similar to _mutationScore_, excluding equivalent and subsumed mutants                       |
