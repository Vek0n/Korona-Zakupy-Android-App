package com.maskjs.korona_zakupy.helpers

class ConfirmPasswordTextInputLayoutViewModel : InputTextLayoutViewModel() {
    override fun validate(errorsMessages: Map<String, String>, extraParameter:String) {
        super.validate(errorsMessages,extraParameter)

        if(errorContent.value == null && extraParameter!="")
            validateConfirmPassword(errorsMessages,extraParameter)
    }

    private fun validateConfirmPassword(errorsMessages: Map<String, String>, password: String){
        if(password!= textContent.value)
            errorContent.value = errorsMessages["notMatchError"]
        else
            errorContent.value = null
    }
}