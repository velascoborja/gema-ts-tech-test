package com.gemasr.surgeonwizard.procedures.list.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gemasr.surgeonwizard.design.R as designR
import com.gemasr.surgeonwizard.procedures.R
import com.gemasr.surgeonwizard.procedures.list.ProcedureListError

@Composable
fun ProcedureListErrorView(error: ProcedureListError, isShowingOnlyFavorites: Boolean, onRetry: () -> Unit) {
    var showRetry by remember { mutableStateOf((isShowingOnlyFavorites && error is ProcedureListError.EmptyList).not()) }

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = designR.drawable.wizard_error),
            contentDescription = "Error Image",
            modifier = Modifier.size(300.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text =
            when (error) {
                ProcedureListError.NoInternetError -> stringResource(R.string.network_error_message)
                ProcedureListError.EmptyList ->
                    if (isShowingOnlyFavorites) {
                        stringResource(R.string.favorite_empty)
                    } else {
                        stringResource(R.string.server_error_message)
                    }
                ProcedureListError.UnidentifiedError -> stringResource(R.string.unknown_error_message)
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (showRetry) {
            Button(
                onClick = onRetry,
                modifier = Modifier.widthIn(min = 200.dp),
            ) {
                Text(text = stringResource(R.string.retry_button_text))
            }
        }
    }
}