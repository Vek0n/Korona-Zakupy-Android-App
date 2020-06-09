package com.maskjs.korona_zakupy.ui.register.part1

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.data_transfer_object.RegisterUserDto
import com.maskjs.korona_zakupy.utils.SharedRegisterDto

class RegisterPart1ViewModel(private val registerUserDto: RegisterUserDto): ViewModel() {
    var roleName =""

    fun save(){
        registerUserDto.roleName = roleName
    }
}