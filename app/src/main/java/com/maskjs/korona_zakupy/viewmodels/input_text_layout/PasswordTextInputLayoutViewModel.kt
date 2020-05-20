package com.maskjs.korona_zakupy.viewmodels.input_text_layout

import com.maskjs.korona_zakupy.viewmodels.input_text_layout.InputTextLayoutViewModel

class PasswordTextInputLayoutViewModel : InputTextLayoutViewModel() {
    private val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=?!])(?=\\S+$).{4,}$"

    override fun validate(errorsMessages: Map<String, String>, extraParameter:String) {
        super.validate(errorsMessages,extraParameter)

        if(errorContent.value == null){
            validatePassword(errorsMessages)
        }
    }

    private fun validatePassword(errorMessages: Map<String,String>){
                if(textContent.value?.length ?: "".length < 8)
                    errorContent.value = errorMessages["errorRegexMessage"]
                else{
                    val passwordMatcher = Regex(passwordPattern)

                    if(passwordMatcher.find(textContent.value!!) == null)
                       errorContent.value = errorMessages["errorRegexMessage"]
                    else
                        errorContent.value = null
                }
    }

}