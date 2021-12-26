package com.jamal.composeprefs.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
@ExperimentalMaterialApi
fun PrefsListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    darkenOnDisable: Boolean = true,
    icon: @Composable (() -> Unit)? = null,
    secondaryText: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    textColor: Color = LocalTextStyle.current.color,
    minimalHeight: Boolean = false,
    text: @Composable () -> Unit
) {

    val typography = MaterialTheme.typography

    val styledText = applyTextStyle(
        typography.subtitle1,
        textColor,
        when {
            enabled || !darkenOnDisable -> ContentAlpha.high
            else -> ContentAlpha.disabled
        },
        text
    )!!
    val styledSecondaryText = applyTextStyle(
        typography.body2,
        textColor,
        when {
            enabled || !darkenOnDisable -> ContentAlpha.medium
            else -> ContentAlpha.disabled
        },
        secondaryText
    )
    val styledTrailing = applyTextStyle(
        typography.caption,
        textColor,
        when {
            enabled || !darkenOnDisable -> ContentAlpha.high
            else -> ContentAlpha.disabled
        },
        trailing
    )

    AnyLine.CustomListItem(
        modifier = modifier,
        minimalHeight,
        icon,
        styledSecondaryText,
        styledTrailing,
        styledText
    )
}

private object AnyLine {
    private val MinHeight = 48.dp
    private val MinHeightSmaller = 32.dp
    private val IconMinPaddedWidth = 40.dp
    private val ContentPadding = 16.dp

    @Composable
    fun CustomListItem(
        modifier: Modifier = Modifier,
        minimalHeight: Boolean = true,
        icon: @Composable (() -> Unit)? = null,
        secondaryText: (@Composable (() -> Unit))? = null,
        trailing: @Composable (() -> Unit)? = null,
        text: @Composable (() -> Unit)
    ) {
        Row(
            modifier
                .heightIn(min = if (minimalHeight) MinHeightSmaller else MinHeight)
                .padding(
                    start = ContentPadding,
                    end = ContentPadding,
                    top = ContentPadding,
                    bottom = if (minimalHeight) 0.dp else ContentPadding
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (icon != null) {
                val minSize = IconMinPaddedWidth
                Box(
                    Modifier.sizeIn(minWidth = minSize, minHeight = minSize),
                    contentAlignment = Alignment.CenterStart
                ) { icon() }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.8f)
            ) {
                text()
                secondaryText?.invoke()
            }
            trailing?.invoke()
        }
    }
}

private fun applyTextStyle(
    textStyle: TextStyle,
    textColor: Color,
    contentAlpha: Float,
    content: @Composable (() -> Unit)?
): @Composable (() -> Unit)? {
    if (content == null) return null
    return {
        val newTextStyle = textStyle.copy(
            color = textColor.copy(alpha = contentAlpha)
        )
        CompositionLocalProvider(
            LocalContentAlpha provides contentAlpha,
            LocalTextStyle provides newTextStyle
        ) {
            content()
        }
    }
}



