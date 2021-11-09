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

package dev.nycode.github.ktor

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.http.HttpHeaders
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelinePhase

@Suppress("EXPERIMENTAL_API_USAGE_FUTURE_ERROR")
internal class GitHubApiWrapperPlugin {
    companion object Feature : HttpClientFeature<Unit, GitHubApiWrapperPlugin> {
        override val key: AttributeKey<GitHubApiWrapperPlugin> = AttributeKey("github-api-wrapper")

        override fun install(feature: GitHubApiWrapperPlugin, scope: HttpClient) {
            val newPhase = PipelinePhase("github")
            scope.requestPipeline.insertPhaseAfter(HttpRequestPipeline.Transform, newPhase)
            scope.requestPipeline.intercept(newPhase) {
                context.headers.remove(HttpHeaders.Accept)
            }
        }

        override fun prepare(block: Unit.() -> Unit): GitHubApiWrapperPlugin {
            return GitHubApiWrapperPlugin()
        }
    }
}
