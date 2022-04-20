### Jacoco Listener

To set up the Jacoco Listener to collect coverage data for individual test cases, add the profile below to the `pom.xml` file, then run the tests using `mvn clean test -Ptest-analysis`. To automate the modification of `pom.xml` files you can use the `MavenPreparation` module.

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