package com.gemasr.surgeonwizard.procedures.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gemasr.surgeonwizard.design.R as designR
import com.gemasr.surgeonwizard.design.theme.SurgeonWizardTheme
import com.gemasr.surgeonwizard.procedures.R
import com.gemasr.surgeonwizard.procedures.detail.ui.model.ProcedureDetailUI

@Composable
fun ProcedureDetailContent(procedureDetail: ProcedureDetailUI, onFavoriteToggle: () -> Unit) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .testTag(ProcedureDetailTestTags.CONTENT_VIEW),
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            AsyncImage(
                model = procedureDetail.cardImageUrl,
                contentDescription = stringResource(R.string.procedure_image_content_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(id = designR.drawable.wizard_placeholder),
            )
            IconToggleButton(
                checked = procedureDetail.isFavorite,
                onCheckedChange = { onFavoriteToggle() },
                modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
            ) {
                Icon(
                    imageVector = if (procedureDetail.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.favorite_button_content_description),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag(ProcedureDetailTestTags.FAVORITE_ICON),
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = procedureDetail.name,
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.duration_format, procedureDetail.durationInMinutes),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = stringResource(R.string.created_format, procedureDetail.datePublished),
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.phases_title),
                style = MaterialTheme.typography.titleMedium,
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(procedureDetail.phases) { phase ->
                    PhaseItem(phase)
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun PhaseItem(phase: ProcedureDetailUI.PhaseUI) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(150.dp).testTag(ProcedureDetailTestTags.PHASE_ITEM),
    ) {
        AsyncImage(
            model = phase.iconUrl,
            contentDescription = stringResource(R.string.phase_thumbnail_content_description),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = designR.drawable.wizard_placeholder),
            modifier =
            Modifier
                .size(150.dp)
                .aspectRatio(1f),
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = phase.name,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

// region Preview

@Composable
@Preview(name = "Light Theme", showBackground = true)
fun ProcedureDetailContentPreview() {
    val sampleProcedureDetail =
        ProcedureDetailUI(
            id = "1",
            name = "Sample Procedure",
            phases =
            listOf(
                ProcedureDetailUI.PhaseUI("1", "Phase 1", "https://example.com/icon1.jpg"),
                ProcedureDetailUI.PhaseUI("2", "Phase 2", "https://example.com/icon2.jpg"),
                ProcedureDetailUI.PhaseUI("3", "Phase 3", "https://example.com/icon3.jpg"),
            ),
            cardImageUrl = "https://example.com/procedure_image.jpg",
            datePublished = "01/01/2024",
            durationInMinutes = 60,
            isFavorite = false,
        )

    SurgeonWizardTheme {
        ProcedureDetailContent(
            procedureDetail = sampleProcedureDetail,
            onFavoriteToggle = {},
        )
    }
}
// endregion Preview