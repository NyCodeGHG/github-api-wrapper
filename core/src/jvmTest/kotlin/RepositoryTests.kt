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

import de.nycode.github.GitHubClient
import de.nycode.github.auth.AuthProvider
import de.nycode.github.preview.ApiPreview
import de.nycode.github.request.GitHubRequestException
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class RepositoryTests {

    private val client = GitHubClient(authProvider = AuthProvider.Basic("nycodeghg", System.getenv("GITHUB_TOKEN")))

    private val unauthenticatedClient = GitHubClient()

    @Test
    fun `getRepository returns correct data`(): Unit = runBlocking {
        val repo = client.repos.getRepository("octocat", "Spoon-Knife")
        assertEquals("octocat", repo.owner.login)
        assertEquals("Spoon-Knife", repo.name)
    }

    @OptIn(ApiPreview::class)
    @Test
    fun `getRepositoryTopics returns correct data`(): Unit = runBlocking {
        val topics = client.repos.getRepositoryTopics("kordlib", "kord")
        assert(topics.isNotEmpty())
    }

    @OptIn(ApiPreview::class)
    @Test
    fun `checkVulnerabilityAlertsEnabled returns correct data`(): Unit = runBlocking {
        assertTrue {
            client.repos.checkVulnerabilityAlertsEnabled("NyCodeGHG", "github-api-wrapper")
        }
        assertFalse {
            client.repos.checkVulnerabilityAlertsEnabled("NyCodeGHG", "github-api")
        }
    }

    @Test
    fun `listRepositoriesForAuthenticatedUser should throw exception without authentication`(): Unit = runBlocking {
        assertFailsWith<GitHubRequestException> {
            unauthenticatedClient.repos.listRepositoriesForAuthenticatedUser()
        }
    }
}
