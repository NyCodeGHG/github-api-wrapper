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
import dev.nycode.github.repositories.repositories
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class RepositoriesOrganizationsAPITests {

    private val client = GitHubClient {
        authProvider =
            if (System.getenv("GITHUB_TOKEN") != null)
                AuthProvider.OAuth(System.getenv("GITHUB_TOKEN"))
            else AuthProvider.None
    }

    @Test
    fun `listOrganizationRepositories returns correct data`() = runBlocking {
        val repos = client.repositories.organizations.listOrganizationRepositories("PaperMC")
        println(repos.toList().joinToString { it.name })
    }
}
