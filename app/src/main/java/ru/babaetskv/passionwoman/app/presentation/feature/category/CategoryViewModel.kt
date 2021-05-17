package ru.babaetskv.passionwoman.app.presentation.feature.category

import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.domain.interactor.GetProductsUseCase
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.model.Product

class CategoryViewModel(
    args: CategoryFragment.Args,
    private val getProductsUseCase: GetProductsUseCase,
    router: Router
) : BaseViewModel(router) {
    val categoryLiveData = MutableLiveData<Category>(args.category)
    val productsLiveData = MutableLiveData<List<Product>>(emptyList())

    init {
        loadProducts()
    }

    private fun loadProducts() {
        launchWithLoading {
            when (val result = getProductsUseCase.execute(categoryLiveData.value!!.id)) {
                is BaseUseCase.Result.Success -> {
                    productsLiveData.postValue(result.data)
                }
                is BaseUseCase.Result.Failure -> {
                    // TODO: handle error
                }
            }
        }
    }

    fun onProductPressed(product: Product) {
        // TODO
    }
}
