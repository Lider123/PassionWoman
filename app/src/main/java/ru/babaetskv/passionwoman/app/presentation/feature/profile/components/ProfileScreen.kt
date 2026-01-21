package ru.babaetskv.passionwoman.app.presentation.feature.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.profile.menu.ContactsProfileMenuItem
import ru.babaetskv.passionwoman.app.presentation.feature.profile.menu.FavoritesProfileMenuItem
import ru.babaetskv.passionwoman.app.presentation.feature.profile.menu.OrdersProfileMenuItem
import ru.babaetskv.passionwoman.app.presentation.feature.profile.menu.ProfileMenuItem
import ru.babaetskv.passionwoman.app.presentation.theme.PassionWomanTheme
import ru.babaetskv.passionwoman.app.utils.DeviceType
import ru.babaetskv.passionwoman.app.utils.LARGE_TABLET_LANDSCAPE
import ru.babaetskv.passionwoman.app.utils.LARGE_TABLET_PORTRAIT
import ru.babaetskv.passionwoman.app.utils.PHONE_LANDSCAPE
import ru.babaetskv.passionwoman.app.utils.PHONE_PORTRAIT
import ru.babaetskv.passionwoman.app.utils.SMALL_TABLET_LANDSCAPE
import ru.babaetskv.passionwoman.app.utils.SMALL_TABLET_PORTRAIT
import ru.babaetskv.passionwoman.domain.model.Profile

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    deviceType: DeviceType,
    modifier: Modifier = Modifier,
) {
    val profile by viewModel.profileLiveData.observeAsState()
    val menuItems by viewModel.menuItemsLiveData.observeAsState(emptyList())
    ProfileScreen(
        modifier = modifier,
        deviceType = deviceType,
        profile = profile,
        menuItems = menuItems,
        onEditAvatarClick = viewModel::onEditAvatarPressed,
        onEditProfileClick = viewModel::onEditPressed,
        onLogInClick = viewModel::onLogInPressed,
        onLogOutClick = viewModel::onLogOutPressed,
        onMenuItemClick = viewModel::onMenuItemPressed,
    )
}

@Composable
private fun ProfileScreen(
    profile: Profile?,
    menuItems: List<ProfileMenuItem>,
    onEditAvatarClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLogInClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onMenuItemClick: (item: ProfileMenuItem) -> Unit,
    deviceType: DeviceType,
    modifier: Modifier = Modifier
) {
    // TODO: fix crash caused by any layout Scaffold on tablets
    when(deviceType) {
        PHONE_LANDSCAPE -> {
            ProfilePhoneLandscapeLayout(
                modifier = modifier,
                profile = profile,
                menuItems = menuItems,
                onEditAvatarClick = onEditAvatarClick,
                onEditProfileClick = onEditProfileClick,
                onLogInClick = onLogInClick,
                onLogOutClick = onLogOutClick,
                onMenuItemClick = onMenuItemClick
            )
        }
        SMALL_TABLET_PORTRAIT, SMALL_TABLET_LANDSCAPE, LARGE_TABLET_PORTRAIT, LARGE_TABLET_LANDSCAPE -> {
            ProfileTabletLayout(
                modifier = modifier,
                profile = profile,
                menuItems = menuItems,
                onEditAvatarClick = onEditAvatarClick,
                onEditProfileClick = onEditProfileClick,
                onLogInClick = onLogInClick,
                onLogOutClick = onLogOutClick,
                onMenuItemClick = onMenuItemClick
            )
        }
        else -> {
            ProfilePhonePortraitLayout(
                modifier = modifier,
                profile = profile,
                menuItems = menuItems,
                onEditAvatarClick = onEditAvatarClick,
                onEditProfileClick = onEditProfileClick,
                onLogInClick = onLogInClick,
                onLogOutClick = onLogOutClick,
                onMenuItemClick = onMenuItemClick
            )
        }
    }
}

@Composable
private fun ProfilePhonePortraitLayout(
    profile: Profile?,
    menuItems: List<ProfileMenuItem>,
    onEditAvatarClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLogInClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onMenuItemClick: (item: ProfileMenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.margin_default)),
            verticalArrangement = Arrangement.spacedBy(
                space = dimensionResource(R.dimen.margin_default)
            ),
            contentPadding = PaddingValues(
                top = with(LocalDensity.current) {
                    WindowInsets.systemBars.getTop(LocalDensity.current).toDp()
                }
            )
        ) {
            item {
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
            }

            items(menuItems) { menuItem ->
                ProfileMenuItem(
                    item = menuItem,
                    onClick = {
                        onMenuItemClick.invoke(menuItem)
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfilePhoneLandscapeLayout(
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
        Row(
            modifier = Modifier
                .systemBarsPadding()
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.margin_default)),
            horizontalArrangement = Arrangement.spacedBy(
                space = dimensionResource(R.dimen.margin_default)
            )
        ) {
            profile?.let {
                ProfileHeader(
                    modifier = Modifier.weight(1f),
                    profile = it,
                    onEditAvatarClick = onEditAvatarClick,
                    onEditProfileClick = onEditProfileClick,
                    onLogOutClick = onLogOutClick,
                )
            } ?: run {
                ProfileHeaderPlaceholder(
                    modifier = Modifier.weight(1f),
                    onLogInClick =  onLogInClick
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(
                    space = dimensionResource(R.dimen.margin_default)
                )
            ) {
                items(menuItems) { menuItem ->
                    ProfileMenuItem(
                        item = menuItem,
                        onClick = {
                            onMenuItemClick.invoke(menuItem)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileTabletLayout(
    profile: Profile?,
    menuItems: List<ProfileMenuItem>,
    onEditAvatarClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLogInClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onMenuItemClick: (item: ProfileMenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
                .systemBarsPadding()
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

@Preview(
    showSystemUi = true,
    device = PHONE_PORTRAIT
)
@Composable
private fun PreviewProfileScreenPhonePortrait() {
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
            onMenuItemClick = {},
            deviceType = PHONE_PORTRAIT
        )
    }
}

@Preview(
    showSystemUi = true,
    device = PHONE_LANDSCAPE
)
@Composable
private fun PreviewProfileScreenPhoneLandscape() {
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
            onMenuItemClick = {},
            deviceType = PHONE_LANDSCAPE
        )
    }
}

@Preview(
    showSystemUi = true,
    device = SMALL_TABLET_PORTRAIT
)
@Composable
private fun PreviewProfileScreenSmallTabletPortrait() {
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
            onMenuItemClick = {},
            deviceType = SMALL_TABLET_PORTRAIT
        )
    }
}

@Preview(
    showSystemUi = true,
    device = SMALL_TABLET_LANDSCAPE
)
@Composable
private fun PreviewProfileScreenSmallTabletLandscape() {
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
            onMenuItemClick = {},
            deviceType = SMALL_TABLET_LANDSCAPE
        )
    }
}

@Preview(
    showSystemUi = true,
    device = LARGE_TABLET_PORTRAIT
)
@Composable
private fun PreviewProfileScreenLargeTabletPortrait() {
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
            onMenuItemClick = {},
            deviceType = LARGE_TABLET_PORTRAIT
        )
    }
}

@Preview(
    showSystemUi = true,
    device = LARGE_TABLET_LANDSCAPE
)
@Composable
private fun PreviewProfileScreenLargeTabletLandscape() {
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
            onMenuItemClick = {},
            deviceType = LARGE_TABLET_LANDSCAPE
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewProfileScreenCompactPlaceholder() {
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
            onMenuItemClick = {},
            deviceType = PHONE_PORTRAIT
        )
    }
}