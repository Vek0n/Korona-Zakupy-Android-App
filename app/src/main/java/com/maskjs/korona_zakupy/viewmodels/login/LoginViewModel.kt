package com.maskjs.korona_zakupy.viewmodels.login

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.*
import com.maskjs.korona_zakupy.viewmodels.input_text_layout.InputTextLayoutViewModel
import com.maskjs.korona_zakupy.viewmodels.input_text_layout.PlainTextInputTextLayoutViewModel
import okhttp3.OkHttpClient

class LoginViewModel : ViewModel() {
    private val userRepository = UserRepository<LoginUserDto>(userDao = UserDao(client = OkHttpClient()))
    lateinit var loginResponseDto: LoginResponseDto

    val emailInputTextLayoutViewModel : InputTextLayoutViewModel =
        PlainTextInputTextLayoutViewModel()
    val passwordInputTextLayoutViewModel : InputTextLayoutViewModel =
        PlainTextInputTextLayoutViewModel()

    suspend fun login(){
        loginResponseDto = userRepository.loginUser(
            LoginUserDto(
                email = emailInputTextLayoutViewModel.textContent.value?: "",
                password = passwordInputTextLayoutViewModel.textContent.value?:""
        ))
    }

    fun validateEmail(errorMessages: Map<String, String>){
        emailInputTextLayoutViewModel.validate(errorMessages)
    }

    fun validatePassword(errorMessages: Map<String, String>){
        passwordInputTextLayoutViewModel.validate(errorMessages)
    }

    fun checkValidation(errorMessages: Map<String,String>): Boolean{
        emailInputTextLayoutViewModel.validate(errorMessages)
        passwordInputTextLayoutViewModel.validate(errorMessages)

        if( emailInputTextLayoutViewModel.errorContent.value != null ||
                passwordInputTextLayoutViewModel.errorContent.value!= null)
                    return false
        return true
    }
}

