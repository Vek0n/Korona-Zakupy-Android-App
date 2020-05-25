package com.maskjs.korona_zakupy.ui.register.part1

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.utils.SharedRegisterDto

class RegisterPartOneViewModel: ViewModel() {
    var roleName =""
    private val registerUserDto = SharedRegisterDto

    fun save(){
        registerUserDto.registerUserDto.roleName = roleName
    }
}