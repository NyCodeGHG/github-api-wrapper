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

import de.nycode.github.GitHubClient
import de.nycode.github.utils.GitHubWrapperInternals
import de.nycode.github.utils.paginate
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import kotlinx.coroutines.flow.Flow

@GitHubWrapperInternals
public suspend inline fun <reified T> GitHubClient.request(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    httpClient.request {
        RequestBuilder().apply(builder).requests.forEach { it() }
        url.takeFrom(URLBuilder(baseUrl).path(*path))
    }

@GitHubWrapperInternals
public suspend inline fun <reified T> GitHubClient.get(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    request(*path) {
        builder()
        request {
            method = HttpMethod.Get
        }
    }

@GitHubWrapperInternals
public suspend inline fun <reified T> GitHubClient.post(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    request(*path) {
        builder()
        request {
            method = HttpMethod.Post
        }
    }

@GitHubWrapperInternals
public suspend inline fun <reified T> GitHubClient.put(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    request(*path) {
        builder()
        request {
            method = HttpMethod.Put
        }
    }

@GitHubWrapperInternals
public suspend inline fun <reified T> GitHubClient.patch(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    request(*path) {
        builder()
        request {
            method = HttpMethod.Patch
        }
    }

@GitHubWrapperInternals
public suspend inline fun <reified T> GitHubClient.delete(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    request(*path) {
        builder()
        request {
            method = HttpMethod.Delete
        }
    }

/**
 * Implements parameters for [pagination](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#pagination) in the GitHub API.
 *
 * @param T the response type of the endpoint (See [PaginatedRequestBuilder.mapper]
 * @param R the type of the resulting [Flow]
 *
 * @see paginate
 * @see simplePaginatedGet
 * @see paginatedGet
 */
@GitHubWrapperInternals
public inline fun <reified T, R> GitHubClient.paginatedRequest(
    vararg path: String,
    builder: PaginatedRequestBuilder<T, R>.() -> Unit
): Flow<R> {
    val (perPage, builders, mapper) = PaginatedRequestBuilder<T, R>().apply(builder)

    return paginate(perPage) { offset, batchSize ->
        val response = request<T>(*path) {
            request {
                parameter("per_page", batchSize)
                parameter("page", offset + 1)

                builders.forEach { it() }
            }
        }

        mapper(response)
    }
}

/**
 * Performs a paginated GET Request.
 *
 * @see paginatedRequest
 */
@GitHubWrapperInternals
public inline fun <reified T, R> GitHubClient.paginatedGet(
    vararg path: String,
    builder: PaginatedRequestBuilder<T, R>.() -> Unit = {}
): Flow<R> = paginatedRequest<T, R>(*path) {
    builder()
    request {
        method = HttpMethod.Get
    }
}

/**
 * Performs a paginated GET Request which requests a [List] of [T].
 *
 * @see paginatedRequest
 */
@GitHubWrapperInternals
public inline fun <reified T> GitHubClient.simplePaginatedGet(
    vararg path: String,
    builder: SimplePaginatedRequestBuilder<T>.() -> Unit = {}
): Flow<T> = paginatedRequest<List<T>, T>(*path) {
    builder()
    request {
        method = HttpMethod.Get
    }
}
