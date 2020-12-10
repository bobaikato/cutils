## Honerfor Common Utilities (Cutils)
[![License](https://img.shields.io/github/license/honerfor/cutils)](#License)
[![Build Status](https://travis-ci.org/Honerfor/cutils.svg?branch=master)](https://travis-ci.org/Honerfor/cutils)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/Honerfor/cutils.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Honerfor/cutils/context:java)
[![CircleCI](https://img.shields.io/circleci/build/gh/Honerfor/cutils?color=%23088A08&label=test)](https://circleci.com/gh/Honerfor/cutils)
[![Coverage Status](https://coveralls.io/repos/github/Honerfor/cutils/badge.svg?branch=master)](https://coveralls.io/github/Honerfor/cutils?branch=master)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/Honerfor/cutils.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Honerfor/cutils/alerts/)
[![codebeat badge](https://codebeat.co/badges/da875096-0224-474a-82ba-41e39b146a89)](https://codebeat.co/projects/github-com-honerfor-cutils-development)

A package of utilities classes intended to enhance and simplify Java development — You need `Java 8+`. You can browse the [Javadocs](https://javadoc.io/doc/com.honerfor/cutils). If you have any questions related to usage, simple open an issue.


## Sponsor
<a href="https://www.jetbrains.com/?from=Cutils" target="_blank"><img src="https://avatars0.githubusercontent.com/u/878437?s=200&v=4" width="50"></a>



## Where can I get the latest release?
[![Maven Central](https://img.shields.io/maven-central/v/com.honerfor/cutils)](https://search.maven.org/artifact/com.honerfor/cutils)
[![Javadocs](https://javadoc.io/badge/com.honerfor/cutils.svg?color=brown)](https://javadoc.io/doc/com.honerfor/cutils)


```xml
<dependency>
  <groupId>com.honerfor</groupId>
  <artifactId>cutils</artifactId>
  <version>{version}</version>
</dependency> 
```
- Gradle Groovy
```
implementation 'com.honerfor:cutils:{version}'
```
- Gradle Kotlin
```
compile("com.honerfor:cutils:{version}")
```
- [More, and others](https://search.maven.org/artifact/com.honerfor/cutils)

## Contributing
We accept Pull Requests via GitHub. A public Slack Channel will soon be made available for communications.
But, in the mean time, there are some guidelines which will make applying PRs easier for us:

1. No tabs! Please use spaces for indentation.
2. Respect the code style.
3. Create minimal diffs.
4. If it will help, disable on save actions like reformat source code or organize imports. **If you feel the source code should be reformatted create a separate PR for this change**.
5. Please, provide **JUnit tests** for your changes and make sure your changes don't break any existing tests by running `mvn clean test`.
6. Lastly, [follow this rudimentary convention](https://blog.jasonmeridth.com/posts/do-not-issue-pull-requests-from-your-master-branch/)

## License

This code is under the [Apache Licence v2](https://github.com/Honerfor/Common/blob/master/LICENSE).
