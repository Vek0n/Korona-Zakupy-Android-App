package com.maskjs.korona_zakupy.ui.main

import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    var userId : String? = null
    var userToken : String? = null
    var userRole : String? = null

    fun readSavedData(savedData : Triple<String,String,String>){
        userId = savedData.first
        userToken = savedData.second
        userRole = savedData.third
    }

   fun chooseActivity() : Activities{
       return validateSavedData()
   }

    private fun validateSavedData() : Activities{
        return if(userId == null || userId == "" )
            Activities.MAIN
        else if(userToken == null || userToken =="")
            Activities.MAIN
        else if(userRole == null || userRole == "")
            Activities.MAIN
        else goToUserActivity()
    }

    private fun goToUserActivity(): Activities{
        return when(userRole){
            "Volunteer" -> Activities.VOLUNTEER
            "PersonInQuarantine" -> Activities.PERSON_IN_QUARANTINE
            else -> Activities.MAIN
        }
    }

    enum class Activities{
        MAIN, VOLUNTEER, PERSON_IN_QUARANTINE, LOGIN, REGISTER
    }
}