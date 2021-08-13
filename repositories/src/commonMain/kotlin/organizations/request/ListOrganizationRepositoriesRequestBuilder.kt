/*
 *    Copyright 2021 NyCode
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.nycode.github.repositories.organizations.request

import de.nycode.github.request.RepositorySort
import de.nycode.github.request.RepositoryType
import de.nycode.github.request.SortDirection

/**
 * Builder for configuring the [List organization repositories endpoint](https://docs.github.com/en/rest/reference/repos#list-organization-repositories).
 *
 * @property type specifies the types of repositories you want returned
 * @property sort specify the sorting type
 * @property direction specify the sort direction
 * @property perPage specify how many items should be in a page
 */
public data class ListOrganizationRepositoriesRequestBuilder(
    public var type: RepositoryType? = null,
    public var sort: RepositorySort? = null,
    public var direction: SortDirection? = null,
    public var perPage: Int? = null
)
