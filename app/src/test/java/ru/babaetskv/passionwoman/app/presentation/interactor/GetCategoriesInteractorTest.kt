package ru.babaetskv.passionwoman.app.presentation.interactor

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.usecase.GetCategoriesUseCase
import ru.babaetskv.passionwoman.domain.utils.transform
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class GetCategoriesInteractorTest {
    @Mock
    private lateinit var catalogGatewayMock: CatalogGateway
    @Mock
    private lateinit var stringProvider: StringProvider
    @InjectMocks
    private lateinit var interactor: GetCategoriesInteractor

    @Test
    fun execute_returnsCategories_whenCategoriesExist() = runBlocking {
        val categories = listOf(
            createCategoryTransformable("1"),
            createCategoryTransformable("2")
        )
        val expected = categories.map { it.transform() }

        whenever(catalogGatewayMock.getCategories()) doReturn categories

        val actual: List<Category> = interactor.execute()

        assertEquals(expected, actual)
    }

    @Test
    fun execute_throwsEmptyListException_whenCategoriesListIsEmpty() = runBlocking {
        whenever(catalogGatewayMock.getCategories()) doReturn emptyList()
        whenever(stringProvider.EMPTY_CATEGORIES_ERROR) doReturn "error"

        try {
            interactor.execute()
            fail()
        } catch (e: Exception) {
            assertTrue(e.cause is GetCategoriesUseCase.EmptyCategoriesException)
        }
    }

    @Test
    fun execute_throwsGetCategoriesException_whenCatchesException() = runBlocking {
        whenever(catalogGatewayMock.getCategories()) doThrow RuntimeException()
        whenever(stringProvider.GET_CATEGORIES_ERROR) doReturn "error"

        try {
            interactor.execute()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GetCategoriesUseCase.GetCategoriesException)
        }
    }

    private fun createCategoryTransformable(id: String): Transformable<Unit, Category> =
        CategoryModel(
            id = id,
            name = "",
            image = ""
        )
}
