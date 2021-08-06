import de.nycode.github.GitHubClient
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class RepositoriesOrganizationsAPITests {

    private val client = GitHubClient()

    @Test
    fun `listOrganizationRepositories returns correct data`() = runBlocking {
        val repos = client.repos.organizations.listOrganizationRepositories("PaperMC")
        println(repos.joinToString { it.name })
    }

}