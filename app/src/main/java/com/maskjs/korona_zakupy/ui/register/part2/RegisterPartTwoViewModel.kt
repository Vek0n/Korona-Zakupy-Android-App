package com.maskjs.korona_zakupy.ui.register.part2

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.data_transfer_object.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.api_communication.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import com.maskjs.korona_zakupy.utils.SharedRegisterDto
import com.maskjs.korona_zakupy.data.layoutModels.ConfirmPasswordTextInputLayoutModel
import com.maskjs.korona_zakupy.data.layoutModels.InputTextLayoutModel
import com.maskjs.korona_zakupy.data.layoutModels.PasswordTextInputLayoutModel
import com.maskjs.korona_zakupy.data.layoutModels.PlainTextInputTextLayoutModel
import kotlinx.coroutines.*
import okhttp3.OkHttpClient

class RegisterPartTwoViewModel:  ViewModel() {

    val userNameInputTextLayoutModel: InputTextLayoutModel =
        PlainTextInputTextLayoutModel()
    val emailInputTextLayoutModel: InputTextLayoutModel =
        PlainTextInputTextLayoutModel()
    val passwordInputTextLayoutModel: InputTextLayoutModel =
        PasswordTextInputLayoutModel()
    val confirmPasswordInputTextLayoutModel: InputTextLayoutModel =
        ConfirmPasswordTextInputLayoutModel()
    private lateinit var isUserNameAlreadyTaken: String
    private lateinit var isEmailAlreadyTaken: String
    private val userRepository = UserRepository<RegisterUserDto>(userDao = UserDao(
        client = OkHttpClient()
    )
    )
    private val registerUserDto = SharedRegisterDto

    fun validateUserName(errorMessages: Map<String,String>){
        CoroutineScope(Dispatchers.IO).launch {
            checkIsUserNameAlreadyTaken()
            withContext(Dispatchers.Main) {
                userNameInputTextLayoutModel.validate(errorMessages,isUserNameAlreadyTaken)
            }
        }
    }

    private suspend fun checkIsUserNameAlreadyTaken() {
        delay(1500)
        isUserNameAlreadyTaken = userRepository.getValidation("name",userNameInputTextLayoutModel.textContent.value?:"userDefault")
    }


    fun validateEmail(errorMessages: Map<String, String>){
        CoroutineScope(Dispatchers.IO).launch {
            checkIsEmailNameAlreadyTaken()
            withContext(Dispatchers.Main) {
                emailInputTextLayoutModel.validate(errorMessages,isEmailAlreadyTaken)
            }
        }
    }

    private suspend fun checkIsEmailNameAlreadyTaken() {
        delay(1500)
        isEmailAlreadyTaken = userRepository.getValidation("email",emailInputTextLayoutModel.textContent.value?:"emailDefault")
    }

    fun validatePassword(errorMessages: Map<String, String>){
        passwordInputTextLayoutModel.validate(errorMessages)
    }

    fun validateConfirmPassword(errorMessages: Map<String, String>){
        confirmPasswordInputTextLayoutModel.validate(errorMessages,passwordInputTextLayoutModel.textContent.value?:"")
    }

     fun checkValidation(errorMessages: Map<String, String>): Boolean{
        userNameInputTextLayoutModel.validate(errorMessages)
        emailInputTextLayoutModel.validate(errorMessages)
        passwordInputTextLayoutModel.validate(errorMessages)
        confirmPasswordInputTextLayoutModel.validate(errorMessages,passwordInputTextLayoutModel.textContent.value?:"")

        return isCorrectValidation()
    }

    private fun isCorrectValidation():Boolean{
        if(userNameInputTextLayoutModel.errorContent.value !=null)
            return false
        else if(emailInputTextLayoutModel.errorContent.value != null)
            return false
        else if(passwordInputTextLayoutModel.errorContent.value !=null)
            return false
        else if(confirmPasswordInputTextLayoutModel.errorContent.value != null)
            return false

        return true
    }

    fun save(){
       registerUserDto.registerUserDto.username = userNameInputTextLayoutModel.textContent.value!!
       registerUserDto.registerUserDto.email = emailInputTextLayoutModel.textContent.value!!
       registerUserDto.registerUserDto.password = passwordInputTextLayoutModel.textContent.value!!
       registerUserDto.registerUserDto.confirmPassword = confirmPasswordInputTextLayoutModel.textContent.value!!
    }
}