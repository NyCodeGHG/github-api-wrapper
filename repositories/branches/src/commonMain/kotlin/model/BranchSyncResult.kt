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

package dev.nycode.github.repositories.branches.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public sealed interface BranchSyncResult {
    @Serializable
    public class Success(
        public val message: String,
        @SerialName("merge_type")
        public val mergeType: MergeType,
        @SerialName("base_branch")
        public val baseBranch: String
    ) : BranchSyncResult {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Success) return false

            if (message != other.message) return false
            if (mergeType != other.mergeType) return false
            if (baseBranch != other.baseBranch) return false

            return true
        }

        override fun hashCode(): Int {
            var result = message.hashCode()
            result = 31 * result + mergeType.hashCode()
            result = 31 * result + baseBranch.hashCode()
            return result
        }

        override fun toString(): String {
            return "Success(message='$message', mergeType=$mergeType, baseBranch='$baseBranch')"
        }
    }

    public object Conflict : BranchSyncResult

    public object Unprocessable : BranchSyncResult

    public object Unknown : BranchSyncResult
}
