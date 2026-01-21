package ru.babaetskv.passionwoman.app.presentation.feature.profile.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.components.PassionWomanCard
import ru.babaetskv.passionwoman.app.presentation.feature.profile.menu.ProfileMenuItem
import ru.babaetskv.passionwoman.app.presentation.theme.PassionWomanTheme
import ru.babaetskv.passionwoman.app.utils.ComponentPreviews

@Composable
fun ProfileMenuItem(
    icon: Painter,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
) {
    PassionWomanCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(
                    all = dimensionResource(R.dimen.margin_default)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                tint = iconTint,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier
                    .width(
                        width = dimensionResource(R.dimen.margin_small)
                    )
            )

            Text(
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.h5,
                text = title
            )

            Spacer(
                modifier = Modifier
                    .width(
                        width = dimensionResource(R.dimen.margin_small)
                    )
            )

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    item: ProfileMenuItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileMenuItem(
        modifier = modifier,
        icon = item.getIconPainter(),
        title = item.getTitle(LocalContext.current),
        onClick = onClick
    )
}

@ComponentPreviews
@Composable
private fun PreviewProfileMenuItem() {
    PassionWomanTheme {
        ProfileMenuItem(
            icon = painterResource(R.drawable.ic_favorites),
            iconTint = Color.Red,
            title = "Favourites",
            onClick = {}
        )
    }
}
