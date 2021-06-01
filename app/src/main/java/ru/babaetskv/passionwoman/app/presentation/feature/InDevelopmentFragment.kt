package ru.babaetskv.passionwoman.app.presentation.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.babaetskv.passionwoman.app.R

class InDevelopmentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_in_development, container, false)

    companion object {

        fun create() = InDevelopmentFragment()
    }
}
