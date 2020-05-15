package com.maskjs.korona_zakupy.viewmodels.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.RegisterResponseDto
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import com.maskjs.korona_zakupy.helpers.InputTextType
import com.maskjs.korona_zakupy.helpers.RegistrationPart
import kotlinx.coroutines.delay
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
    val firstNameErrorText = MutableLiveData<String?>()
    val lastNameErrorText = MutableLiveData<String?>()
    val addressErrorText = MutableLiveData<String?>()
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
            InputTextType.USER_NAME -> userNameErrorText.value = validateIsNotEmptyAndIsNotAlreadyTaken(userNameEditTextContent.value?:"",isUserNameAlreadyTaken,
                errorsText["emptyError"]?:"null",errorsText["userNameIsAlreadyTaken"]?:"" )

            InputTextType.EMAIL ->  emailErrorText.value = validateIsNotEmptyAndIsNotAlreadyTaken(emailEditTextContent.value?:"",isEmailAlreadyTaken,
                errorsText["emptyError"]?:"null",errorsText["emailNameIsAlreadyTaken"]?:"" )

            InputTextType.PASSWORD_REGISTER -> passwordErrorText.value =  validatePassword(passwordEditTextContent.value?:"",errorsText["emptyError"]?:"null",errorsText["errorRegexMessage"]?:"null")

            InputTextType.CONFIRM_PASSWORD_REGISTER -> confirmPasswordErrorText.value = validateConfirmPassword(confirmPasswordEditTextContent.value?:"",passwordEditTextContent.value?:"",
                errorsText["emptyError"]?:"null",errorsText["notMatchError"]?:"null")

            InputTextType.FIRST_NAME -> firstNameErrorText.value = validateIsNotEmpty(firstNameEditTextContent.value?:"",errorsText["emptyError"]?:"null")

            InputTextType.LAST_NAME -> lastNameErrorText.value = validateIsNotEmpty(lastNameEditTextContent.value?:"",errorsText["emptyError"]?:"null")

            InputTextType.ADDRESS ->  addressErrorText.value = validateIsNotEmpty(addressEditTextContent.value?:"",errorsText["emptyError"]?:"null")
        }
    }

    private fun validateIsNotEmpty(textToValidate: String, errorMessage: String) : String?{
        return if(!isNotEmpty(textToValidate))
            errorMessage
        else
            null
    }

    private fun validateIsNotEmptyAndIsNotAlreadyTaken(textToValidate: String,apiResponse:String,
       errorEmptyMessage: String, errorAlreadyTaken: String): String?{
        return if(!isNotEmpty(textToValidate))
            errorEmptyMessage
        else {
            if(apiResponse.equals("true"))
                errorAlreadyTaken
            else
                null
            }
    }

    fun isNotEmpty(validText: String): Boolean{
        if(validText.isEmpty()|| validText == "null")
            return false

        return true
    }

    private fun validatePassword(textToValidate: String, errorEmptyMessage: String,
         errorRegexMessage:String):String?{
        return if (!isNotEmpty(textToValidate))
            errorEmptyMessage
        else{
            if(textToValidate.length < 8)
                errorRegexMessage
            else{
                val passwordMatcher = Regex(passwordPattern)
                if(passwordMatcher.find(textToValidate) == null)
                    errorRegexMessage
                else
                    null
            }
        }
    }

    private fun validateConfirmPassword(textToValidate: String, textToCompare:String,
                                        errorEmptyMessage:String, errorMatchMessage: String):String?{
        return if(!isNotEmpty(textToValidate))
            errorEmptyMessage
        else{
            if(textToValidate!=textToCompare)
                errorMatchMessage
            else
                null
        }
    }

    fun checkValidation( registrationPart: RegistrationPart,errorMessage: Map<String,String>):Boolean{
        return when(registrationPart){
            RegistrationPart.PART_2 ->{
                validateReg1UiElements(errorMessage)
                checkValidationReg1UiElements()
            }
            RegistrationPart.PART_3 ->{
                validateReg2UiElements(errorMessage)
                checkValidationReg2UiElements()
            }
            else -> false
        }
    }

    private fun validateReg1UiElements(errorMessage: Map<String,String>){
        if(!isNotEmpty(userNameEditTextContent.value?:"null"))
            userNameErrorText.value = errorMessage["emptyError"]
        if(!isNotEmpty(emailEditTextContent.value?:"null"))
            emailErrorText.value = errorMessage["emptyError"]
        if(!isNotEmpty(passwordEditTextContent.value?:"null"))
            passwordErrorText.value = errorMessage["emptyError"]
        if(!isNotEmpty(confirmPasswordEditTextContent.value?:"null"))
            confirmPasswordErrorText.value = errorMessage["emptyError"]
    }

    private fun checkValidationReg1UiElements():Boolean{

        if(userNameErrorText.value !=null)
            return false
        else if(emailErrorText.value != null)
          return false
        else if(passwordErrorText.value !=null)
          return false
        else if(confirmPasswordErrorText.value != null)
            return false

        return true
    }

    private fun validateReg2UiElements(errorMessage: Map<String,String>){
        if(!isNotEmpty(firstNameEditTextContent.value?:"null"))
            firstNameErrorText.value = errorMessage["emptyError"]
        if(!isNotEmpty(lastNameEditTextContent.value?:"null"))
            lastNameErrorText.value = errorMessage["emptyError"]
        if(!isNotEmpty(addressEditTextContent.value?:"null"))
            addressErrorText.value = errorMessage["emptyError"]
    }

    private fun checkValidationReg2UiElements(): Boolean{
        if(firstNameErrorText.value !=null)
            return false
        else if(lastNameErrorText.value != null)
            return false
        else if(addressErrorText.value !=null)
            return false

        return true
    }

    suspend fun checkIsUserNameAlreadyTaken() {
        delay(1500)
        isUserNameAlreadyTaken = userRepository.getValidation("name",userNameEditTextContent.value?:"userDefault")
    }

    suspend fun checkIsEmailAlreadyTaken() {
        delay(1500)
        isEmailAlreadyTaken = userRepository.getValidation("email",emailEditTextContent.value?:"emailDefault")
    }

}