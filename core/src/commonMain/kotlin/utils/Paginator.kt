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

package de.nycode.github.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Returns a flow which requests items in batches of [batchSize] items by calling [requestPaginated].
 */
@PublishedApi
@GitHubWrapperInternals
internal fun <T, C : Collection<T>> paginate(
    batchSize: Int = 20,
    requestPaginated: suspend (offset: Int, batchSize: Int) -> C
): Flow<T> = flow {
    var page = 0
    while (true) {
        val items = requestPaginated(page, batchSize)
        for (item in items) emit(item)
        if (items.size < batchSize) return@flow // no more items after this
        page++
    }
}
