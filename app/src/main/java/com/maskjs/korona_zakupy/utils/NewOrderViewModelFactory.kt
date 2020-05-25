package com.maskjs.korona_zakupy.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maskjs.korona_zakupy.ui.new_order.NewOrderViewModel

class NewOrderViewModelFactory(private val initialText: String, private val onProductClickListener: NewOrderViewModel.OnProductClickListener?)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewOrderViewModel(
            initialText,
            onProductClickListener
        ) as T
    }
}