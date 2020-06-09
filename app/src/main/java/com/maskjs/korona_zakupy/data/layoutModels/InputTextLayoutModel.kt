package com.maskjs.korona_zakupy.data.layoutModels

import androidx.lifecycle.MutableLiveData
import com.maskjs.korona_zakupy.data.layoutModels.interfaces.IInputTextLayoutModel

abstract class InputTextLayoutModel : IInputTextLayoutModel {
     val textContent = MutableLiveData<String>()
    val errorContent = MutableLiveData<String?>()

    override fun validate(errorsMessages: Map<String,String>, extraParameter:String){
        validateIsNotEmpty(errorsMessages["emptyError"]?:"null")
    }

    private fun validateIsNotEmpty(errorMessage: String){
       if(!isNotEmpty())
           errorContent.value = errorMessage
       else
           errorContent.value = null
    }

    private fun isNotEmpty(): Boolean{
        if(textContent.value?.isEmpty() != false || textContent?.value == "null")
            return false

        return true
    }

 }