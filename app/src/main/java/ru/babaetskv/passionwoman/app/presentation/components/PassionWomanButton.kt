package ru.babaetskv.passionwoman.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.theme.PassionWomanTheme

@Composable
fun PassionWomanButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onPrimary
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        contentPadding = PaddingValues(dimensionResource(R.dimen.margin_small)),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Text(text)
    }
}

@Composable
fun PassionWomanButton(
    painter: Painter,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IconButton(
        modifier = modifier
            .background(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colors.primary
            ),
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = MaterialTheme.colors.onPrimary
        )
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
private fun PassionWomanButtonIconPreview() {
    PassionWomanTheme {
        PassionWomanButton(
            painter = painterResource(R.drawable.ic_edit),
            contentDescription = null,
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
