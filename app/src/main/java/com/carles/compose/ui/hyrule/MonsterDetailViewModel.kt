package com.carles.compose.ui.hyrule

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carles.compose.model.MonsterDetail
import com.carles.compose.ui.Screen
import com.carles.compose.domain.GetMonsterDetail
import com.carles.compose.ui.Arguments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MonsterDetailUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val monster: MonsterDetail? = null,
)

@HiltViewModel
class MonsterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMonsterDetail: GetMonsterDetail
) : ViewModel() {

    private val id = savedStateHandle.get<String>(Arguments.monsterId)?.toInt() ?: 0

    private val _uiState = MutableStateFlow(MonsterDetailUiState())
    val uiState: StateFlow<MonsterDetailUiState> = _uiState

    init {
        getMonsterDetail()
    }

    private fun getMonsterDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            try {
                val monster = getMonsterDetail.execute(id)
                _uiState.update { it.copy(loading = false, error = null, monster = monster) }
            } catch (e: Exception) {
                Log.w("MonsterDetailViewModel", e)
                _uiState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

    fun retry() {
        getMonsterDetail()
    }
}