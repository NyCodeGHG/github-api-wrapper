package de.nycode.github.repositories.organizations

import de.nycode.github.request.SortDirection

public class ListOrganizationRepositoriesRequestBuilder(
    public var type: RepositoryType? = null,
    public var sort: RepositorySort? = null,
    public var direction: SortDirection? = null
)

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

public enum class RepositorySort {
    CREATED,
    UPDATED,
    PUSHED,
    FULL_NAME;

    override fun toString(): String = name.lowercase()
}
