package com.github.khanshoaib3.steamcompanion.ui.screen.search.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputFieldHeight
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.SearchBarDefaults.inputFieldShape
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun SearchInputField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = inputFieldColors(),
    interactionSource: MutableInteractionSource? = null,
    focusManager: FocusManager? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val focused = interactionSource.collectIsFocusedAsState().value
    val focusRequester = remember { FocusRequester() }
    val focusManager = focusManager ?: LocalFocusManager.current

//    val searchSemantics = getString(Strings.SearchBarSearch)
//    val suggestionsAvailableSemantics = getString(Strings.SuggestionsAvailable)

    val textColor =
        LocalTextStyle.current.color.takeOrElse {
            textColor(enabled, isError = false, focused = focused)
        }

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier =
            modifier
                .sizeIn(
                    minWidth = SearchBarMinWidth,
                    maxWidth = SearchBarMaxWidth,
                    minHeight = InputFieldHeight,
                )
                .focusRequester(focusRequester)
                .semantics {
//                    contentDescription = searchSemantics
                },
        enabled = enabled,
        singleLine = true,
        textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor)),
        cursorBrush = SolidColor(cursorColor(isError = false)),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
        interactionSource = interactionSource,
        decorationBox =
            @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = query,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = placeholder,
                    leadingIcon =
                        leadingIcon?.let { leading ->
                            { Box(Modifier.offset(x = SearchBarIconOffsetX)) { leading() } }
                        },
                    trailingIcon =
                        trailingIcon?.let { trailing ->
                            { Box(Modifier.offset(x = -SearchBarIconOffsetX)) { trailing() } }
                        },
                    shape = SearchBarDefaults.inputFieldShape,
                    colors = colors,
                    contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(),
                    container = {
                        val containerColor =
                            animateColorAsState(
                                targetValue =
                                    containerColor(
                                        enabled = enabled,
                                        isError = false,
                                        focused = focused
                                    ),
//                                animationSpec = MotionSchemeKeyTokens.FastEffects.value(),
                            )
                        Box(
                            Modifier.textFieldBackground(containerColor::value, inputFieldShape)
                        )
                    },
                )
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Stable
@Composable
private fun textColor(
    enabled: Boolean,
    isError: Boolean,
    focused: Boolean,
): Color =
    when {
        !enabled -> inputFieldColors().disabledTextColor
        isError -> inputFieldColors().errorTextColor
        focused -> inputFieldColors().focusedTextColor
        else -> inputFieldColors().unfocusedTextColor
    }

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Stable
private fun cursorColor(isError: Boolean): Color =
    if (isError) inputFieldColors().errorCursorColor else inputFieldColors().cursorColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Stable
private fun containerColor(
    enabled: Boolean,
    isError: Boolean,
    focused: Boolean,
): Color =
    when {
        !enabled -> inputFieldColors().disabledContainerColor
        isError -> inputFieldColors().errorContainerColor
        focused -> inputFieldColors().focusedContainerColor
        else -> inputFieldColors().unfocusedContainerColor
    }

/**
 * Replacement for Modifier.background which takes color lazily to avoid recomposition while
 * animating.
 */
private fun Modifier.textFieldBackground(
    color: ColorProducer,
    shape: Shape,
): Modifier =
    this.drawWithCache {
        val outline = shape.createOutline(size, layoutDirection, this)
        onDrawBehind { drawOutline(outline, color = color()) }
    }

private val SearchBarIconOffsetX: Dp = 4.dp
internal val SearchBarMinWidth: Dp = 360.dp
private val SearchBarMaxWidth: Dp = 720.dp
