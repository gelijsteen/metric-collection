### Maven Preparation

Utility command-line tool to add or replace a Maven profile in existing `pom.xml` file.

If a profile with given identifier is already present, it is replaced.

Requires two arguments; (1) the profile XML file and (2) the `pom.xml` to modify.

```shell
java -jar MavenPreparation-exec.jar /path/to/profile.xml /path/to/pom.xml
```

**Note**: The `<profile>` element in the profile XML file requires the Maven namespace, e.g: `<profile xmlns="http://maven.apache.org/POM/4.0.0">`.
