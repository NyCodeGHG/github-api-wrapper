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
import de.nycode.github.repositories.branches.branches
import de.nycode.github.repositories.repositories
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import kotlin.test.Test

class RepositoryBranchTests {

    private val client = GitHubClient {
        authProvider = if (System.getenv("GITHUB_TOKEN") != null)
            AuthProvider.OAuth(System.getenv("GITHUB_TOKEN"))
        else
            AuthProvider.None
    }

    @Test
    fun `Get a branch`(): Unit = runBlocking {
        println(client.repositories.branches.getBranch("NyCodeGHG", "github-api-wrapper", "main"))
    }

    @Test
    fun `list branches`(): Unit = runBlocking {
        println(client.repositories.branches.listBranches("NyCodeGHG", "github-api-wrapper").collect())
    }

    @EnabledIfEnvironmentVariable(named = "GITHUB_TOKEN", matches = ".+")
    @Test
    fun `get repository protection`(): Unit = runBlocking {
        println(client.repositories.branches.getBranchProtection("NyCodeGHG", "github-api-wrapper", "main"))
    }
}
