package com.gemasr.surgeonwizard.procedures.detail.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.gemasr.surgeonwizard.design.theme.SurgeonWizardTheme
import com.gemasr.surgeonwizard.procedures.R
import com.gemasr.surgeonwizard.procedures.detail.ProcedureDetailError

@Composable
fun ProcedureDetailError(error: ProcedureDetailError) {
    Text(
        text =
        when (error) {
            ProcedureDetailError.NotFound -> stringResource(R.string.detail_not_found_message)
            ProcedureDetailError.UnidentifiedError -> stringResource(R.string.unknown_error_message)
        },
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.testTag(ProcedureDetailTestTags.ERROR_VIEW),
    )
}

// region Preview

@Composable
@Preview(showBackground = true)
fun ProcedureError() {
    SurgeonWizardTheme {
        ProcedureDetailError(error = ProcedureDetailError.NotFound)
    }
}

// endregion Preview