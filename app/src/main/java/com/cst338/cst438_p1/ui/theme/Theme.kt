package com.cst338.cst438_p1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.cst338.cst438_p1.Background
import com.cst338.cst438_p1.Bar
import com.cst338.cst438_p1.Button
import com.cst338.cst438_p1.ButtonText
import com.cst338.cst438_p1.Text

private val AppColors = lightColorScheme(
    primary = Button,
    onPrimary = ButtonText,
    background = Background,
    primaryContainer = Bar,
    onPrimaryContainer = Text,
    onSurface = Text
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        content = content
    )
}