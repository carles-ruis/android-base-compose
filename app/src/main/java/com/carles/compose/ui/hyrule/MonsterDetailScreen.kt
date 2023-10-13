package com.carles.compose.ui.hyrule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.carles.compose.R
import com.carles.compose.model.MonsterDetail
import com.carles.compose.ui.composables.CenteredProgressIndicator
import com.carles.compose.ui.theme.HyruleTheme

@Composable
fun MonsterDetailScreen(viewModel: MonsterDetailViewModel, changeTitle: (String) -> Unit, navigateUp: () -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MonsterDetailScreen(uiState, changeTitle, navigateUp) { viewModel.retry() }
}

@Composable
private fun MonsterDetailScreen(
    uiState: MonsterDetailUiState,
    changeTitle: (String) -> Unit,
    navigateUp: () -> Unit,
    retry: () -> Unit
) {
    when {
        uiState.loading -> CenteredProgressIndicator()
        uiState.error != null -> ErrorDialog(Modifier, uiState.error, retry, navigateUp)
        else -> uiState.monster?.let {
            changeTitle(it.name)
            MonsterDetailContent(it)
        }
    }
}

@Composable
private fun MonsterDetailContent(monster: MonsterDetail, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MonsterDetailLocations(monster.commonLocations)
        MonsterDetailDescription(monster.description)
        MonsterDetailImage(monster.image)
    }
}

@Composable
private fun MonsterDetailLocations(locations: String, modifier: Modifier = Modifier) {
    Text(
        color = MaterialTheme.colorScheme.onSurface,
        text = locations,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .testTag(stringResource(R.string.tag_monster_locations))
    )
    Divider(color = MaterialTheme.colorScheme.outline)
}

@Composable
private fun MonsterDetailDescription(description: String, modifier: Modifier = Modifier) {
    Text(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        text = description,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .testTag(stringResource(R.string.tag_monster_description))
    )
    Divider(color = MaterialTheme.colorScheme.outline)
}

@Composable
private fun MonsterDetailImage(image: String, modifier: Modifier = Modifier) {
    SubcomposeAsyncImage(
        model = image,
        contentDescription = null,
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .testTag(stringResource(R.string.tag_monster_image))
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Error ->
                Text(
                    text = image,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

            is AsyncImagePainter.State.Success ->
                SubcomposeAsyncImageContent(modifier = Modifier.size(width = 240.dp, height = 240.dp))

            else -> Box(modifier = Modifier.size(width = 240.dp, height = 240.dp)) {
                CenteredProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun MonsterDetailPreview() {
    HyruleTheme {
        MonsterDetailContent(
            MonsterDetail(
                id = 120,
                name = "guardian scout iv",
                commonLocations = "Gerudo Highlands, Hebra Mountains",
                description = "These quick-witted, lizard-like monsters can be found all over Hyrule. The balls of ice they spit " +
                        "make them particularly troublesome, but exposure to fire will kill them instantly. Their homes are mainly on snowy mountains.",
                image = ""
            )
        )
    }
}
