package com.maskjs.korona_zakupy.viewmodels.register

import android.R.attr.password
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.RegisterResponseDto
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import com.maskjs.korona_zakupy.helpers.InputTextType
import okhttp3.OkHttpClient
import java.util.*

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
    var passwordErrorText = MutableLiveData<String?>()
    var confirmPasswordErrorText = MutableLiveData<String?>()
    var emailErrorText = MutableLiveData<String?>()

    private val userRepository = UserRepository<RegisterUserDto>(userDao = UserDao(client = OkHttpClient()))
    private val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=?!])(?=\\S+$).{4,}$"

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

    fun validate(textToValidate:String,textInputTextType: InputTextType,errorsText: Map<String,String>){

        userNameErrorText.value = if(!isNotEmpty(textToValidate))
            errorsText["emptyError"]
        else
            null

        when(textInputTextType){
            InputTextType.PASSWORD_REGISTER -> refactoredCheckPassword(errorsText["errorRegexMessage"]?:"null")
            InputTextType.CONFIRM_PASSWORD_REGISTER ->refactoredCheckConfirmPassword(errorsText["notMatchError"]?:"null")
            else -> return
        }
    }

    fun isNotEmpty(validText: String): Boolean{
        if(validText.isEmpty()|| validText == "null")
            return false

        return true
    }

    fun refactoredCheckPassword( errorRegexMessage:String){
        passwordEditTextContent.value?.let {
            passwordErrorText.value = if(it.length < 8)
                errorRegexMessage
            else
                errorRegexMessage

            val passwordMatcher = Regex(passwordPattern)

            passwordErrorText.value = if(passwordMatcher.find(it) == null)
                errorRegexMessage
            else
                null
        }
    }

    fun  checkPassword(): Boolean{
        passwordEditTextContent.value?.let {
            if(it.length < 8)
                return false

            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(it) != null
        } ?: return false

    }

    fun refactoredCheckConfirmPassword(errorMessage: String){
        if(confirmPasswordEditTextContent.value.toString() != passwordEditTextContent.value.toString())
          confirmPasswordErrorText.value =  errorMessage

        confirmPasswordErrorText.value = null
    }


    fun checkConfirmPassword(): Boolean{
        if(confirmPasswordEditTextContent.value.toString() != passwordEditTextContent.value.toString())
            return false

        return true
    }
}