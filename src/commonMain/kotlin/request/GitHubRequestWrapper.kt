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
