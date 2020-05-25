package com.maskjs.korona_zakupy.helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maskjs.korona_zakupy.viewmodels.add_product_dialog.AddProductDialogViewModel
import com.maskjs.korona_zakupy.viewmodels.new_order.NewOrderViewModel

class NewOrderViewModelFactory(private val initialText: String, private val onProductClickListener: NewOrderViewModel.OnProductClickListener?)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewOrderViewModel(initialText,onProductClickListener) as T
    }
}