import de.nycode.github.GitHubClient
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class RepositoryTests {

    private val client = GitHubClient()

    @Test
    fun `getRepository returns correct data`(): Unit = runBlocking {
        val repo = client.repos.getRepository("octocat", "Spoon-Knife")
        assertEquals("octocat", repo.owner.login)
        assertEquals("Spoon-Knife", repo.name)
    }

}
