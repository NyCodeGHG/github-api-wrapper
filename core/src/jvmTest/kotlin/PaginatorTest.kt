import dev.nycode.github.GitHubClient
import dev.nycode.github.GitHubClientImpl
import dev.nycode.github.request.simplePaginatedGet
import dev.nycode.github.utils.paginate
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

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

class PaginatorTest {

    // Requests to paginated endpoint
    private var batchRequests = 0

    // baseURL domain
    private val paginatedDomain = "https://schlau.bi/"

    // endpoint for paginated requests
    private val paginatedEndpoint = "pagination-in-kotlin-is-cool"

    // generator for paginated numbers
    private val content = generateSequence(1) { prev -> prev + 1 }

    // batchSize used for requests
    private val batchSize = 20

    // custom client
    private val client = GitHubClient {
        baseUrl = paginatedDomain
        httpClient = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.encodedPath.substringAfter('/') == paginatedEndpoint) {
                        val responseHeaders = headersOf("Content-Type", ContentType.Application.Json.toString())

                        val limit = request.url.parameters["per_page"]!!.toInt()
                        val page = request.url.parameters["page"]!!.toInt() - 1
                        val response = content.drop(limit * page).take(limit).toList()
                        val json = Json.encodeToString(response)

                        batchRequests++
                        respond(json, headers = responseHeaders)
                    } else error("Unknown endpoint")
                }
            }
        }
    } as GitHubClientImpl

    @Test
    fun `paginate one batch`() = runBlocking {
        assertBatchCount(1, batchSize)
    }

    @Test
    fun `request one batch`() = runBlocking {
        assertRequestedBatchCount(1, batchSize)
    }

    @Test
    fun `paginate more batches exact`() = runBlocking {
        assertBatchCount(5, batchSize * 5)
    }

    @Test
    fun `request more batches exact`() = runBlocking {
        assertRequestedBatchCount(5, batchSize * 5)
    }

    @Test
    fun `paginate more batches exceeding exact`() = runBlocking {
        // request more than 4 batches but not exactly 5
        assertBatchCount(5, batchSize * 5 - batchSize / 2)
    }

    @Test
    fun `request more batches exceeding exact`() = runBlocking {
        // request more than 4 batches but not exactly 5
        assertRequestedBatchCount(5, batchSize * 5 - batchSize / 2)
    }

    private suspend fun assertBatchCount(expectedCount: Int, requestItems: Int) {
        var count = 0
        val items = paginate(batchSize) { page, limit ->
            count++
            content.drop(limit * page).take(limit).toList()
        }.take(requestItems).toList()
        assertEquals(content.take(requestItems).toList(), items, "Items must be first $requestItems items")
        assertEquals(expectedCount, count, "Must request exactly one batch")
    }

    private suspend fun assertRequestedBatchCount(expectedCount: Int, requestItems: Int) {
        batchRequests = 0
        val items = client.simplePaginatedGet<Int>(paginatedEndpoint).take(requestItems).toList()
        assertEquals(content.take(requestItems).toList(), items, "Items must be first $requestItems items")
        assertEquals(expectedCount, batchRequests, "Must request exactly one batch")
    }
}
