package ru.babaetskv.passionwoman.app.presentation.feature.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment

class OrdersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            FragmentContent()
        }
    }

    @Preview
    @Composable
    fun FragmentContent() {
        Text(
            modifier = Modifier
                .background(Color.Red)
                .fillMaxWidth(),
            text = "TODO"
        )
    }

    companion object {

        fun create() = OrdersFragment()
    }
}
