package de.nycode.github.request

public enum class RepositorySort {
    CREATED,
    UPDATED,
    PUSHED,
    FULL_NAME;

    override fun toString(): String = name.lowercase()
}
