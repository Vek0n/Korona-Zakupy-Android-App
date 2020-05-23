package com.maskjs.korona_zakupy.helpers

import android.widget.NumberPicker
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseMethod
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.maskjs.korona_zakupy.viewmodels.add_product_dialog.AddProductDialogViewModel
import com.maskjs.korona_zakupy.viewmodels.new_order.NewOrderViewModel


class DataBindingAdapters {

    companion object {
        @JvmStatic
        @BindingAdapter("app:errorText")
        fun bindErrorText(textInputLayout: TextInputLayout, errorText: String?) {
            textInputLayout.error = errorText
        }

        @JvmStatic
        @BindingAdapter("app:recyclerViewAdapter")
        fun setRecyclerViewAdapter(recyclerView: RecyclerView, newOrderViewModel: NewOrderViewModel?){
            recyclerView.adapter = newOrderViewModel?.productRecyclerViewAdapter
        }

        @JvmStatic
        @BindingAdapter("app:maxValue")
         fun customSetMaxValue(numberPicker: NumberPicker,numberPickerViewModel: AddProductDialogViewModel.NumberPickerViewModel){
            numberPicker.maxValue = numberPickerViewModel.maxValue
        }
        @JvmStatic
        @BindingAdapter("app:minValue")
        fun customSetMinMaxValue(numberPicker: NumberPicker,numberPickerViewModel: AddProductDialogViewModel.NumberPickerViewModel){
            numberPicker.minValue = numberPickerViewModel.minValue
        }

        @JvmStatic
        @BindingAdapter("app:displayedValues")
        fun customSetDisplayedValue(numberPicker: NumberPicker,numberPickerViewModel: AddProductDialogViewModel.NumberPickerViewModel){
            numberPicker.displayedValues = numberPickerViewModel.displayValues
        }

        @JvmStatic
        @BindingAdapter("app:wrapSelectorWheel")
        fun setwrapSelectorWheel(numberPicker: NumberPicker, numberPickerViewModel: AddProductDialogViewModel.NumberPickerViewModel){
            numberPicker.wrapSelectorWheel = numberPickerViewModel.wrapSelectorWheel
        }

        @JvmStatic
        @BindingAdapter("app:onValueChangedListenerNumberPicker")
        fun setOnValueChangedListenerNumberPicker(numberPicker: NumberPicker, numberPickerViewModel: AddProductDialogViewModel.NumberPickerViewModel){
            numberPicker.setOnValueChangedListener { _, _, newVal ->
                numberPickerViewModel.getPickerValue(newVal)
            }
        }
    }
}