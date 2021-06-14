package ru.babaetskv.passionwoman.app.presentation.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.FragmentInDevelopmentBinding
import ru.babaetskv.passionwoman.app.utils.load

class InDevelopmentFragment : Fragment() {
    private val binding: FragmentInDevelopmentBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_in_development, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBanner.load(R.drawable.banner_in_development)
    }

    companion object {

        fun create() = InDevelopmentFragment()
    }
}
