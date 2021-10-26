package ru.babaetskv.passionwoman.app.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.Creator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface BottomSheetDialogFragmentScreen : FragmentScreen {

    override fun createFragment(factory: FragmentFactory): Fragment =
        createBottomSheetFragment(factory)

    fun createBottomSheetFragment(factory: FragmentFactory): BottomSheetDialogFragment

    companion object {

        operator fun invoke(
            key: String? = null,
            clearContainer: Boolean = true,
            fragmentCreator: Creator<FragmentFactory, BottomSheetDialogFragment>
        ) = object : BottomSheetDialogFragmentScreen {
            override val screenKey = key ?: fragmentCreator::class.java.name
            override val clearContainer = clearContainer

            override fun createBottomSheetFragment(factory: FragmentFactory) = fragmentCreator.create(factory)
        }
    }
}
