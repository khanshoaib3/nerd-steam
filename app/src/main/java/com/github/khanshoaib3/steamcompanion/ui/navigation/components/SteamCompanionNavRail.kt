package com.github.khanshoaib3.steamcompanion.ui.navigation.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalWideNavigationRail
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.ui.utils.NAV_OTHER_ROUTES
import com.github.khanshoaib3.steamcompanion.ui.utils.NAV_TOP_LEVEL_ROUTES
import com.github.khanshoaib3.steamcompanion.ui.utils.Route

@Composable
fun SteamCompanionNavRail(
    currentTopLevelRoute: Route,
    navigateTo: (Route) -> Unit,
    railState: WideNavigationRailState,
    onRailButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isWideScreen: Boolean,
) {
    ModalWideNavigationRail(
        state = railState,
        hideOnCollapse = isWideScreen,
        header = {
            IconButton(
                modifier =
                    Modifier
                        .padding(start = 24.dp)
                        .semantics {
                            // The button must announce the expanded or collapsed state of the rail
                            // for accessibility.
                            stateDescription =
                                if (railState.currentValue == WideNavigationRailValue.Expanded) {
                                    "Expanded"
                                } else {
                                    "Collapsed"
                                }
                        },
                onClick = onRailButtonClicked,
            ) {
                if (railState.targetValue == WideNavigationRailValue.Expanded) {
                    Icon(Icons.AutoMirrored.Filled.MenuOpen, "Collapse rail")
                } else {
                    Icon(Icons.Filled.Menu, "Expand rail")
                }
            }
        },
        modifier = if (isWideScreen) modifier else modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .offset(x = (-1).dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .requiredWidth(IntrinsicSize.Max)
        ) {
            Column {
                NAV_TOP_LEVEL_ROUTES.forEach { route ->
                    val isSelected = currentTopLevelRoute == route
                    WideNavigationRailItem(
                        railExpanded = railState.targetValue == WideNavigationRailValue.Expanded,
                        icon = {
                            val imageVector = (if (isSelected) route.selectedIcon else route.icon)
                                ?: Icons.Default.QuestionMark
                            Icon(imageVector = imageVector, contentDescription = route.name)
                        },
                        label = { Text(route.name) },
                        selected = isSelected,
                        onClick = { navigateTo(route) }
                    )
                }
            }
            if (railState.targetValue == WideNavigationRailValue.Expanded) {
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth(0.9f)
                        .padding(
                            top = dimensionResource(R.dimen.padding_medium),
                            bottom = dimensionResource(R.dimen.padding_small),
                            start = 20.dp
                        )
                        .align(Alignment.CenterHorizontally)
                )
                Column {
                    NAV_OTHER_ROUTES.forEach { route ->
                        WideNavigationRailItem(
                            railExpanded = true,
                            icon = {
                                val imageVector = route.icon ?: Icons.Default.QuestionMark
                                Icon(imageVector = imageVector, contentDescription = route.name)
                            },
                            label = { Text(route.name) },
                            selected = false,
                            onClick = { navigateTo(route) },
                        )
                    }
                }
                Spacer(Modifier.height(31.dp))
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavRailExpandedPreview() {
    SteamCompanionTheme {
        Column {
            SteamCompanionNavRail(
                currentTopLevelRoute = Route.Search,
                navigateTo = {},
                railState = rememberWideNavigationRailState(WideNavigationRailValue.Expanded),
                onRailButtonClicked = {},
                isWideScreen = false
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NavRailCollapsedPreview() {
    SteamCompanionTheme {
        Column {
            SteamCompanionNavRail(
                currentTopLevelRoute = Route.Search,
                navigateTo = {},
                railState = rememberWideNavigationRailState(WideNavigationRailValue.Collapsed),
                onRailButtonClicked = {},
                isWideScreen = false
            )
        }
    }
}
