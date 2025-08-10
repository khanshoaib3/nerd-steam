package com.github.khanshoaib3.nerdsteam.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

// Ref: https://github.com/android/nav3-recipes/blob/main/app/src/main/java/com/example/nav3recipes/commonui/CommonUiActivity.kt
class TopLevelBackStack<T>(startKey: T) {

    // Maintain a stack for each top level route
    private var topLevelStacks: LinkedHashMap<T, SnapshotStateList<T>> = linkedMapOf(
        startKey to mutableStateListOf(startKey)
    )

    // Expose the current top level route for consumers
    var topLevelKey by mutableStateOf(startKey)
        private set

    var lastTopLevelKey: T? = null

    // Expose the back stack so it can be rendered by the NavDisplay
    val backStack = mutableStateListOf(startKey)

    private fun updateBackStack() =
        backStack.apply {
            clear()
            addAll(topLevelStacks.flatMap { it.value })
        }

    fun addTopLevel(key: T) {
        // If the top level doesn't exist, add it
        if (topLevelStacks[key] == null) {
            topLevelStacks.put(key, mutableStateListOf(key))
        } else {
            // Otherwise just move it to the end of the stacks
            topLevelStacks.apply {
                remove(key)?.let {
                    put(key, it)
                }
            }
        }
        lastTopLevelKey = topLevelKey
        topLevelKey = key
        updateBackStack()
    }

    fun add(key: T) {
        lastTopLevelKey = topLevelKey
        topLevelStacks[topLevelKey]?.add(key)
        updateBackStack()
    }

    @Suppress("unused")
    fun getCurrentBackstack() = topLevelStacks[topLevelKey]

    fun getLast() = topLevelStacks[topLevelKey]?.lastOrNull()

    fun removeLast() {
        val removedKey = topLevelStacks[topLevelKey]?.removeLastOrNull()
        // If the removed key was a top level key, remove the associated top level stack
        topLevelStacks.remove(removedKey)
        lastTopLevelKey = topLevelKey
        topLevelKey = topLevelStacks.keys.last()
        updateBackStack()
    }
}
