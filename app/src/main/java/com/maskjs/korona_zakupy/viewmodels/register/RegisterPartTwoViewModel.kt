package com.maskjs.korona_zakupy.viewmodels.register

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import com.maskjs.korona_zakupy.helpers.SharedRegisterDto
import com.maskjs.korona_zakupy.viewmodels.input_text_layout.ConfirmPasswordTextInputLayoutViewModel
import com.maskjs.korona_zakupy.viewmodels.input_text_layout.InputTextLayoutViewModel
import com.maskjs.korona_zakupy.viewmodels.input_text_layout.PasswordTextInputLayoutViewModel
import com.maskjs.korona_zakupy.viewmodels.input_text_layout.PlainTextInputTextLayoutViewModel
import kotlinx.coroutines.*
import okhttp3.OkHttpClient

class RegisterPartTwoViewModel:  ViewModel() {

    val userNameInputTextLayoutViewModel: InputTextLayoutViewModel =
        PlainTextInputTextLayoutViewModel()
    val emailInputTextLayoutViewModel: InputTextLayoutViewModel =
        PlainTextInputTextLayoutViewModel()
    val passwordInputTextLayoutViewModel: InputTextLayoutViewModel =
        PasswordTextInputLayoutViewModel()
    val confirmPasswordInputTextLayoutViewModel: InputTextLayoutViewModel =
        ConfirmPasswordTextInputLayoutViewModel()
    private lateinit var isUserNameAlreadyTaken: String
    private lateinit var isEmailAlreadyTaken: String
    private val userRepository = UserRepository<RegisterUserDto>(userDao = UserDao(client = OkHttpClient()))
    private val registerUserDto = SharedRegisterDto

    fun validateUserName(errorMessages: Map<String,String>){
        CoroutineScope(Dispatchers.IO).launch {
            checkIsUserNameAlreadyTaken()
            withContext(Dispatchers.Main) {
                userNameInputTextLayoutViewModel.validate(errorMessages,isUserNameAlreadyTaken)
            }
        }
    }

    private suspend fun checkIsUserNameAlreadyTaken() {
        delay(1500)
        isUserNameAlreadyTaken = userRepository.getValidation("name",userNameInputTextLayoutViewModel.textContent.value?:"userDefault")
    }


    fun validateEmail(errorMessages: Map<String, String>){
        CoroutineScope(Dispatchers.IO).launch {
            checkIsEmailNameAlreadyTaken()
            withContext(Dispatchers.Main) {
                emailInputTextLayoutViewModel.validate(errorMessages,isEmailAlreadyTaken)
            }
        }
    }

    private suspend fun checkIsEmailNameAlreadyTaken() {
        delay(1500)
        isEmailAlreadyTaken = userRepository.getValidation("email",emailInputTextLayoutViewModel.textContent.value?:"emailDefault")
    }

    fun validatePassword(errorMessages: Map<String, String>){
        passwordInputTextLayoutViewModel.validate(errorMessages)
    }

    fun validateConfirmPassword(errorMessages: Map<String, String>){
        confirmPasswordInputTextLayoutViewModel.validate(errorMessages,passwordInputTextLayoutViewModel.textContent.value?:"")
    }

     fun checkValidation(errorMessages: Map<String, String>): Boolean{
        userNameInputTextLayoutViewModel.validate(errorMessages)
        emailInputTextLayoutViewModel.validate(errorMessages)
        passwordInputTextLayoutViewModel.validate(errorMessages)
        confirmPasswordInputTextLayoutViewModel.validate(errorMessages,passwordInputTextLayoutViewModel.textContent.value?:"")

        return isCorrectValidation()
    }

    private fun isCorrectValidation():Boolean{
        if(userNameInputTextLayoutViewModel.errorContent.value !=null)
            return false
        else if(emailInputTextLayoutViewModel.errorContent.value != null)
            return false
        else if(passwordInputTextLayoutViewModel.errorContent.value !=null)
            return false
        else if(confirmPasswordInputTextLayoutViewModel.errorContent.value != null)
            return false

        return true
    }

    fun save(){
       registerUserDto.registerUserDto.username = userNameInputTextLayoutViewModel.textContent.value!!
       registerUserDto.registerUserDto.email = emailInputTextLayoutViewModel.textContent.value!!
       registerUserDto.registerUserDto.password = passwordInputTextLayoutViewModel.textContent.value!!
       registerUserDto.registerUserDto.confirmPassword = confirmPasswordInputTextLayoutViewModel.textContent.value!!
    }
}