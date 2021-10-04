# GitHub API Wrapper

[![Gradle CI](https://github.com/NyCodeGHG/github-api-wrapper/actions/workflows/ci.yml/badge.svg?branch=dev)](https://github.com/NyCodeGHG/github-api-wrapper/actions/workflows/ci.yml)
![GitHub](https://img.shields.io/github/license/NyCodeGHG/github-api-wrapper?color=067abd&style=flat-square)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg?style=flat-square)](https://ktlint.github.io/)
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-multiplatform-7d23eb?logo=kotlin&style=flat-square)](https://kotlinlang.org)

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
    implementation(platform("dev.nycode.github:bom:1.0.0-SNAPSHOT"))
    implementation("dev.nycode.github:core")
    implementation("dev.nycode.github:repositories")
    implementation("dev.nycode.github:branches")
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
    implementation platform('dev.nycode.github:bom:1.0.0-SNAPSHOT')
    implementation 'dev.nycode.github:core'
    implementation 'dev.nycode.github:repositories'
    implementation 'dev.nycode.github:branches'
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

<dependencyManagement>
<dependencies>
    <dependency>
        <groupId>dev.nycode.github</groupId>
        <artifactId>bom</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
</dependencies>
</dependencyManagement>

<dependencies>
    <!-- Use the modules you need -->
    <dependency>
        <groupId>dev.nycode.github</groupId>
        <artifactId>core</artifactId>
    </dependency>
    <dependency>
        <groupId>dev.nycode.github</groupId>
        <artifactId>repositories</artifactId>
    </dependency>
    <dependency>
        <groupId>dev.nycode.github</groupId>
        <artifactId>branches</artifactId>
    </dependency>
</dependencies>
```

</details>
