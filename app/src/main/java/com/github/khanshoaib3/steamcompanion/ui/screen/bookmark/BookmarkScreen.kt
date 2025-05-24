package com.github.khanshoaib3.steamcompanion.ui.screen.bookmark

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.khanshoaib3.steamcompanion.data.model.bookmark.Bookmark
import com.github.khanshoaib3.steamcompanion.ui.theme.SteamCompanionTheme
import com.github.khanshoaib3.steamcompanion.R
import com.github.khanshoaib3.steamcompanion.ui.navigation.SteamCompanionTopAppBar

@Composable
fun BookmarkScreenRoot(
    modifier: Modifier = Modifier,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel()
) {
    val bookmarkDataState by bookmarkViewModel.bookmarkDataState.collectAsState()
    BookmarkScreen(modifier, bookmarkDataState.bookmarks)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier,
    bookmarks: List<Bookmark>
) {
    Scaffold(
        topBar = {
            SteamCompanionTopAppBar(
                showMenuButton = false,
                onMenuButtonClick = {},
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        }
    ) { innerPadding ->
        Column(
            modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Row(
                    Modifier.weight(0.6f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("App", textAlign = TextAlign.Center)
                }
                Row(
                    Modifier.weight(0.2f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("AppId", textAlign = TextAlign.Center)
                }
                Row(
                    Modifier.weight(0.2f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Time", textAlign = TextAlign.Center)
                }
            }
            HorizontalDivider(Modifier.fillMaxWidth(0.95f))
            LazyColumn {
                items(bookmarks) {
                    Row(
                        Modifier.clickable(true, onClick = { /* TODO Add details page */ }),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            Modifier.weight(0.6f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                it.name,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Row(
                            Modifier.weight(0.2f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(it.appId.toString(), textAlign = TextAlign.Center)
                        }
                        Row(
                            Modifier.weight(0.2f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(it.timeStamp.toString(), textAlign = TextAlign.Center)
                        }
                    }
                }
            }
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
                Bookmark(
                    appId = 1231,
                    name = "Max Payne: The Fall of Max Payne",
                    timeStamp = 12341321
                )
            )
        )
    }
}