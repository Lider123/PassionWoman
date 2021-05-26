package ru.babaetskv.passionwoman.app.presentation.base

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Router
import org.koin.android.ext.android.inject
import ru.babaetskv.passionwoman.app.presentation.SimpleKeyboardAnimator

abstract class BaseActivity : AppCompatActivity() {
    private val keyboardAnimator: SimpleKeyboardAnimator by lazy {
        SimpleKeyboardAnimator(window)
    }
    protected val router: Router by inject()

    override fun onStart() {
        super.onStart()
        keyboardAnimator.start()
    }

    override fun onStop() {
        keyboardAnimator.stop()
        super.onStop()
    }

    protected fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
