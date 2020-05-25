package com.maskjs.korona_zakupy.ui.login

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.data_transfer_object.LoginUserDto
import com.maskjs.korona_zakupy.data.users.*
import com.maskjs.korona_zakupy.data.users.api_communication.LoginResponseDto
import com.maskjs.korona_zakupy.data.users.api_communication.UserDao
import com.maskjs.korona_zakupy.data.layoutModels.InputTextLayoutModel
import com.maskjs.korona_zakupy.data.layoutModels.PlainTextInputTextLayoutModel
import okhttp3.OkHttpClient

class LoginViewModel : ViewModel() {
    private val userRepository = UserRepository<LoginUserDto>(userDao = UserDao(
        client = OkHttpClient()
    )
    )
    lateinit var loginResponseDto: LoginResponseDto

    val emailInputTextLayoutModel : InputTextLayoutModel =
        PlainTextInputTextLayoutModel()
    val passwordInputTextLayoutModel : InputTextLayoutModel =
        PlainTextInputTextLayoutModel()

    suspend fun login(){
        loginResponseDto = userRepository.loginUser(
            LoginUserDto(
                email = emailInputTextLayoutModel.textContent.value ?: "",
                password = passwordInputTextLayoutModel.textContent.value ?: ""
            )
        )
    }

    fun validateEmail(errorMessages: Map<String, String>){
        emailInputTextLayoutModel.validate(errorMessages)
    }

    fun validatePassword(errorMessages: Map<String, String>){
        passwordInputTextLayoutModel.validate(errorMessages)
    }

    fun checkValidation(errorMessages: Map<String,String>): Boolean{
        emailInputTextLayoutModel.validate(errorMessages)
        passwordInputTextLayoutModel.validate(errorMessages)

        if( emailInputTextLayoutModel.errorContent.value != null ||
                passwordInputTextLayoutModel.errorContent.value!= null)
                    return false
        return true
    }
}

