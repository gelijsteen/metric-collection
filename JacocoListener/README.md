### JaCoCo Listener

Minimal Java library used to have JaCoCo collect coverage per test case. Supports the JUnit test framework.

#### Prerequisites

*Environment:* Requires Java version `11.x` and Maven version `3.x`.

*Subject project*: Contains at least one `pom.xml` file (Maven multi-module projects are supported).

#### Initial Setup

Install the JaCoCo Listener to your local Maven repository using the following command in this directory:

```shell
mvn clean install
```

#### Running the Tool

To set up the JaCoCo Listener for a subject project, add the Maven profile below to the `pom.xml` file, then run the tests using `mvn clean test -Ptest-analysis`.

**Note**: To automate the modification of `pom.xml` files you can use the `MavenPreparation` module.

```xml
<profile>
    <id>test-analysis</id>
    <properties>
        <maven.test.failure.ignore>true</maven.test.failure.ignore>
    </properties>
    <build>
        <plugins>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.8</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <properties>
                        <property>
                            <name>listener</name>
                            <value>nl.uva.yamp.JUnitListener</value>
                        </property>
                    </properties>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <dependencies>
        <dependency>
            <groupId>org.uva</groupId>
            <artifactId>JacocoListener</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</profile>
```
