package com.maskjs.korona_zakupy.viewmodels.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.RegisterResponseDto
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import com.maskjs.korona_zakupy.helpers.InputTextType
import okhttp3.OkHttpClient

class RegisterViewModel () : ViewModel() {

    lateinit var userRegisterResponseDto: RegisterResponseDto
    val userNameEditTextContent = MutableLiveData<String>()
    val passwordEditTextContent = MutableLiveData<String>()
    val confirmPasswordEditTextContent = MutableLiveData<String>()
    val emailEditTextContent = MutableLiveData<String>()
    val firstNameEditTextContent = MutableLiveData<String>()
    val lastNameEditTextContent = MutableLiveData<String>()
    val addressEditTextContent = MutableLiveData<String>()
    val userNameErrorText = MutableLiveData<String?>()
    val passwordErrorText = MutableLiveData<String?>()
    val confirmPasswordErrorText = MutableLiveData<String?>()
    val emailErrorText = MutableLiveData<String?>()
    private val userRepository = UserRepository<RegisterUserDto>(userDao = UserDao(client = OkHttpClient()))
    private val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=?!])(?=\\S+$).{4,}$"
    private lateinit var isUserNameAlreadyTaken: String
    private lateinit var isEmailAlreadyTaken: String

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

    fun validate(textInputTextType: InputTextType,errorsText: Map<String,String>){
        when(textInputTextType){
            InputTextType.USERNAME -> validateUserName(errorsText["emptyError"]?:"null")
            InputTextType.EMAIL ->  validateEmail(errorsText["emptyError"]?:"null")
            InputTextType.PASSWORD_REGISTER -> validatePassword(errorsText["emptyError"]?:"null",errorsText["errorRegexMessage"]?:"null")
            InputTextType.CONFIRM_PASSWORD_REGISTER -> validateConfirmPassword(errorsText["emptyError"]?:"null",errorsText["notMatchError"]?:"null")
        }

    }

    private fun validateUserName(errorMessage: String){
        userNameErrorText.value = if(!isNotEmpty(userNameEditTextContent?.value?:""))
            errorMessage
        else
            null
        if(isUserNameAlreadyTaken.equals("true")){
            userNameErrorText.value = "Is already taken!"
        }
    }

    private fun validateEmail(errorMessage: String){
        emailErrorText.value = if(!isNotEmpty(emailEditTextContent?.value?:""))
            errorMessage
        else
            null
        if(isEmailAlreadyTaken.equals("true")){
            emailErrorText.value = "Is already taken!"
        }
    }

    fun isNotEmpty(validText: String): Boolean{
        if(validText.isEmpty()|| validText == "null")
            return false

        return true
    }

    private fun validatePassword(errorEmptyMessage: String,errorRegexMessage:String){

            if (!isNotEmpty(passwordEditTextContent?.value ?: ""))
                passwordErrorText.value = errorEmptyMessage
            else {
                passwordEditTextContent.value?.let {
                    passwordErrorText.value = if (it.length < 8)
                        errorRegexMessage
                    else
                        errorRegexMessage

                    val passwordMatcher = Regex(passwordPattern)

                    passwordErrorText.value = if (passwordMatcher.find(it) == null)
                        errorRegexMessage
                    else
                        null
                }
            }
    }

    private fun validateConfirmPassword(errorEmptyMessage:String, errorMatchMessage: String){
        if(!isNotEmpty(confirmPasswordEditTextContent?.value?:""))
            confirmPasswordErrorText.value = errorEmptyMessage
        else {
            if (confirmPasswordEditTextContent.value.toString() != passwordEditTextContent.value.toString())
                confirmPasswordErrorText.value = errorMatchMessage
            else
                confirmPasswordErrorText.value = null
        }
    }

    fun checkValidation(errorsText: Map<String, String>):Boolean{
        validateUserName(errorsText["emptyError"]?:"null")
        validateEmail(errorsText["emptyError"]?:"null")
        validatePassword(errorsText["emptyError"]?:"null",errorsText["errorRegexMessage"]?:"null")
        validateConfirmPassword(errorsText["emptyError"]?:"null",errorsText["notMatchError"]?:"null")
        var check = true
        if(userNameErrorText.value !=null)
            check =false
        else if(emailErrorText.value != null)
            check = false
        else if(passwordErrorText.value !=null)
            check = false
        else if(confirmPasswordErrorText.value != null)
            check = false

        return  check
    }

    suspend fun checkIsUserNameAlreadyTaken() {
        isUserNameAlreadyTaken = userRepository.getValidation("name",userNameEditTextContent.value?:"userDefault")
    }

    suspend fun checkIsEmailAlreadyTaken() {
        isEmailAlreadyTaken = userRepository.getValidation("email",emailEditTextContent.value?:"emailDefault")
    }

}