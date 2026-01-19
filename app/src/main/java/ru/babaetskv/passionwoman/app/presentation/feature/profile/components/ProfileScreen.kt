package ru.babaetskv.passionwoman.app.presentation.feature.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.profile.menu.ContactsProfileMenuItem
import ru.babaetskv.passionwoman.app.presentation.feature.profile.menu.FavoritesProfileMenuItem
import ru.babaetskv.passionwoman.app.presentation.feature.profile.menu.OrdersProfileMenuItem
import ru.babaetskv.passionwoman.app.presentation.feature.profile.menu.ProfileMenuItem
import ru.babaetskv.passionwoman.app.presentation.theme.PassionWomanTheme
import ru.babaetskv.passionwoman.app.utils.ScreenPreviews
import ru.babaetskv.passionwoman.domain.model.Profile

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.profileLiveData.observeAsState()
    val menuItems by viewModel.menuItemsLiveData.observeAsState(emptyList())
    ProfileScreen(
        modifier = modifier,
        profile = profile,
        menuItems = menuItems,
        onEditAvatarClick = viewModel::onEditAvatarPressed,
        onEditProfileClick = viewModel::onEditPressed,
        onLogInClick = viewModel::onLogInPressed,
        onLogOutClick = viewModel::onLogOutPressed,
        onMenuItemClick = viewModel::onMenuItemPressed
    )
}

@Composable
fun ProfileScreen(
    profile: Profile?,
    menuItems: List<ProfileMenuItem>,
    onEditAvatarClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLogInClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onMenuItemClick: (item: ProfileMenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.margin_default)),
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(R.dimen.margin_default)
            )
        ) {
            profile?.let {
                ProfileHeader(
                    modifier = Modifier.fillMaxWidth(),
                    profile = it,
                    onEditAvatarClick = onEditAvatarClick,
                    onEditProfileClick = onEditProfileClick,
                    onLogOutClick = onLogOutClick,
                )
            } ?: run {
                ProfileHeaderPlaceholder(
                    modifier = Modifier.fillMaxWidth(),
                    onLogInClick =  onLogInClick
                )
            }

            for (item in menuItems) {
                ProfileMenuItem(
                    item = item,
                    onClick = {
                        onMenuItemClick.invoke(item)
                    }
                )
            }
        }
    }
}

@ScreenPreviews
@Composable
private fun PreviewProfileScreenProfile() {
    PassionWomanTheme {
        ProfileScreen(
            profile = Profile(
                id = 0,
                name = "Wolfgang",
                surname = "Mozart",
                phone = "wamozart56@mail.com",
                avatar = null
            ),
            menuItems = listOf(
                FavoritesProfileMenuItem(),
                OrdersProfileMenuItem(),
                ContactsProfileMenuItem()
            ),
            onEditAvatarClick = {},
            onEditProfileClick = {},
            onLogInClick = {},
            onLogOutClick = {},
            onMenuItemClick = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewProfileScreenPlaceholder() {
    PassionWomanTheme {
        ProfileScreen(
            profile = null,
            menuItems = listOf(
                FavoritesProfileMenuItem(),
                OrdersProfileMenuItem(),
                ContactsProfileMenuItem()
            ),
            onEditAvatarClick = {},
            onEditProfileClick = {},
            onLogInClick = {},
            onLogOutClick = {},
            onMenuItemClick = {}
        )
    }
}