package com.github.khanshoaib3.nerdsteam.utils

import kotlin.coroutines.cancellation.CancellationException

/**
 * By default, runCatching might catch a CancellationException, affecting coroutine executions. This prevents it.
 * Ref: https://androidpoet.medium.com/the-magic-of-kotlin-result-class-4894f7fec4a7
 */
public suspend inline fun <R> runSafeSuspendCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}