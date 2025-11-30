package com.github.khanshoaib3.nerdsteam.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND


// --- TwoPaneScene ---
// Ref: https://github.com/android/nav3-recipes/tree/main/app/src/main/java/com/example/nav3recipes/scenes/twopane
/**
 * A custom [Scene] that displays two [NavEntry]s side-by-side in a 50/50 split.
 */
class TwoPaneScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val firstEntry: NavEntry<T>,
    val secondEntry: NavEntry<T>
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(firstEntry, secondEntry)
    override val content: @Composable (() -> Unit) = {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(0.45f)) {
                firstEntry.Content()
            }
            Column(modifier = Modifier.weight(0.55f)) {
                secondEntry.Content()
            }
        }
    }

    companion object {
        internal const val TWO_PANE_FIRST_KEY = "TwoPaneFirst"
        internal const val TWO_PANE_SECOND_KEY = "TwoPaneSecond"
        internal var IsActive = false

        /**
         * Helper function to add metadata to a [NavEntry] indicating it can be displayed
         * in a two-pane layout.
         */
        fun setAsFirst() = mapOf(TWO_PANE_FIRST_KEY to true)
        fun setAsSecond() = mapOf(TWO_PANE_SECOND_KEY to true)
    }
}

// --- TwoPaneSceneStrategy ---
/**
 * A [SceneStrategy] that activates a [TwoPaneScene] if the window is wide enough
 * and the top two back stack entries declare support for two-pane display.
 */
class TwoPaneSceneStrategy<T : Any>(
    val windowSizeClass: WindowSizeClass,
    val adaptiveInfo: WindowAdaptiveInfo,
    val configuration: Configuration
) : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {

        // TODO Maybe extract this into a util. *This is also in RootNavDisplay*
//        val adaptiveInfo = currentWindowAdaptiveInfo()

        val showNavRail = when {
            adaptiveInfo.windowPosture.isTabletop -> false
            windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
                -> true

            else -> false
        }
        val isWideScreen = when {
            !showNavRail -> false
            windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND)
                    && !windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_DP_EXPANDED_LOWER_BOUND)
                -> true

            windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
                    && !windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_DP_MEDIUM_LOWER_BOUND)
                -> true

            configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> true

            else -> false
        }

        // Condition 1: Only return the Scene if the window is sufficiently wide
        // (and not in portrait) to render two panes.
        if (!isWideScreen) {
            TwoPaneScene.IsActive = false
            return null
        }

        val lastTwoEntries = entries.takeLast(2)

        // Condition 2: Only return a Scene if there are two entries, and both have declared
        // they can be displayed in a two pane scene.
        return if (lastTwoEntries.size == 2
            && lastTwoEntries.first().metadata.containsKey(TwoPaneScene.TWO_PANE_FIRST_KEY)
            && lastTwoEntries.last().metadata.containsKey(TwoPaneScene.TWO_PANE_SECOND_KEY)
        ) {
            val firstEntry = lastTwoEntries.first()
            val secondEntry = lastTwoEntries.last()

            // The scene key must uniquely represent the state of the scene.
            // A Pair of the first and second entry keys ensures uniqueness.
            TwoPaneScene.IsActive = true

            TwoPaneScene(
                key = firstEntry.contentKey,
                // Where we go back to is a UX decision. In this case, we only remove the top
                // entry from the back stack, despite displaying two entries in this scene.
                // This is because in this app we only ever add one entry to the
                // back stack at a time. It would therefore be confusing to the user to add one
                // when navigating forward, but remove two when navigating back.
                previousEntries = entries.dropLast(1),
                firstEntry = firstEntry,
                secondEntry = secondEntry
            )

        } else {
            TwoPaneScene.IsActive = false
            null
        }
    }
}
