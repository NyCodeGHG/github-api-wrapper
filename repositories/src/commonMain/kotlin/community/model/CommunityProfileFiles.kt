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

package dev.nycode.github.repositories.community.model

import dev.nycode.github.model.SimpleLicense
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CommunityProfileFiles(
    @SerialName("code_of_conduct")
    val codeOfConduct: SimpleCodeOfConduct?,
    @SerialName("code_of_conduct_file")
    val codeOfConductFile: CommunityHealthFile?,
    val license: SimpleLicense,
    val contributing: CommunityHealthFile?,
    val readme: CommunityHealthFile?,
    @SerialName("issue_template")
    val issueTemplate: CommunityHealthFile?,
    @SerialName("pull_request_template")
    val pullRequestTemplate: CommunityHealthFile?
)
