package com.gemasr.surgeonwizard.procedures.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureItem

@Composable
fun ProcedureItem(procedure: ProcedureItem, onItemClick: () -> Unit, onFavoriteToggle: () -> Unit, showFavoriteButton: Boolean = false) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick() })
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = procedure.iconUrl,
            contentDescription = null,
            modifier =
            Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = procedure.name,
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = "Fases: ${procedure.phaseCount}",
                style = MaterialTheme.typography.bodySmall,
            )
        }

        if (!showFavoriteButton) {
            IconToggleButton(
                checked = procedure.isFavorite,
                onCheckedChange = { onFavoriteToggle() },
            ) {
                Icon(
                    imageVector = if (procedure.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorito",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProcedureItemPreview() {
    Column {
        ProcedureItem(
            procedure =
            ProcedureItem(
                id = "1",
                iconUrl = "https://example.com/icon.png",
                name = "Procedimiento de ejemplo",
                duration = 30,
                phaseCount = 3,
                isFavorite = true,
            ),
            onItemClick = {},
            onFavoriteToggle = {},
            showFavoriteButton = false,
        )

        ProcedureItem(
            procedure =
            ProcedureItem(
                id = "2",
                iconUrl = "https://example.com/icon2.png",
                name = "Procedimiento favorito",
                duration = 45,
                phaseCount = 5,
                isFavorite = true,
            ),
            onItemClick = {},
            onFavoriteToggle = {},
            showFavoriteButton = true,
        )
    }
}