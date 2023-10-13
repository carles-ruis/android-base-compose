package com.carles.compose.ui.settings

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carles.compose.R
import com.carles.compose.model.Setting
import com.carles.compose.model.SettingsCategory
import com.carles.compose.model.SettingsUi
import com.carles.compose.model.UserSettings
import com.carles.compose.ui.theme.HyruleTheme

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val uiState: SettingsUiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(
        uiState = uiState,
        onSelected = { key, selectedOption -> viewModel.onSettingSelected(key, selectedOption) })
}

@Composable
fun SettingsScreen(uiState: SettingsUiState, onSelected: (Int, Int) -> Unit = { _, _ -> }) {
    uiState.error?.let {
        Toast.makeText(LocalContext.current, uiState.error, Toast.LENGTH_LONG).show()
    }
    uiState.settings?.let {
        SettingsContent(it, onSelected)
    }
}

@Composable
fun SettingsContent(settings: SettingsUi, onSelected: (Int, Int) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        settings.forEach { category ->
            SettingsCategory(category, onSelected)
        }
    }
}

@Composable
private fun SettingsCategory(category: SettingsCategory, onSelected: (Int, Int) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = stringResource(category.title),
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp, start = 72.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        )
        category.settings.forEach { setting ->
            when (setting) {
                is Setting.ListSetting -> SettingList(setting, onSelected)
            }
        }
    }
}

@Composable
private fun SettingList(setting: Setting.ListSetting, onSelected: (Int, Int) -> Unit, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier
        .clickable { showDialog = true }
        .padding(top = 16.dp, bottom = 16.dp, start = 72.dp)) {
        Text(
            text = stringResource(setting.title),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = stringResource(setting.value),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    if (showDialog) {
        ListSettingDialog(
            setting = setting,
            onDismiss = { showDialog = false },
            onSelected = onSelected
        )
    }
}

@Composable
private fun ListSettingDialog(
    setting: Setting.ListSetting,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onSelected: (Int, Int) -> Unit = { _, _ -> }
) {
    AlertDialog(
        modifier = modifier.testTag(stringResource(R.string.tag_settings_dialog)),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onDismiss) { Text(stringResource(android.R.string.cancel)) }
        },
        title = {
            Text(
                text = stringResource(id = setting.title),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(Modifier.selectableGroup()) {
                setting.options.forEach { option ->
                    ListSettingDialogRow(
                        currentSelection = setting.value,
                        option = option,
                        onSelected = {
                            onSelected(setting.key, option)
                            onDismiss()
                        })
                }
            }
        })
}

@Composable
private fun ListSettingDialogRow(
    currentSelection: Int,
    @StringRes option: Int,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
            .selectable(
                selected = currentSelection == option,
                onClick = { onSelected() },
                role = Role.RadioButton
            )
    ) {
        RadioButton(
            selected = currentSelection == option,
            onClick = null
        )
        Text(
            text = stringResource(id = option),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
private fun SettingsScreen_CacheExpirationTenMinutes() {
    val settings = SettingsMapper().toUi(UserSettings(cacheExpirationTime = 10))
    HyruleTheme {
        SettingsScreen(SettingsUiState(settings = settings))
    }
}

@Preview
@Composable
private fun ListSettingDialog_SelectCacheExpiration() {
    HyruleTheme {
        ListSettingDialog(
            Setting.ListSetting(
                key = R.string.preferences_cache_key,
                title = R.string.preferences_cache_expiration,
                value = R.string.preferences_cache_dont_use,
                options = listOf(
                    R.string.preferences_cache_dont_use,
                    R.string.preferences_cache_one_minute,
                    R.string.preferences_cache_ten_minutes,
                    R.string.preferences_cache_never
                )
            )
        )
    }
}