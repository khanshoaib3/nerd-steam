package com.github.khanshoaib3.nerdsteam.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImagePainter.Companion.DefaultTransform
import coil3.compose.AsyncImagePainter.State
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageScope
import com.github.khanshoaib3.nerdsteam.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MonochromeAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alternateImageModifier: Modifier = Modifier,
    transform: (State) -> State = DefaultTransform,
    loading: @Composable (SubcomposeAsyncImageScope.(State.Loading) -> Unit)? = null,
    success: @Composable (SubcomposeAsyncImageScope.(State.Success) -> Unit)? = null,
    error: @Composable (SubcomposeAsyncImageScope.(State.Error) -> Unit)? = null,
    onLoading: ((State.Loading) -> Unit)? = null,
    onSuccess: ((State.Success) -> Unit)? = null,
    onError: ((State.Error) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
) = SubcomposeAsyncImage(
    model = model,
    contentDescription = contentDescription,
    modifier = modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small))),
    transform = transform,
    loading = loading ?: {
        Box(
            contentAlignment = Alignment.Center,
            modifier = alternateImageModifier.background(MaterialTheme.colorScheme.surfaceContainerHighest)
        ) {
            LoadingIndicator()
        }
    },
    success = success,
    error = error ?: {
        Box(
            contentAlignment = Alignment.Center,
            modifier = alternateImageModifier.background(MaterialTheme.colorScheme.surfaceContainerHighest)
        ) {
            Image(
                painter = painterResource(R.mipmap.ic_launcher_monochrome),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }
    },
    onLoading = onLoading,
    onSuccess = onSuccess,
    onError = onError,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter,
    filterQuality = filterQuality,
)
