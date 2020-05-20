package com.maskjs.korona_zakupy.viewmodels.input_text_layout

import com.maskjs.korona_zakupy.viewmodels.input_text_layout.InputTextLayoutViewModel

class PlainTextInputTextLayoutViewModel: InputTextLayoutViewModel() {
    override fun validate(errorMessages: Map<String, String>, extraParameter:String) {
        super.validate(errorMessages,extraParameter)

        if(extraParameter != "")
            validateApiRequest(errorMessages["isAlreadyTaken"]?:"null",extraParameter)
    }

    private fun validateApiRequest(apiResponseError:String,apiRequest: String){
        if(apiRequest != "true")
            errorContent.value = null
        else
            errorContent.value = apiResponseError
    }
}