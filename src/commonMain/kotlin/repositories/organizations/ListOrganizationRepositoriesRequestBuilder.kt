package de.nycode.github.repositories.organizations

import de.nycode.github.request.RepositorySort
import de.nycode.github.request.RepositoryType
import de.nycode.github.request.SortDirection

public class ListOrganizationRepositoriesRequestBuilder(
    public var type: RepositoryType? = null,
    public var sort: RepositorySort? = null,
    public var direction: SortDirection? = null
)
