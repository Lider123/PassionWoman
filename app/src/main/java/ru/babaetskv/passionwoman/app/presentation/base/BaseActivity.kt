package ru.babaetskv.passionwoman.app.presentation.base

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Router
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import ru.babaetskv.passionwoman.app.MainApp


abstract class BaseActivity : AppCompatActivity(), KodeinAware {
    protected val router: Router by instance()

    override val kodein: Kodein by lazy {
        MainApp.instance.kodein
    }

    protected fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}