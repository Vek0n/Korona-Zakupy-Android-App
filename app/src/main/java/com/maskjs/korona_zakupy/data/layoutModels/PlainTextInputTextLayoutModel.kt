package com.maskjs.korona_zakupy.data.layoutModels

class PlainTextInputTextLayoutModel: InputTextLayoutModel() {
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