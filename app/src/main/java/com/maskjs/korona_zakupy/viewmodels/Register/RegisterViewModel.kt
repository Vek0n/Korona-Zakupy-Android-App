package com.maskjs.korona_zakupy.viewmodels.Register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import okhttp3.OkHttpClient
import javax.inject.Inject

class RegisterViewModel () : ViewModel() {
    private val userRepository = UserRepository<RegisterUserDto>(userDao = UserDao(client = OkHttpClient()))
    val registerDto = MutableLiveData<RegisterUserDto>()

}