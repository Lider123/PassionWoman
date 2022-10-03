package ru.babaetskv.passionwoman.app.presentation.base

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import kotlinx.parcelize.Parcelize

interface FragmentComponent<VM : IViewModel, TArgs : Parcelable> : ViewComponent<VM> {
    var componentArguments: Bundle
    var _args: TArgs?
    var args: TArgs
        get() = run {
            if (_args == null) {
                _args = componentArguments.getParcelable(ARGUMENTS_KEY)!!
            }
            _args!!
        }
        set(args) {
            componentArguments = bundleOf(ARGUMENTS_KEY to args)
            _args = args
        }

    @Parcelize
    object NoArgs : Parcelable

    companion object {
        private const val ARGUMENTS_KEY = "FRAGMENT_ARGUMENTS"
    }
}
