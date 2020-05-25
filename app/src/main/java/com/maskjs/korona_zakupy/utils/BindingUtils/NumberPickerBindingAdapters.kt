package com.maskjs.korona_zakupy.utils.BindingUtils

import android.widget.NumberPicker
import androidx.databinding.BindingAdapter
import com.maskjs.korona_zakupy.ui.new_order.add_product_dialog.AddProductDialogViewModel

class NumberPickerBindingAdapters {
    companion object{
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