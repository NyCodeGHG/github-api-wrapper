package auth

import de.nycode.github.GitHubClient
import de.nycode.github.auth.AuthProvider
import io.ktor.client.features.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFailsWith

class AuthenticationTests {

    private val authenticatedClient = GitHubClient(authProvider = AuthProvider.OAuth(System.getenv("GITHUB_TOKEN")))
    private val client = GitHubClient()

    private val repoOwner: String = System.getenv("REPO_OWNER")!!
    private val repoName: String = System.getenv("REPO_NAME")!!

    @Test
    fun `Authenticated Request works`(): Unit = runBlocking {
        authenticatedClient.repos.getRepository(repoOwner, repoName)
    }

    @Test
    fun `Unauthenticated Request fails`(): Unit = runBlocking {
        assertFailsWith<ClientRequestException> {
            client.repos.getRepository(repoOwner, repoName)
        }
    }

}
