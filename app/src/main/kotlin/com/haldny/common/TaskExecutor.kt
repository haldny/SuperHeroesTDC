package com.haldny.common

import kotlinx.coroutines.*

suspend fun <T> async(
    block: suspend CoroutineScope.() -> T
): T = withContext(Dispatchers.Default, block)