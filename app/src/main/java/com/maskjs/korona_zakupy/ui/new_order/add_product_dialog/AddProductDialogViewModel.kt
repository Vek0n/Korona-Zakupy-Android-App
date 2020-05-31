package com.maskjs.korona_zakupy.ui.new_order.add_product_dialog

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.ProductDto
import com.maskjs.korona_zakupy.data.layoutModels.InputTextLayoutModel
import com.maskjs.korona_zakupy.data.layoutModels.PlainTextInputTextLayoutModel

class AddProductDialogViewModel(private val displayNumber: Array<String>, private val displayUnit: Array<String> ) : ViewModel() {
    val quantityNumberPickerModel : NumberPickerModel =  NumberPickerModel(displayNumber)
    val unitNumberPickerModel: NumberPickerModel = NumberPickerModel(displayUnit)
    val productTextInputLayout : InputTextLayoutModel =
        PlainTextInputTextLayoutModel()
    var isSendToEdit =false

    fun setInitValuesToEdit(product: String?, quantity: String?, unit: String?, sendToEdit: Boolean?){
        product?.let { productTextInputLayout.textContent.value = it }
        sendToEdit?.let { isSendToEdit = sendToEdit }
        quantity?.let { quantityNumberPickerModel.pickerValue = displayNumber.indexOf(quantity)}
        unit?.let { unitNumberPickerModel.pickerValue = displayUnit.indexOf(unit) }

    }

    fun getProductDto() : ProductDto {
        return ProductDto(
            product = productTextInputLayout.textContent.value ?: "null",
            quantity = quantityNumberPickerModel.getDisplayValue() ?: "null",
            unit = unitNumberPickerModel.getDisplayValue() ?: "null",
            isSendToEdit = isSendToEdit
        )
    }

     fun checkValidation(errorMessages: Map<String,String>) : Boolean{
         productTextInputLayout.validate(errorMessages)
         return  isCorrectValidation()
    }

    private fun isCorrectValidation(): Boolean{
        if(productTextInputLayout.errorContent.value !=null)
            return false
        return true
    }

    inner class NumberPickerModel(val displayValues : Array<String>,val wrapSelectorWheel: Boolean = true){
        val minValue = 0
        val maxValue = displayValues.size - 1
        var pickerValue = 0

        fun getDisplayValue(): String?{
            return  displayValues[pickerValue]
        }
    }
}