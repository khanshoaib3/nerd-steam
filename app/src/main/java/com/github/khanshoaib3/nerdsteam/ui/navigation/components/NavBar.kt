package com.github.khanshoaib3.nerdsteam.ui.navigation.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.khanshoaib3.nerdsteam.ui.theme.NerdSteamTheme
import com.github.khanshoaib3.nerdsteam.ui.utils.NAV_TOP_LEVEL_ROUTES
import com.github.khanshoaib3.nerdsteam.ui.utils.Route
import com.github.khanshoaib3.nerdsteam.ui.utils.TopLevelRoute

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NavBar(
    currentTopLevelRoute: Route,
    navigateTo: (Route) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    // Ref: https://stackoverflow.com/questions/74848618/how-to-detect-whether-gesture-navigation-or-button-navigation-is-active
    val isInGestureNavMode =
        WindowInsets.systemGestures.getLeft(density, LocalLayoutDirection.current) > 0
                || WindowInsets.systemGestures.getRight(density, LocalLayoutDirection.current) > 0
    val customWindowInsets = WindowInsets(
        top = 0,
        left = WindowInsets.navigationBars.getLeft(density, LocalLayoutDirection.current),
        right = WindowInsets.navigationBars.getRight(density, LocalLayoutDirection.current),
        bottom = WindowInsets.navigationBars.getBottom(density) / (if (isInGestureNavMode) 3 else 1)
    )

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .offset(y = 1.dp),
        windowInsets = customWindowInsets
    ) {
        NAV_TOP_LEVEL_ROUTES.forEach { route ->
            val isSelected = currentTopLevelRoute == route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navigateTo(route)
                },
                icon = {
                    Icon(
                        imageVector = (if (isSelected) route.selectedIcon else route.icon)
                            ?: Icons.Default.QuestionMark,
                        contentDescription = route.name,
                        modifier = Modifier.size(28.dp)
                    )
                }
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavBarPreview() {
    NerdSteamTheme {
        Column {
            NavBar(
                currentTopLevelRoute = TopLevelRoute.Home,
                navigateTo = {},
            )
        }
    }
}
