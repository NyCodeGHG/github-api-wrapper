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

public data class ListOrganizationRepositoriesRequestBuilder(
    public var type: RepositoryType? = null,
    public var sort: RepositorySort? = null,
    public var direction: SortDirection? = null,
    public var page: Int? = null,
    public var perPage: Int? = null
)
