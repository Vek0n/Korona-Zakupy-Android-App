package com.maskjs.korona_zakupy.viewmodels.register

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.RegisterResponseDto
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import com.maskjs.korona_zakupy.helpers.SharedRegisterDto
import com.maskjs.korona_zakupy.viewmodels.input_text_layout.InputTextLayoutViewModel
import com.maskjs.korona_zakupy.viewmodels.input_text_layout.PlainTextInputTextLayoutViewModel
import okhttp3.OkHttpClient

class RegisterPartThreeViewModel :  ViewModel() {
    val firstNameInputLayoutViewModel: InputTextLayoutViewModel =
        PlainTextInputTextLayoutViewModel()
    val lastNameInputLayoutViewModel: InputTextLayoutViewModel =
        PlainTextInputTextLayoutViewModel()
    val addressInputTextLayoutViewModel: InputTextLayoutViewModel =
        PlainTextInputTextLayoutViewModel()
    lateinit var userRegisterResponseDto: RegisterResponseDto
    private val userRepository = UserRepository<RegisterUserDto>(userDao = UserDao(client = OkHttpClient()))
    private val registerUserDto = SharedRegisterDto

    suspend fun register() {
        userRegisterResponseDto = userRepository.registerUser(registerUserDto.registerUserDto)!! //can be null ? - instant exception!!!
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


    fun checkValidation(errorMessage: Map<String,String>) : Boolean{
        firstNameInputLayoutViewModel.validate(errorMessage)
        lastNameInputLayoutViewModel.validate(errorMessage)
        addressInputTextLayoutViewModel.validate(errorMessage)

        return isCorrectValidation()
    }

    private fun isCorrectValidation(): Boolean{
        if(firstNameInputLayoutViewModel.errorContent.value !=null)
            return false
        else if(lastNameInputLayoutViewModel.errorContent.value != null)
            return false
        else if(addressInputTextLayoutViewModel.errorContent.value !=null)
            return false

        return true
    }

     fun save(){
        registerUserDto.registerUserDto.firstName = firstNameInputLayoutViewModel.textContent.value!!
        registerUserDto.registerUserDto.lastName = lastNameInputLayoutViewModel.textContent.value!!
        registerUserDto. registerUserDto.address = addressInputTextLayoutViewModel.textContent.value!!
    }
}