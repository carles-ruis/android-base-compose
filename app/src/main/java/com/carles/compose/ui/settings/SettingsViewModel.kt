package com.carles.compose.ui.settings

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carles.compose.R
import com.carles.compose.model.SettingsUi
import com.carles.compose.domain.ObserveUserSettings
import com.carles.compose.domain.SetCacheExpirationTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val error: String? = null,
    val settings: SettingsUi? = null,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setCacheExpirationTime: SetCacheExpirationTime,
    private val observeUserSettings: ObserveUserSettings,
    private val settingsMapper: SettingsMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        observeUserSettings()
    }

    private fun observeUserSettings() {
        observeUserSettings.execute()
            .catch { error ->
                Log.w("SettingsViewModel", error)
                _uiState.update { it.copy(error = error.message) }
            }
            .onEach { settings ->
                _uiState.update { it.copy(settings = settingsMapper.toUi(settings), error = null) }
            }
            .launchIn(viewModelScope)
    }

    fun onSettingSelected(@StringRes key: Int, @StringRes selectedOption: Int) {
        when (key) {
            R.string.preferences_cache_key -> setCacheExpirationTime(settingsMapper.toCacheExpirationTimeValue(selectedOption))
        }
    }

    private fun setCacheExpirationTime(expirationTime: Int) {
        val exceptionHandler = CoroutineExceptionHandler { _, error ->
            Log.w("SettingsViewModel", error)
        }
        viewModelScope.launch(exceptionHandler) {
            setCacheExpirationTime.execute(expirationTime)
        }
    }
}