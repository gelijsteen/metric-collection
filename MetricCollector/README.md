### MetricCollector

Command-line tool to collect a set of metrics for the test cases within a given Maven project.

Requires a single argument: the path to the root directory of the project.

```shell
java -jar MetricCollector-exec.jar /path/to/project
```

#### Collected Metrics

| Metric                | Description                                                                                 |
|-----------------------|---------------------------------------------------------------------------------------------|
| rTDATA                | Number of constructors directly covered by test case                                        |
| IMC                   | Number of methods indirectly covered by test case                                           |
| ICC                   | Number of classes indirectly covered by test case                                           |
| IPC                   | Number of packages indirectly covered by test case                                          |
| DPHC                  | Similar to _IPC_, excluding child packages                                                  |
| rDirectness           | Ratio between the LOC of directly covered methods and the LOC of indirectly covered methods |
| tLOC                  | Number of test lines-of-code covered by test case                                           |
| aLOC                  | Number of application lines-of-code covered by test case                                    |
| DEV                   | Classification of test according to developer(s)                                            |
| mutationScore         | Ratio between the number of mutants killed and the total number of mutants                  |
| disjointMutationScore | Similar to _mutationScore_, excluding equivalent and subsumed mutants                       |

**Note**: Currently requires the _application.yml_ and _pitest-template.xml_ files to be present in the directory to execute properly. These files can be found in the following directory:

```shell
src/main/resources/
```