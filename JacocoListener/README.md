### Jacoco Listener

Setting up Jacoco Listener to collect coverage data for individual test cases:

1. Include `JacocoListener` dependency in project.

```xml
<dependency>
    <groupId>org.uva</groupId>
    <artifactId>JacocoListener</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

2. Add `Jacoco-Maven-Plugin` to project.

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

3. Add `JUnitListener` to `Maven-Surefire-Plugin` in project.

```xml
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
```