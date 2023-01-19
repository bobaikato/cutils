## CUTILS ART

[![License](https://img.shields.io/github/license/honerfor/cutils)](#License)
[![Build & Test](https://github.com/bobaikato/cutils/actions/workflows/maven.yml/badge.svg)](https://github.com/bobaikato/cutils/actions/workflows/maven.yml)
[![codebeat badge](https://codebeat.co/badges/0938a891-4a79-4966-b914-e07720cd1771)](https://codebeat.co/projects/github-com-b0bai-cutils-master)
[![Release](https://github.com/bobaikato/cutils/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/bobaikato/cutils/actions/workflows/maven-publish.yml)

A package of utility classes intended to enhance and simplify my development — You need `Java 8+`. You can browse the [Javadocs](https://javadoc.io/doc/art.cutils/cutils). If you have any questions related to usage, simple open an issue.


## Where can I get the latest release?
[![Maven Central](https://img.shields.io/maven-central/v/art.cutils/cutils)](https://search.maven.org/artifact/art.cutils/cutils)
[![javadoc](https://javadoc.io/badge/art.cutils/cutils.svg?color=brown)](https://javadoc.io/doc/art.cutils/cutils)

```xml
<dependency>
  <groupId>art.cutils</groupId>
  <artifactId>cutils</artifactId>
  <version>{version}</version>
</dependency> 
```
- Gradle Groovy
```
implementation 'art.cutils:cutils:{version}'
```
- Gradle Kotlin
```
compile("art.cutils:cutils:{version}")
```
- [More, and others](https://search.maven.org/artifact/art.cutils/cutils)



## Contributing
I accept Pull Requests via GitHub. Here are some guidelines which will make applying PRs easier:

1. No tabs! Please use spaces for indentation.
2. Respect the code style.
3. Create minimal diffs.
4. If it will help, disable on save actions like reformat source code or organize imports. **If you feel the source code should be reformatted create a separate PR for this change**.
5. Please, provide **JUnit tests** for your changes and make sure your changes don't break any existing tests by running `mvn clean test`.
6. Lastly, open PR to `development` branch and [follow this rudimentary convention](https://blog.jasonmeridth.com/posts/do-not-issue-pull-requests-from-your-master-branch/)

## License

This code is under the [Apache Licence v2](https://github.com/prohorde/cutils/blob/master/LICENSE).
