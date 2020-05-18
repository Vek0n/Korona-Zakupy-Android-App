package com.maskjs.korona_zakupy.helpers

class PlainTextInputTextLayoutViewModel: InputTextLayoutViewModel() {
    override fun validate(errorMessages: Map<String, String>, extraParameter:String) {
        super.validate(errorMessages,extraParameter)

        if(extraParameter != "")
            validateApiRequest(errorMessages["userNameIsAlreadyTaken"]?:"null",extraParameter)
    }

    private fun validateApiRequest(apiResponseError:String,apiRequest: String){
        if(apiRequest != "true")
            errorContent.value = null
        else
            errorContent.value = apiResponseError
    }
}