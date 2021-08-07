package de.nycode.github.request

public enum class RepositoryType {
    ALL,
    PUBLIC,
    PRIVATE,
    FORKS,
    SOURCES,
    MEMBER,
    INTERNAL;

    override fun toString(): String = name.lowercase()
}
