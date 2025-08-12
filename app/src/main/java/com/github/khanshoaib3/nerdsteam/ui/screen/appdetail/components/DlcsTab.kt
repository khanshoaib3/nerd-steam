package com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import com.github.khanshoaib3.nerdsteam.R
import com.github.khanshoaib3.nerdsteam.data.model.appdetail.Dlc
import com.github.khanshoaib3.nerdsteam.ui.components.CenterAlignedSelectableText
import com.github.khanshoaib3.nerdsteam.ui.components.ErrorColumn
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.AppData
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.AppViewState
import com.github.khanshoaib3.nerdsteam.ui.screen.appdetail.DataType
import com.github.khanshoaib3.nerdsteam.utils.OpenWebPage
import com.github.khanshoaib3.nerdsteam.utils.Progress
import com.github.khanshoaib3.nerdsteam.utils.getNumberFormatFromCurrencyCode

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DlcsTab(
    appData: AppData,
    appViewState: AppViewState,
    fetchDataFromSourceCallback: (DataType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (appViewState.dlcsStatus) {
            Progress.NOT_QUEUED -> {
                LaunchedEffect(appData.steamAppId) {
                    fetchDataFromSourceCallback(DataType.DLCS)
                }
            }

            Progress.LOADING -> {
                LoadingIndicator()
            }

            is Progress.FAILED -> {
                ErrorColumn(reason = appViewState.dlcsStatus.reason)
            }

            Progress.LOADED -> {
                if (appData.dlcs.isNullOrEmpty()) {
                    ErrorColumn(
                        reason = null,
                        title = "No DLCs found!",
                    )
                } else appData.dlcs.let { dlcs ->
                    dlcs.forEach {
                        DlcRow(
                            dlc = it,
                            modifier = Modifier.clickable(role = Role.Tab) {
                                OpenWebPage(it.steamUrl, context)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun DlcRow(
    dlc: Dlc,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.95f)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_very_small)))
            .height(IntrinsicSize.Max)
    ) {
        Row {
            AsyncImage(
                model = dlc.imageUrl,
                contentDescription = dlc.name,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxHeight()
            )
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            ) {
                CenterAlignedSelectableText(
                    text = dlc.name,
                    style = MaterialTheme.typography.titleMediumEmphasized,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
                if (dlc.price != null && dlc.currencyCode != null) {
                    val currencyFormatter = getNumberFormatFromCurrencyCode(dlc.currencyCode)
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
                    CenterAlignedSelectableText(
                        text = currencyFormatter.format(dlc.price),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}
