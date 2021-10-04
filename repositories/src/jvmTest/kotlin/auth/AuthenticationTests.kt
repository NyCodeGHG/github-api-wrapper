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

package auth

import dev.nycode.github.GitHubClient
import dev.nycode.github.auth.AuthProvider
import dev.nycode.github.repositories.repositories
import dev.nycode.github.request.GitHubRequestException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import kotlin.test.Test
import kotlin.test.assertFailsWith

class AuthenticationTests {

    private val client = GitHubClient()

    private val repoOwner: String = System.getenv("REPO_OWNER") ?: "NyCodeGHG"
    private val repoName: String = System.getenv("REPO_NAME") ?: "pathfinding-plugin"

    @EnabledIfEnvironmentVariable(named = "GITHUB_TOKEN", matches = ".+")
    @Test
    fun `Authenticated Request works`(): Unit = runBlocking {
        val authenticatedClient = GitHubClient {
            authProvider = AuthProvider.OAuth(System.getenv("GITHUB_TOKEN"))
        }
        authenticatedClient.repositories.getRepository(repoOwner, repoName)
    }

    @Test
    fun `Unauthenticated Request fails`(): Unit = runBlocking {
        assertFailsWith<GitHubRequestException> {
            client.repositories.getRepository(repoOwner, repoName)
        }
    }
}
