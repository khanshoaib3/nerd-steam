package com.github.khanshoaib3.steamcompanion.ui.screen.bookmark

import android.content.res.Configuration
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import coil3.compose.AsyncImage
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionTopAppBar
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme

@Composable
fun BookmarkScreenRoot(
    modifier: Modifier = Modifier,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel(),
    currentDestination: NavDestination?,
    onMenuButtonClick: () -> Unit
) {
    val localView = LocalView.current
    val sortedBookmarks by bookmarkViewModel.sortedBookmarks.collectAsState()

    BookmarkScreen(
        modifier = modifier,
        bookmarks = sortedBookmarks,
        currentDestination = currentDestination,
        onMenuButtonClick = onMenuButtonClick,
        onGameHeaderClick = {
            bookmarkViewModel.toggleSortOrderOfTypeName()
            localView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        },
        onTimeHeaderClick = {
            bookmarkViewModel.toggleSortOrderOfTypeTime()
            localView.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier,
    bookmarks: List<BookmarkDisplay>,
    currentDestination: NavDestination?,
    onMenuButtonClick: () -> Unit,
    onGameHeaderClick: () -> Unit,
    onTimeHeaderClick: () -> Unit
) {
    val density: Density = LocalDensity.current
    val imageWidth: Dp
    val imageHeight: Dp
    with(density) {
        imageWidth = 150.toDp()
        imageHeight = 225.toDp()
    }

    Scaffold(
        topBar = {
            SteamCompanionTopAppBar(
                showMenuButton = true,
                onMenuButtonClick = onMenuButtonClick,
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                currentDestination = currentDestination
            )
        }
    ) { innerPadding ->
        Column(
            modifier
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BookmarkTableHeader(
                onGameHeaderClick = onGameHeaderClick,
                onTimeHeaderClick = onTimeHeaderClick
            )
            HorizontalDivider(
                Modifier.padding(
                    horizontal = dimensionResource(R.dimen.padding_medium),
                    vertical = dimensionResource(R.dimen.padding_very_small)
                )
            )
            BookmarkTableBody(
                bookmarks = bookmarks,
                imageWidth = imageWidth,
                imageHeight = imageHeight
            )
        }
    }
}

@Composable
fun BookmarkTableBody(
    modifier: Modifier = Modifier,
    bookmarks: List<BookmarkDisplay>,
    imageWidth: Dp,
    imageHeight: Dp
) {
    LazyColumn(modifier) {
        items(bookmarks) { bookmark ->
            Row(
                Modifier.clickable(true, onClick = { /* TODO Add details page */ }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    Modifier.weight(0.6f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = "https://cdn.cloudflare.steamstatic.com/steam/apps/${bookmark.appId}/library_600x900.jpg",
                        contentDescription = bookmark.name,
                        placeholder = painterResource(R.drawable.preview_image_300x450),
                        modifier = Modifier
                            .size(width = imageWidth, height = imageHeight)
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                    )
                    Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
                    CenterAlignedSelectableText(
                        text = bookmark.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
                CenterAlignedSelectableText(
                    modifier = Modifier.weight(0.2f),
                    text = bookmark.appId,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                CenterAlignedSelectableText(
                    modifier = Modifier.weight(0.2f),
                    text = bookmark.formattedTime,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BookmarkTableHeader(
    onGameHeaderClick: () -> Unit,
    onTimeHeaderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO Add a11y indication of sorting order
    Row(modifier) {
        Row(
            Modifier
                .weight(0.6f)
                .clickable(true, onClick = onGameHeaderClick, role = Role.Button),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Game",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
        Row(
            Modifier.weight(0.2f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "AppId",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
        Row(
            Modifier
                .weight(0.2f)
                .clickable(true, onClick = onTimeHeaderClick, role = Role.Button),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Time",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BookmarkScreenPreview() {
    SteamCompanionTheme {
        BookmarkScreen(
            bookmarks = listOf(
                BookmarkDisplay(
                    appId = "1231",
                    name = "Max Payne: The Fall of Max Payne",
                    formattedTime = "dd MMM yyyy"
                )
            ),
            currentDestination = null,
            onMenuButtonClick = {},
            onGameHeaderClick = {},
            onTimeHeaderClick = {}
        )
    }
}