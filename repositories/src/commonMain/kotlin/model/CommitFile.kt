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

package dev.nycode.github.repositories.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CommitFile(
    val filename: String,
    val additions: Int,
    val deletions: Int,
    val changes: Int,
    val status: String,
    @SerialName("raw_url")
    val rawUrl: String,
    @SerialName("blob_url")
    val blobUrl: String,
    val patch: String? = null,
    val sha: String,
    @SerialName("contents_url")
    val contentsUrl: String,
    @SerialName("previous_filename")
    val previousFilename: String? = null
)
