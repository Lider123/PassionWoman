package ru.babaetskv.passionwoman.app.presentation.feature.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.components.PassionWomanButton
import ru.babaetskv.passionwoman.app.presentation.components.PassionWomanCard
import ru.babaetskv.passionwoman.app.presentation.theme.PassionWomanTheme
import ru.babaetskv.passionwoman.app.utils.ComponentPreviews
import ru.babaetskv.passionwoman.domain.model.Profile

@Composable
fun ProfileHeader(
    profile: Profile,
    onEditAvatarClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLogOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileHeader(
        modifier = modifier,
        name = profile.name,
        subtitle = profile.phone,
        avatarUrl = profile.avatar?.url,
        avatarAction = AvatarAction(
            painter = painterResource(R.drawable.ic_edit),
            contentDescription = stringResource(R.string.profile_edit_avatar),
            onClick = onEditAvatarClick
        ),
        actions = listOf(
            profileAction(
                title = stringResource(R.string.profile_edit),
                onClick = onEditProfileClick
            ),
            profileAction(
                title = stringResource(R.string.profile_log_out),
                onClick = onLogOutClick,
                backgroundColor = MaterialTheme.colors.error,
                contentColor = MaterialTheme.colors.onError
            )
        )
    )
}

@Composable
fun ProfileHeaderPlaceholder(
    onLogInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileHeader(
        modifier = modifier,
        name = stringResource(R.string.profile_guest),
        subtitle = stringResource(R.string.profile_guest_welcome),
        avatarUrl = null,
        avatarAction = null,
        actions = listOf(
            profileAction(
                title = stringResource(R.string.profile_log_in),
                onClick = onLogInClick
            ),
        )
    )
}

@Composable
private fun ProfileHeader(
    name: String,
    subtitle: String,
    avatarUrl: String?,
    avatarAction: AvatarAction?,
    actions: List<ProfileAction>,
    modifier: Modifier = Modifier
) {
    PassionWomanCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(
                    all = dimensionResource(R.dimen.margin_default)
                ),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.margin_default)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PassionWomanCard {
                Box {
                    AsyncImage(
                        modifier = Modifier
                            .size(128.dp)
                            .clip(MaterialTheme.shapes.small),
                        model = avatarUrl,
                        placeholder = painterResource(R.drawable.ic_profile),
                        error = painterResource(R.drawable.ic_profile),
                        contentDescription = null
                    )

                    avatarAction?.let {
                        PassionWomanButton(
                            modifier = Modifier.align(Alignment.BottomEnd),
                            painter = it.painter,
                            contentDescription = it.contentDescription,
                            onClick = it.onClick
                        )
                    }
                }
            }

            Column {
                Text(
                    style = MaterialTheme.typography.h6,
                    text = stringResource(R.string.profile_greeting_template, name)
                )

                Text(
                    style = MaterialTheme.typography.subtitle1,
                    text = subtitle
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.margin_small)))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(
                        space = dimensionResource(R.dimen.margin_small),
                    )
                ) {
                    actions.forEach { action ->
                        PassionWomanButton(
                            onClick = action.onClick,
                            text = action.title,
                            backgroundColor = action.backgroundColor,
                            contentColor = action.contentColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun profileAction(
    title: String,
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onPrimary
) =
    ProfileAction(
        title = title,
        onClick = onClick,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )

@ComponentPreviews
@Composable
private fun PreviewProfileHeader() {
    PassionWomanTheme {
        ProfileHeader(
            name = "Wolfgang",
            subtitle = "wamozart56@mail.com",
            avatarUrl = "https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?s=200",
            avatarAction = AvatarAction(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = stringResource(R.string.profile_edit_avatar),
                onClick = {}
            ),
            actions = listOf(
                profileAction(
                    title = stringResource(R.string.profile_edit),
                    onClick = {}
                ),
                profileAction(
                    title = stringResource(R.string.profile_log_out),
                    onClick = {},
                    backgroundColor = MaterialTheme.colors.error,
                    contentColor = MaterialTheme.colors.onError
                )
            ),
        )
    }
}

@ComponentPreviews
@Composable
private fun PreviewProfileHeaderPlaceholder() {
    PassionWomanTheme {
        ProfileHeaderPlaceholder(
            onLogInClick = {}
        )
    }
}

private data class AvatarAction(
    val painter: Painter,
    val contentDescription: String,
    val onClick: () -> Unit
)

private data class ProfileAction(
    val title: String,
    val onClick: () -> Unit,
    val backgroundColor: Color,
    val contentColor: Color
)
