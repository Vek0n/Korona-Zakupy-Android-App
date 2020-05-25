package com.maskjs.korona_zakupy.helpers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maskjs.korona_zakupy.viewmodels.add_product_dialog.AddProductDialogViewModel

class AddProductDialogViewModelFactory( private val displayNumber: Array<String>, private val displayUnit: Array<String>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddProductDialogViewModel( displayNumber,displayUnit) as T
    }
}