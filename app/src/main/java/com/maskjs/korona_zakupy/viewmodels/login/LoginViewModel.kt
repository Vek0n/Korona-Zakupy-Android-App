package com.maskjs.korona_zakupy.viewmodels.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.*
import okhttp3.OkHttpClient

class LoginViewModel : ViewModel() {
    private val userRepository = UserRepository<LoginUserDto>(userDao = UserDao(client = OkHttpClient()))
    val emailEditTextContent = MutableLiveData<String>()
    val passwordEditTextContent = MutableLiveData<String>()
    lateinit var loginResponseDto: LoginResponseDto

    suspend fun login(){
        loginResponseDto = userRepository.loginUser(
            LoginUserDto(
                email = emailEditTextContent.value?: "",
                password = passwordEditTextContent.value?:""
        ))
    }

    fun isNotEmpty(validText: String): Boolean{
        if(validText.isEmpty()|| validText == "null")
            return false

        return true
    }

}