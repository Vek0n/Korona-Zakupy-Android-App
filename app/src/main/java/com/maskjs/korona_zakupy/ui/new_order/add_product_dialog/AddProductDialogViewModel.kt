package com.maskjs.korona_zakupy.ui.new_order.add_product_dialog

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.ProductDto
import com.maskjs.korona_zakupy.data.layoutModels.InputTextLayoutModel
import com.maskjs.korona_zakupy.data.layoutModels.PlainTextInputTextLayoutModel

class AddProductDialogViewModel(displayNumber: Array<String>, displayUnit: Array<String> ) : ViewModel() {
    val quantityNumberPickerModel : NumberPickerModel =  NumberPickerModel(displayNumber)
    val unitNumberPickerModel: NumberPickerModel = NumberPickerModel(displayUnit)
    val productTextInputLayout : InputTextLayoutModel =
        PlainTextInputTextLayoutModel()
    var isSendToEdit =false

    fun setInitValuesToEdit(product: String?, quantity: String?, unit: String?,sendToEdit: Boolean?){
        product?.let { productTextInputLayout.textContent.value = it }
        quantity?.let { quantityNumberPickerModel.pickerValue = it }
        unit?.let { unitNumberPickerModel.pickerValue = it }
        sendToEdit?.let { isSendToEdit = sendToEdit }
    }

    fun getProductDto() : ProductDto {
        return ProductDto(
            product = productTextInputLayout.textContent.value ?: "product",
            quantity = quantityNumberPickerModel.pickerValue,
            unit = unitNumberPickerModel.pickerValue,
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

    inner class NumberPickerModel(val displayValues : Array<String>){
        val minValue = 0
        val maxValue = displayValues.size - 1
        val wrapSelectorWheel = true
        var pickerValue = displayValues[0]

        fun getPickerValue(index : Int){
            pickerValue = displayValues[index]
        }
    }
}