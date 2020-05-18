package com.maskjs.korona_zakupy.viewmodels.register

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.RegisterResponseDto
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import com.maskjs.korona_zakupy.helpers.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient

class RegisterViewModel () : ViewModel() {

    private val userRepository = UserRepository<RegisterUserDto>(userDao = UserDao(client = OkHttpClient()))
    lateinit var userRegisterResponseDto: RegisterResponseDto
    lateinit var isUserNameAlreadyTaken: String
    lateinit var isEmailAlreadyTaken: String
    lateinit var roleName: String
    val userNameInputTextLayoutViewModel: InputTextLayoutViewModel = PlainTextInputTextLayoutViewModel()
    val emailInputTextLayoutViewModel: InputTextLayoutViewModel = PlainTextInputTextLayoutViewModel()
    val passwordInputTextLayoutViewModel: InputTextLayoutViewModel = PasswordTextInputLayoutViewModel()
    val confirmPasswordInputTextLayoutViewModel: InputTextLayoutViewModel = ConfirmPasswordTextInputLayoutViewModel()
    val firstNameInputLayoutViewModel: InputTextLayoutViewModel = PlainTextInputTextLayoutViewModel()
    val lastNameInputLayoutViewModel: InputTextLayoutViewModel = PlainTextInputTextLayoutViewModel()
    val addressInputTextLayoutViewModel: InputTextLayoutViewModel= PlainTextInputTextLayoutViewModel()

    suspend fun register() {
            userRegisterResponseDto = userRepository.registerUser(
                RegisterUserDto(
                    username = userNameInputTextLayoutViewModel.textContent.value ?:"",
                    password =  passwordInputTextLayoutViewModel.textContent.value ?:"",
                    confirmPassword =  confirmPasswordInputTextLayoutViewModel.textContent.value ?:"",
                    email =  emailInputTextLayoutViewModel.textContent.value?:"",
                    firstName = firstNameInputLayoutViewModel.textContent.value ?: "",
                    lastName = lastNameInputLayoutViewModel.textContent.value ?: "",
                    address = addressInputTextLayoutViewModel.textContent.value ?: "",
                    roleName = roleName
                )
            )!!
    }

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

    fun validateConfirmPassword(errorMessages: Map<String, String>, password: String){
        confirmPasswordInputTextLayoutViewModel.validate(errorMessages,passwordInputTextLayoutViewModel.textContent.value?:"")
    }

    fun validateFirstName(errorMessages: Map<String, String>){
        firstNameInputLayoutViewModel.validate(errorMessages)
    }

    fun validateLastName(errorMessages: Map<String, String>){
        lastNameInputLayoutViewModel.validate(errorMessages)
    }

    fun validateAddress(errorMessages: Map<String, String>){
        addressInputTextLayoutViewModel.validate(errorMessages)
    }

    fun checkValidationForPartTwo(errorMessage: Map<String,String>) : Boolean{
        firstNameInputLayoutViewModel.validate(errorMessage)
        lastNameInputLayoutViewModel.validate(errorMessage)
        addressInputTextLayoutViewModel.validate(errorMessage)

        return isRegUi2HasNotError()
    }

    fun checkValidationForPartOne(errorMessages: Map<String, String>) : Boolean{
        userNameInputTextLayoutViewModel.validate(errorMessages)
        emailInputTextLayoutViewModel.validate(errorMessages)
        passwordInputTextLayoutViewModel.validate(errorMessages)
        confirmPasswordInputTextLayoutViewModel.validate(errorMessages)

        return isRegUi1HasNotError()
    }

    private fun isRegUi1HasNotError():Boolean{

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

    private fun isRegUi2HasNotError(): Boolean{
        if(firstNameInputLayoutViewModel.errorContent.value !=null)
            return false
        else if(lastNameInputLayoutViewModel.errorContent.value != null)
            return false
        else if(addressInputTextLayoutViewModel.errorContent.value !=null)
            return false

        return true
    }
}