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
