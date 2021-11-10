rootProject.name = "github-api-wrapper"

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    "core",
    "repositories",
    "repositories:branches",
    "bom",
    "full",
    "repositories:autolinks",
    "repositories:collaborators",
    "repositories:comments",
    "repositories:commits",
    "repositories:community",
    "repositories:contents",
    "repositories:deploy-keys"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("ktor", "1.6.5")
            alias("ktor-client-core").to("io.ktor", "ktor-client-core").versionRef("ktor")
            alias("ktor-client-okhttp").to("io.ktor", "ktor-client-okhttp").versionRef("ktor")
            alias("ktor-client-js").to("io.ktor", "ktor-client-js").versionRef("ktor")
            alias("ktor-client-auth").to("io.ktor", "ktor-client-auth").versionRef("ktor")
            alias("ktor-client-mock").to("io.ktor", "ktor-client-mock").versionRef("ktor")
            alias("ktor-client-serialization").to("io.ktor", "ktor-client-serialization").versionRef("ktor")
            alias("kotlinx-serialization-json").to("org.jetbrains.kotlinx", "kotlinx-serialization-json")
                .version("1.3.0")
            alias("kotlinx-datetime").to("org.jetbrains.kotlinx", "kotlinx-datetime").version("0.2.1")
        }

        create("test") {
            version("junit", "5.8.1")
            alias("junit5").to("org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
        }
    }
}
