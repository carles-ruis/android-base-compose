package com.carles.compose.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.carles.compose.ui.theme.HyruleTheme

@Composable
fun CenteredProgressIndicator(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun CenteredProgressIndicatorPreview() {
    HyruleTheme {
        CenteredProgressIndicator()
    }
}