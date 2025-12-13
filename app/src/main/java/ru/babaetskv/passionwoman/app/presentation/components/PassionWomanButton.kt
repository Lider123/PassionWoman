package ru.babaetskv.passionwoman.app.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.theme.PassionWomanTheme

@Composable
fun PassionWomanButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        contentPadding = PaddingValues(dimensionResource(R.dimen.margin_small)),
        onClick = onClick
    ) {
        Text(text)
    }
}

@Preview
@Composable
private fun PassionWomanButtonPreview() {
    PassionWomanTheme {
        PassionWomanButton(
            text = "Test",
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PassionWomanButtonPreviewDisabled() {
    PassionWomanTheme {
        PassionWomanButton(
            text = "Test",
            enabled = false,
            onClick = {}
        )
    }
}
