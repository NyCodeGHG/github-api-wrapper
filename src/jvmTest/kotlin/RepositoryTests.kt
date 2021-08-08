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
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class RepositoryTests {

    private val client = GitHubClient(authProvider = AuthProvider.Basic("nycodeghg", System.getenv("GITHUB_TOKEN")))

    @Test
    fun `getRepository returns correct data`(): Unit = runBlocking {
        val repo = client.repos.getRepository("octocat", "Spoon-Knife")
        assertEquals("octocat", repo.owner.login)
        assertEquals("Spoon-Knife", repo.name)
    }
}
