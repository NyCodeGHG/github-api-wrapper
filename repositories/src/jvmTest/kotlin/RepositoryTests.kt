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

import dev.nycode.github.GitHubClient
import dev.nycode.github.auth.AuthProvider
import dev.nycode.github.preview.ApiPreview
import dev.nycode.github.repositories.repositories
import dev.nycode.github.request.GitHubRequestException
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RepositoryTests {

    private val client = GitHubClient {
        authProvider = if (System.getenv("GITHUB_TOKEN") != null)
            AuthProvider.OAuth(System.getenv("GITHUB_TOKEN"))
        else
            AuthProvider.None
    }

    private val unauthenticatedClient = GitHubClient()

    @Test
    fun `getRepository returns correct data`(): Unit = runBlocking {
        val repo = client.repositories.getRepository("octocat", "Spoon-Knife")
        assertEquals("octocat", repo.owner.login)
        assertEquals("Spoon-Knife", repo.name)
    }

    @OptIn(ApiPreview::class)
    @Test
    fun `getRepositoryTopics returns correct data`(): Unit = runBlocking {
        val topics = client.repositories.getRepositoryTopics("kordlib", "kord")
        assert(topics.toList().isNotEmpty())
    }

    @Test
    fun `listRepositoriesForAuthenticatedUser should throw exception without authentication`(): Unit = runBlocking {
        assertFailsWith<GitHubRequestException> {
            unauthenticatedClient.repositories.listRepositoriesForAuthenticatedUser()
        }
    }
}
