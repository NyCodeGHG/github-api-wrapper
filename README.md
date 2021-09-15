# GitHub API Wrapper

[![Gradle CI](https://github.com/NyCodeGHG/github-api-wrapper/actions/workflows/ci.yml/badge.svg?branch=dev)](https://github.com/NyCodeGHG/github-api-wrapper/actions/workflows/ci.yml)
![GitHub](https://img.shields.io/github/license/NyCodeGHG/github-api-wrapper?color=067abd&style=flat-square)

This is a [Kotlin](https://kotlinlang.org) wrapper for the [GitHub REST API v3](https://docs.github.com/rest/).

**Note: This library is in a very early stage. The GitHub API is very big, so contributions are welcome.**

## Snapshots

Snapshots are available on [Sonatype Snapshots](https://s01.oss.sonatype.org/content/repositories/snapshots/).

<details open>
<summary>Gradle Kotlin</summary>

```kotlin
repositories {
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    // Use the modules you need
    implementation("dev.nycode.github:core:1.0.0-SNAPSHOT")
    implementation("dev.nycode.github:repositories:1.0.0-SNAPSHOT")
    implementation("dev.nycode.github:branches:1.0.0-SNAPSHOT")
}
```

</details>

<details>
<summary>Gradle Groovy</summary>

```groovy
repositories {
    maven {
        url 'https://s01.oss.sonatype.org/content/repositories/snapshots/'
    }
}

dependencies {
    // Use the modules you need
    implementation 'dev.nycode.github:core:1.0.0-SNAPSHOT'
    implementation 'dev.nycode.github:repositories:1.0.0-SNAPSHOT'
    implementation 'dev.nycode.github:branches:1.0.0-SNAPSHOT'
}
```

</details>

<details>
<summary>Maven</summary>

```xml
<repositories>
    <repository>
        <id>sonatype-01</id>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
</repositories>

<dependencies>
    <!-- Use the modules you need -->
    <dependency>
        <groupId>dev.nycode.github</groupId>
        <artifactId>core</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>dev.nycode.github</groupId>
        <artifactId>repositories</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>dev.nycode.github</groupId>
        <artifactId>branches</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

</details>
