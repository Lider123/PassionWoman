package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.components.PassionWomanButton
import ru.babaetskv.passionwoman.app.presentation.theme.PassionWomanTheme

@Composable
fun OnboardingScreen(
    pages: List<OnboardingPage>,
    currentPage: Int,
    onBackwardClick: () -> Unit,
    onForwardClick: () -> Unit,
    onCurrentPageChanged : (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Box {
            val pagerState = rememberPagerState(currentPage) { pages.size }
            val layoutDirection = LocalLayoutDirection.current
            val density = LocalDensity.current
            var prevButtonWidth by remember { mutableStateOf(Dp.Unspecified) }
            var nextButtonWidth by remember { mutableStateOf(Dp.Unspecified) }
            var listIndicatorHeight by remember { mutableStateOf(Dp.Unspecified) }

            LaunchedEffect(currentPage) {
                pagerState.animateScrollToPage(currentPage)
            }

            LaunchedEffect(pagerState.currentPage) {
                onCurrentPageChanged.invoke(pagerState.currentPage)
            }

            HorizontalPager(state = pagerState) { index ->
                val page = pages[index]
                Box {
                    val shadeColor = colorResource(R.color.onboarding_banner_shade)
                    Image(
                        modifier = modifier
                            .fillMaxSize()
                            .drawWithContent {
                                drawContent()
                                drawRect(shadeColor)
                            },
                        painter = painterResource(page.bannerRes),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(
                                start = prevButtonWidth,
                                end = nextButtonWidth,
                                bottom = listIndicatorHeight
                            )
                            .padding(
                                start = innerPadding.calculateStartPadding(layoutDirection),
                                end = innerPadding.calculateEndPadding(layoutDirection),
                                bottom = innerPadding.calculateBottomPadding()
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.spacedBy(dimensionResource(R.dimen.margin_small).value.dp)
                    ) {
                        Text(
                            style = MaterialTheme.typography.h5,
                            text = stringResource(page.messageRes),
                            textAlign = TextAlign.Center
                        )

                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(R.dimen.margin_small).value.dp)
                        )

                        PassionWomanButton(
                            modifier = Modifier.alpha(if (page.actionRes == null) 0f else 1f),
                            text = page.actionRes?.let { stringResource(it) } ?: "",
                            enabled = page.actionCallback != null,
                            onClick = {
                                page.actionCallback?.invoke()
                            }
                        )

                        Spacer(
                            modifier = Modifier
                                .height(dimensionResource(R.dimen.margin_small).value.dp)
                        )
                    }
                }
            }

            if (currentPage > 0) {
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .onSizeChanged { size ->
                            prevButtonWidth = with (density) {
                                size.width.toDp()
                            }
                        }
                        .align(Alignment.CenterStart)
                        .padding(
                            start = innerPadding.calculateStartPadding(layoutDirection),
                            top = innerPadding.calculateTopPadding(),
                            bottom = innerPadding.calculateBottomPadding()
                        ),
                    onClick = onBackwardClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                        contentDescription = stringResource(R.string.previous)
                    )
                }
            }

            if (currentPage < pages.lastIndex) {
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .onSizeChanged { size ->
                            nextButtonWidth = with (density) {
                                size.width.toDp()
                            }
                        }
                        .align(Alignment.CenterEnd)
                        .padding(
                            end = innerPadding.calculateEndPadding(layoutDirection),
                            top = innerPadding.calculateTopPadding(),
                            bottom = innerPadding.calculateBottomPadding()
                        ),
                    onClick = onForwardClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                        contentDescription = stringResource(R.string.next)
                    )
                }
            }

            ListIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .onSizeChanged { size ->
                        listIndicatorHeight = with (density) {
                            size.height.toDp()
                        }
                    }
                    .padding(bottom = 20.dp)
                    .padding(
                        start = innerPadding.calculateStartPadding(layoutDirection),
                        end = innerPadding.calculateEndPadding(layoutDirection),
                        bottom = innerPadding.calculateBottomPadding()
                    )
                    .navigationBarsPadding(),
                currentItem = currentPage,
                itemsCount = pages.size,
            )
        }
    }
}

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    modifier: Modifier = Modifier
) {
    val pages by viewModel.pagesLiveData.observeAsState(emptyList())
    val currentPage by viewModel.currPageLiveData.observeAsState(0)
    OnboardingScreen(
        modifier = modifier,
        pages = pages,
        currentPage = currentPage,
        onBackwardClick = viewModel::onPrevPagePressed,
        onForwardClick = viewModel::onNextPagePressed,
        onCurrentPageChanged = viewModel::onCurrPageChanged,
    )
}

@Composable
private fun ListIndicator(
    currentItem: Int,
    itemsCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement =
            Arrangement.spacedBy(dimensionResource(R.dimen.margin_small).value.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(itemsCount) { index ->
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        shape = CircleShape,
                        color = if (index == currentItem) {
                            MaterialTheme.colors.primary
                        } else {
                            MaterialTheme.colors.onSurface
                        }
                    )
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    PassionWomanTheme {
        var currentPage by remember { mutableIntStateOf(0) }
        val pages = listOf(
            OnboardingPage(R.drawable.onboarding_1, R.string.onboarding_1),
            OnboardingPage(R.drawable.onboarding_2, R.string.onboarding_2),
            OnboardingPage(R.drawable.onboarding_3, R.string.onboarding_3),
            OnboardingPage(R.drawable.onboarding_4, R.string.onboarding_4),
            OnboardingPage(
                R.drawable.onboarding_5,
                R.string.onboarding_5,
                R.string.onboarding_next
            ) {}
        )
        OnboardingScreen(
            currentPage = currentPage,
            pages = pages,
            onBackwardClick = {
                if (currentPage > 0) currentPage--
            },
            onForwardClick = {
                if (currentPage < pages.lastIndex) currentPage++
            },
            onCurrentPageChanged = { page ->
                currentPage = page
            }
        )
    }
}
