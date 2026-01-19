package ru.babaetskv.passionwoman.app.presentation.components

import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import ru.babaetskv.passionwoman.app.R

@Composable
fun PassionWomanCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        content = content,
        shape = MaterialTheme.shapes.small,
        elevation = dimensionResource(R.dimen.margin_extra_small)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PassionWomanCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        content = content,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        elevation = dimensionResource(R.dimen.margin_extra_small)
    )
}
