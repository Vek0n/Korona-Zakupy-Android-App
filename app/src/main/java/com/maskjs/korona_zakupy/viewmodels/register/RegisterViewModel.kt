package com.maskjs.korona_zakupy.viewmodels.register

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.RegisterResponseDto
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import okhttp3.OkHttpClient
import javax.inject.Inject

class RegisterViewModel () : ViewModel() {
    lateinit var userRegisterResponseDto: RegisterResponseDto
    lateinit var toastText : String
    val userNameEditTextContent = MutableLiveData<String>()
    val passwordEditTextContent = MutableLiveData<String>()
    val confirmPasswordEditTextContent = MutableLiveData<String>()
    val emailEditTextContent = MutableLiveData<String>()
    val firstNameEditTextContent = MutableLiveData<String>()
    val lastNameEditTextContent = MutableLiveData<String>()
    val addressEditTextContent = MutableLiveData<String>()
    private val userRepository = UserRepository<RegisterUserDto>(userDao = UserDao(client = OkHttpClient()))
    private val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\\\S+\$).{4,}\$"

    suspend fun register() {
            userRegisterResponseDto = userRepository.registerUser(
                RegisterUserDto(
                    username = userNameEditTextContent.value ?:"",
                    password =  passwordEditTextContent.value ?:"",
                    confirmPassword =  confirmPasswordEditTextContent.value ?:"",
                    email =  emailEditTextContent.value?:"",
                    firstName = firstNameEditTextContent.value ?: "",
                    lastName = lastNameEditTextContent.value ?: "",
                    address = addressEditTextContent.value ?: "",
                    roleName = "Volunteer"
                )
            )!!
    }

    fun  checkPassword(): Boolean{

        if( !confirmPasswordEditTextContent.value.toString().equals(passwordEditTextContent.value.toString())){
            toastText = "Passwords not match"
            return false
        }
        val regex = passwordPattern.toRegex()
        val match = regex.find(confirmPasswordEditTextContent.value.toString())

        if(match != null){
            toastText = "Password must contain at least"
            return false
        }

        return  true
    }
}