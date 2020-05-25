package com.maskjs.korona_zakupy.helpers

import android.widget.NumberPicker
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.maskjs.korona_zakupy.viewmodels.add_product_dialog.AddProductDialogViewModel
import com.maskjs.korona_zakupy.viewmodels.input_text_layout.InputTextLayoutViewModel
import com.maskjs.korona_zakupy.viewmodels.new_order.NewOrderViewModel


class DataBindingAdapters {

    companion object {
        @JvmStatic
        @BindingAdapter("app:errorText")
        fun bindErrorText(textInputLayout: TextInputLayout, errorText: String?) {
            textInputLayout.error = errorText
        }

        @JvmStatic
        @BindingAdapter("app:errorTextVersion2")
        fun setErrorTextVersion2(textInputLayout: TextInputLayout,inputTextLayoutViewModel: InputTextLayoutViewModel){
            textInputLayout.error = inputTextLayoutViewModel.errorContent.value
        }

        @JvmStatic
        @BindingAdapter("app:recyclerViewAdapter")
        fun setRecyclerViewAdapter(recyclerView: RecyclerView, newOrderViewModel: NewOrderViewModel?){
            recyclerView.adapter = newOrderViewModel?.productRecyclerViewAdapter
        }

        @JvmStatic
        @BindingAdapter("app:maxValue")
         fun customSetMaxValue(numberPicker: NumberPicker, numberPickerModel: AddProductDialogViewModel.NumberPickerModel){
            numberPicker.maxValue = numberPickerModel.maxValue
        }
        @JvmStatic
        @BindingAdapter("app:minValue")
        fun customSetMinMaxValue(numberPicker: NumberPicker, numberPickerModel: AddProductDialogViewModel.NumberPickerModel){
            numberPicker.minValue = numberPickerModel.minValue
        }

        @JvmStatic
        @BindingAdapter("app:displayedValues")
        fun customSetDisplayedValue(numberPicker: NumberPicker, numberPickerModel: AddProductDialogViewModel.NumberPickerModel){
            numberPicker.displayedValues = numberPickerModel.displayValues
        }

        @JvmStatic
        @BindingAdapter("app:wrapSelectorWheel")
        fun setwrapSelectorWheel(numberPicker: NumberPicker, numberPickerModel: AddProductDialogViewModel.NumberPickerModel){
            numberPicker.wrapSelectorWheel = numberPickerModel.wrapSelectorWheel
        }

        @JvmStatic
        @BindingAdapter("app:onValueChangedListenerNumberPicker")
        fun setOnValueChangedListenerNumberPicker(numberPicker: NumberPicker, numberPickerModel: AddProductDialogViewModel.NumberPickerModel){
            numberPicker.setOnValueChangedListener { _, _, newVal ->
                numberPickerModel.getPickerValue(newVal)
            }
        }
    }
}