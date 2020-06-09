package com.maskjs.korona_zakupy.ui.login

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.data_transfer_object.LoginUserDto
import com.maskjs.korona_zakupy.data.users.*
import com.maskjs.korona_zakupy.data.users.api_communication.LoginResponseDto
import com.maskjs.korona_zakupy.data.layoutModels.InputTextLayoutModel

class LoginViewModel(private val errorMessages: Map<String, String>,
                     private val userRepository: UserRepository<LoginUserDto>,
                     val emailInputTextLayoutModel: InputTextLayoutModel,
                     val passwordTextInputLayoutModel: InputTextLayoutModel)
    : ViewModel() {
    lateinit var loginResponseDto: LoginResponseDto

    suspend fun login(){
        loginResponseDto = userRepository.loginUser(
            LoginUserDto(
                email = emailInputTextLayoutModel.textContent.value ?: "",
                password = passwordTextInputLayoutModel.textContent.value ?: ""
            )
        )
    }

    fun validateEmail(){
        emailInputTextLayoutModel.validate(errorMessages)
    }

    fun validatePassword(){
        passwordTextInputLayoutModel.validate(errorMessages)
    }

    fun checkValidation(): Boolean{
        emailInputTextLayoutModel.validate(errorMessages)
        passwordTextInputLayoutModel.validate(errorMessages)

        if( emailInputTextLayoutModel.errorContent.value != null ||
                passwordTextInputLayoutModel.errorContent.value!= null)
                    return false
        return true
    }
}

