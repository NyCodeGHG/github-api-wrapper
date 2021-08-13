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

package de.nycode.github

import io.ktor.client.HttpClient

/**
 * Represents the root implementation of the [GitHub REST API v3](https://docs.github.com/en/rest/).
 *
 * Please use the [GitHubClient] function to create an instance
 *
 * @property baseUrl the base url of the GitHub API. Most of the time, you want to leave that to the default value.
 */
public class GitHubClient internal constructor(
    public val baseUrl: String = "https://api.github.com",
    @PublishedApi
    internal val httpClient: HttpClient
) {

    init {
        require(baseUrl.startsWith("https://")) { "GitHub API base url must start with https." }
    }

}
