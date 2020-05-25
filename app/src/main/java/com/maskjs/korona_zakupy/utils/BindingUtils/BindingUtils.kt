package com.maskjs.korona_zakupy.utils.BindingUtils

import android.widget.NumberPicker
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.maskjs.korona_zakupy.ui.new_order.add_product_dialog.AddProductDialogViewModel
import com.maskjs.korona_zakupy.data.layoutModels.InputTextLayoutModel
import com.maskjs.korona_zakupy.ui.new_order.NewOrderViewModel


class BindingUtils {

    companion object {
        @JvmStatic
        @BindingAdapter("app:errorText")
        fun bindErrorText(textInputLayout: TextInputLayout, errorText: String?) {
            textInputLayout.error = errorText
        }

        @JvmStatic
        @BindingAdapter("app:errorTextVersion2")
        fun setErrorTextVersion2(textInputLayout: TextInputLayout, inputTextLayoutModel: InputTextLayoutModel){
            textInputLayout.error = inputTextLayoutModel.errorContent.value
        }

        @JvmStatic
        @BindingAdapter("app:recyclerViewAdapter")
        fun setRecyclerViewAdapter(recyclerView: RecyclerView, newOrderViewModel: NewOrderViewModel?){
            recyclerView.adapter = newOrderViewModel?.productRecyclerViewAdapter
        }
    }
}