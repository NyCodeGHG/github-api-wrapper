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

package de.nycode.github.request

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Implements parameters for [pagination](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#pagination) in the GitHub API.
 */
internal suspend inline fun <reified T> HttpClient.paginatedRequest(
    page: Int? = null,
    perPage: Int? = null,
    builder: HttpRequestBuilder.() -> Unit
): T =
    request {
        require(page == null || perPage in 1..100) { "perPage must be between 1 and 100" }
        parameter("per_page", perPage)
        require(page == null || page > 0) { "page mustn't be negative" }
        parameter("page", page)
        builder()
    }

/**
 * Performs a paginated GET Request.
 */
internal suspend inline fun <reified T> HttpClient.paginatedGet(
    page: Int? = null,
    perPage: Int? = null,
    builder: HttpRequestBuilder.() -> Unit
): T =
    paginatedRequest(page, perPage) {
        method = HttpMethod.Get
        builder()
    }
