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
import dev.nycode.github.repositories.contents.contents
import dev.nycode.github.repositories.contents.model.SubmoduleRepositoryContent
import dev.nycode.github.repositories.repositories
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertIs

internal class ContentTests {

    private val client = GitHubClient()

    @Test
    fun `request content`(): Unit = runBlocking {
        val response = client.repositories.contents.getRepositoryContent("PaperMC", "Paper", "work/Bukkit")
        assertIs<SubmoduleRepositoryContent>(response)
        println(response)
    }

    @Test
    fun readme(): Unit = runBlocking {
        val readme = client.repositories.contents.getRepositoryReadMe("PaperMC", "Paper")
        println(readme.html())
    }
}
