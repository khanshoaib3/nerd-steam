package com.github.khanshoaib3.steamcompanion.ui.navigation.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.ui.utils.NAV_TOP_LEVEL_ROUTES
import com.github.khanshoaib3.steamcompanion.ui.utils.Route

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SteamCompanionNavBar(
    currentTopLevelRoute: Route,
    navigateTo: (Route) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val customWindowInsets = WindowInsets(
        top = 0,
        left = WindowInsets.systemBars.getLeft(density, LocalLayoutDirection.current),
        right = WindowInsets.systemBars.getRight(density, LocalLayoutDirection.current),
        bottom = WindowInsets.systemBars.getBottom(density) / 3
    )

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .offset(y = (1).dp),
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
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .scale(1.25f)
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
    SteamCompanionTheme {
        Column {
            SteamCompanionNavBar(
                currentTopLevelRoute = Route.Home,
                navigateTo = {},
            )
        }
    }
}
