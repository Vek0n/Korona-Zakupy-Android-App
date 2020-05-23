package com.maskjs.korona_zakupy.viewmodels.add_product_dialog

import com.maskjs.korona_zakupy.data.orders.ProductDto
import com.maskjs.korona_zakupy.viewmodels.input_text_layout.InputTextLayoutViewModel
import com.maskjs.korona_zakupy.viewmodels.input_text_layout.PlainTextInputTextLayoutViewModel

class AddProductDialogViewModel(displayNumber: Array<String>, displayUnit: Array<String> ) {
    val quantityNumberPickerViewModel : NumberPickerViewModel =  NumberPickerViewModel(displayNumber)
    val unitNumberPickerViewModel: NumberPickerViewModel= NumberPickerViewModel(displayUnit)
    val productTextInputLayout : InputTextLayoutViewModel = PlainTextInputTextLayoutViewModel()

    fun getProductDto() : ProductDto{
        return ProductDto(
            product = productTextInputLayout.textContent.value!!,
            quantity = quantityNumberPickerViewModel.pickerValue + " " + unitNumberPickerViewModel.pickerValue)
    }


     fun checkValidation(errorMessages: Map<String,String>) : Boolean{
         validate(errorMessages)

        return isCorrectValidation()
    }

    private fun isCorrectValidation(): Boolean{
        if(productTextInputLayout.errorContent.value !=null)
            return false
        return true
    }

    fun validate(errorMessages: Map<String,String>){
        productTextInputLayout.validate(errorMessages)
    }

    inner class NumberPickerViewModel( val displayValues : Array<String>){
        val minValue = 0
        val maxValue = displayValues.size - 1
        val wrapSelectorWheel = true
        var pickerValue = displayValues[0]

        fun getPickerValue(index : Int){
            pickerValue = displayValues[index]
        }
    }
}