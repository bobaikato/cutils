### H1R4 Common

A package of utilities classes intended to enhance and simplify Java development.

### Where can I get the latest release? 
You can download source binary 
```
wget --content-disposition https://packagecloud.io/Honerfor/common-utilities/packages/java/org.h1r4/org-hr4-common-utilities-1.0-SNAPSHOT.jar/artifacts/org-hr4-common-utilities-1.0-20190811.123243-1.jar/download
```
Or, you can pull it from the central repositories:
 - #### Maven

Firstly, add this to the top level of your pom.xml
```xml
<repositories>
  <repository>
    <id>Honerfor-common-utilities</id>
    <url>https://packagecloud.io/Honerfor/common-utilities/maven2</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```

```xml
<dependency>
  <groupId>org.h1r4</groupId>
  <artifactId>org-hr4-common-utilities</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

- #### Gradle
Firstly, add this entry anywhere in your `build.gradle` file
```
repositories {
    maven {
        url "https://packagecloud.io/Honerfor/common-utilities/maven2"
    }
}
```
```
compile 'org.h1r4:org-hr4-common-utilities:1.0-SNAPSHOT'
```

- #### Grape
Firstly, create a `~/.groovy/grapeConfig.xml` file and add the code below.
```xml
<ivysettings>
  <settings defaultResolver="downloadGrapes"/>
  <resolvers>
    <chain name="downloadGrapes" returnFirst="true">
      <filesystem name="cachedGrapes">
        <ivy pattern="${user.home}/.groovy/grapes/[organisation]/[module]/ivy-[revision].xml"/>
        <artifact pattern="${user.home}/.groovy/grapes/[organisation]/[module]/[type]s/[artifact]-[revision](-[classifier]).[ext]"/>
      </filesystem>
      <ibiblio name="localm2" root="file:${user.home}/.m2/repository/" checkmodified="true" changingPattern=".*" changingMatcher="regexp" m2compatible="true"/>
      <!-- todo add 'endorsed groovy extensions' resolver here -->
      <ibiblio name="Honerfor-common-utilities" root="https://packagecloud.io/Honerfor/common-utilities/maven2" m2compatible="true"/>
      <ibiblio name="ibiblio" m2compatible="true"/>
    </chain>
  </resolvers>
</ivysettings>
```
```
@Grapes(
@Grab(group='org.h1r4', module='org-hr4-common-utilities', version='1.0-SNAPSHOT')
)
```

- #### Ivy
Firstly, Create an `ivysettings.xml` file and added the code below
```xml
<ivysettings>
    <settings defaultResolver="chain"/>
    <resolvers>
        <chain name="chain">
            <ibiblio name="central" m2compatible="true"/>
            <ibiblio name="Honerfor-common-utilities" m2compatible="true" root="https://packagecloud.io/Honerfor/common-utilities/maven2"/>
        </chain>
    </resolvers>
</ivysettings>
```
```xml
<dependency org="org.h1r4" name="org-hr4-common-utilities" rev="1.0-SNAPSHOT" />
```

### Contributing
We accept Pull Requests via GitHub. A public Slack Channel will soon be made available for communications.
But, in the mean time, there are some guidelines which will make applying PRs easier for us:

1. No tabs! Please use spaces for indentation.
2. Respect the code style.
3. Create minimal diffs.
4. If it will help, disable on save actions like reformat source code or organize imports. **If you feel the source code should be reformatted create a separate PR for this change**.
5. Please, provide **JUnit tests** for your changes and make sure your changes don't break any existing tests by running `mvn clean test`.

### License

This code is under the [Apache Licence v2](https://github.com/Honerfor/Common/blob/master/LICENSE).
