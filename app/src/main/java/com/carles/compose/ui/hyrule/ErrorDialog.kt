package com.carles.compose.ui.hyrule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.carles.compose.R
import com.carles.compose.ui.theme.HyruleTheme

@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
    message: String? = null,
    onRetryClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onRetryClick) { Text(stringResource(R.string.error_retry)) }
        },
        text = {
            Text(
                text = message ?: stringResource(R.string.error_server_response),
                style = MaterialTheme.typography.bodyLarge
            )
        })
}

@Preview
@Composable
private fun ErrorDialog_CustomError() {
    HyruleTheme {
        Box(Modifier.fillMaxSize()) {
            ErrorDialog(message = "Error per que ho dic jo")
        }
    }
}

@Preview
@Composable
private fun ErrorDialog_DefaultError() {
    HyruleTheme {
        Box(Modifier.fillMaxSize()) {
            ErrorDialog()
        }
    }
}


