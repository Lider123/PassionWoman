package ru.babaetskv.passionwoman.app.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import ru.babaetskv.passionwoman.app.R

val PassionWomanShapes: Shapes
    @Composable
    get() = Shapes(
        small = RoundedCornerShape(dimensionResource(R.dimen.corners_default)),
    )
