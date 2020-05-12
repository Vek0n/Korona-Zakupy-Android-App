package com.maskjs.korona_zakupy.viewmodels.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import okhttp3.OkHttpClient

class LoginViewModel : ViewModel() {
    private val userRepository = UserRepository<RegisterUserDto>(userDao = UserDao(client = OkHttpClient()))
    val emailEditTextContent = MutableLiveData<String>()
    val passwordEditTextContent = MutableLiveData<String>()

    fun isNotEmpty(validText: String): Boolean{
        if(validText.isEmpty()|| validText == "null")
            return false

        return true
    }

}