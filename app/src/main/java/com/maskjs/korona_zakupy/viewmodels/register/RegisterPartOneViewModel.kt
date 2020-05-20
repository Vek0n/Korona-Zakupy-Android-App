package com.maskjs.korona_zakupy.viewmodels.register

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.helpers.SharedRegisterDto

class RegisterPartOneViewModel: ViewModel() {
    var roleName =""
    private val registerUserDto = SharedRegisterDto

    fun save(){
        registerUserDto.registerUserDto.roleName = roleName
    }
}