package ru.babaetskv.passionwoman.app.presentation.interactor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform
import ru.babaetskv.passionwoman.domain.usecase.GetCategoriesUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetCategoriesInteractorTest {
    @Mock
    private lateinit var catalogGatewayMock: CatalogGateway
    @Mock
    private lateinit var stringProvider: StringProvider
    @InjectMocks
    private lateinit var interactor: GetCategoriesInteractor

    private fun createCategoryTransformable(id: Long): Transformable<Unit, Category> =
        CategoryModel(
            id = id,
            name = "Category $id",
            image = "category_${id}_image"
        )

    @Before
    fun before() {
        whenever(stringProvider.EMPTY_CATEGORIES_ERROR) doReturn "error"
        whenever(stringProvider.GET_CATEGORIES_ERROR) doReturn "error"
    }

    @Test
    fun execute_callsCatalogGateway() = runTest {
        whenever(catalogGatewayMock.getCategories()).doReturn(listOf(createCategoryTransformable(1)))

        interactor.execute()

        verify(catalogGatewayMock, times(1)).getCategories()
    }

    @Test
    fun execute_returnsCategories_whenCategoriesExist() = runTest {
        val categories = listOf(
            createCategoryTransformable(1),
            createCategoryTransformable(2)
        )
        whenever(catalogGatewayMock.getCategories()) doReturn categories
        val expected = categories.map { it.transform() }

        val result = interactor.execute()

        assertEquals(expected, result)
    }

    @Test
    fun execute_throwsEmptyCategoriesException_whenCategoriesListIsEmpty() = runTest {
        whenever(catalogGatewayMock.getCategories()) doReturn emptyList()

        runCatching {
            interactor.execute()
        }.onFailure {
            assertTrue(it is GetCategoriesUseCase.EmptyCategoriesException)
        }.onSuccess {
            fail()
        }
    }

    @Test
    fun execute_throwsGetCategoriesException_whenCatchesException() = runTest {
        whenever(catalogGatewayMock.getCategories()) doThrow RuntimeException()

        runCatching {
            interactor.execute()
        }.onFailure {
            assertTrue(it is GetCategoriesUseCase.GetCategoriesException)
        }.onSuccess {
            fail()
        }
    }
}
