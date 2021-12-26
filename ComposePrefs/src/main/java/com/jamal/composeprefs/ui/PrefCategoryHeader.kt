package com.jamal.composeprefs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PrefsCategoryHeader(
    title: String
) {
    Box(
        Modifier
            .padding(
                start = StartPadding,
            )
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            title,
            color = MaterialTheme.colors.primary,
            fontSize = LocalTextStyle.current.fontSize.times(FontSizeMultiplier),
            fontWeight = FontWeight.SemiBold
        )
    }
}


private val StartPadding = 16.dp
private const val FontSizeMultiplier = 0.85f