package com.carles.compose.ui.hyrule

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.carles.compose.R
import com.carles.compose.data.remote.MonstersResponseDto
import com.carles.compose.model.Monster
import com.carles.compose.model.mapper.MonstersMapper
import com.carles.compose.ui.composables.CenteredProgressIndicator
import com.carles.compose.ui.extensions.debounceClickable
import com.carles.compose.ui.extensions.disableSplitMotionEvents
import com.carles.compose.ui.theme.HyruleTheme
import com.google.gson.Gson

@Composable
fun MonstersScreen(viewModel: MonstersViewModel, onMonsterClick: (Int) -> Unit) {
    val uiState: MonstersUiState by viewModel.uiState.collectAsStateWithLifecycle()
    MonstersScreen(uiState, onMonsterClick) { viewModel.retry() }
}

@Composable
fun MonstersScreen(state: MonstersUiState, onMonsterClick: (Int) -> Unit, onRetryClick: () -> Unit) {
    when (state) {
        is MonstersUiState.Loading -> CenteredProgressIndicator()
        is MonstersUiState.Error -> ErrorDialog(message = state.message, onRetryClick = onRetryClick)
        is MonstersUiState.Data -> MonstersList(monsters = state.monsters, onMonsterClick = onMonsterClick)
    }
}

@Composable
private fun MonstersList(monsters: List<Monster>, modifier: Modifier = Modifier, onMonsterClick: (Int) -> Unit = {}) {
    val state = rememberLazyListState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .disableSplitMotionEvents()
            .testTag(stringResource(R.string.tag_monsters_list)),
        state = state,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        itemsIndexed(
            items = monsters,
            key = { _: Int, monster: Monster -> monster.id }) { index: Int, monster: Monster ->
            MonstersRow(monster, index < monsters.lastIndex, onMonsterClick)
        }
    }
}

@Composable
private fun MonstersRow(monster: Monster, showDivider: Boolean, onMonsterClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Text(
        text = monster.name,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier
            .debounceClickable { onMonsterClick(monster.id) }
            .padding(16.dp)
            .fillMaxWidth(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.primary,
    )
    if (showDivider) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outline,
            thickness = 1.dp
        )
    }
}

@Preview
@Composable
private fun MonstersListPreview() {
    val mapper = MonstersMapper()
    val monsters = mapper.toEntity(Gson().fromJson(MONSTERS_RESPONSE, MonstersResponseDto::class.java)).let { entities ->
        mapper.fromEntity(entities)
    }
    HyruleTheme {
        MonstersList(monsters)
    }
}

private const val MONSTERS_RESPONSE = """
    {
  "data": [
    { "id": 120, "name": "silver lizalfos" },
    { "id": 121, "name": "guardian scout iv" },
    { "id": 122, "name": "ice-breath lizalfos" },
    { "id": 123, "name": "stone talus (rare)" },
    { "id": 124, "name": "lizalfos" } 
    ] }
    """