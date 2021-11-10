## CUTILS ART

[![License](https://img.shields.io/github/license/honerfor/cutils)](#License)
[![Build Status](https://github.com/prohorde/cutils/actions/workflows/maven.yml/badge.svg)](https://github.com/prohorde/cutils/actions/workflows/maven.yml)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/prohorde/cutils.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/prohorde/cutils/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/prohorde/cutils.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/prohorde/cutils/alerts/)
[![codebeat badge](https://codebeat.co/badges/be9f0284-b007-4f52-add7-479bb0140382)](https://codebeat.co/projects/github-com-prohorde-cutils-master)
[![Release](https://github.com/prohorde/cutils/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/prohorde/cutils/actions/workflows/maven-publish.yml)

A package of utilities classes intended to enhance and simplify Java development — You need `Java 8+`. You can browse the [Javadocs](https://javadoc.io/doc/pro.horde.os/cutils). If you have any questions related to usage, simple open an issue.



## Sponsor
<a href="https://www.jetbrains.com/?from=Cutils" target="_blank"><img src="https://avatars0.githubusercontent.com/u/878437?s=200&v=4" width="50"></a>



## Where can I get the latest release?
[![Maven Central](https://img.shields.io/maven-central/v/pro.horde.os/cutils)](https://search.maven.org/artifact/pro.horde.os/cutils)
[![Javadocs](https://javadoc.io/badge/pro.horde.os/cutils.svg?color=brown)](https://javadoc.io/doc/pro.horde.os/cutils)


```xml
<dependency>
  <groupId>pro.horde.os</groupId>
  <artifactId>cutils</artifactId>
  <version>{version}</version>
</dependency> 
```
- Gradle Groovy
```
implementation 'pro.horde.os:cutils:{version}'
```
- Gradle Kotlin
```
compile("pro.horde.os:cutils:{version}")
```
- [More, and others](https://search.maven.org/artifact/pro.horde.os/cutils)



## Contributing
We accept Pull Requests via GitHub. A public Slack Channel will soon be made available for communications.
But, in the mean time, there are some guidelines which will make applying PRs easier for us:

1. No tabs! Please use spaces for indentation.
2. Respect the code style.
3. Create minimal diffs.
4. If it will help, disable on save actions like reformat source code or organize imports. **If you feel the source code should be reformatted create a separate PR for this change**.
5. Please, provide **JUnit tests** for your changes and make sure your changes don't break any existing tests by running `mvn clean test`.
6. Lastly, open PR to `development` branch and [follow this rudimentary convention](https://blog.jasonmeridth.com/posts/do-not-issue-pull-requests-from-your-master-branch/)

## License

This code is under the [Apache Licence v2](https://github.com/prohorde/cutils/blob/master/LICENSE).
