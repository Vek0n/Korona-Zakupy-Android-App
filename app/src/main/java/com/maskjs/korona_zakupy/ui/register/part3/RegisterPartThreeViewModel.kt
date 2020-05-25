package com.maskjs.korona_zakupy.ui.register.part3

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.users.api_communication.RegisterResponseDto
import com.maskjs.korona_zakupy.data.users.data_transfer_object.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.api_communication.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import com.maskjs.korona_zakupy.utils.SharedRegisterDto
import com.maskjs.korona_zakupy.data.layoutModels.InputTextLayoutModel
import com.maskjs.korona_zakupy.data.layoutModels.PlainTextInputTextLayoutModel
import okhttp3.OkHttpClient

class RegisterPartThreeViewModel :  ViewModel() {
    val firstNameInputLayoutModel: InputTextLayoutModel =
        PlainTextInputTextLayoutModel()
    val lastNameInputLayoutModel: InputTextLayoutModel =
        PlainTextInputTextLayoutModel()
    val addressInputTextLayoutModel: InputTextLayoutModel =
        PlainTextInputTextLayoutModel()
    lateinit var userRegisterResponseDto: RegisterResponseDto
    private val userRepository = UserRepository<RegisterUserDto>(userDao = UserDao(
        client = OkHttpClient()
    )
    )
    private val registerUserDto = SharedRegisterDto

    suspend fun register() {
        userRegisterResponseDto = userRepository.registerUser(registerUserDto.registerUserDto)!! //can be null ? - instant exception!!!
    }

    fun validateFirstName(errorMessages: Map<String, String>){
        firstNameInputLayoutModel.validate(errorMessages)
    }

    fun validateLastName(errorMessages: Map<String, String>){
        lastNameInputLayoutModel.validate(errorMessages)
    }

    fun validateAddress(errorMessages: Map<String, String>){
        addressInputTextLayoutModel.validate(errorMessages)
    }


    fun checkValidation(errorMessage: Map<String,String>) : Boolean{
        firstNameInputLayoutModel.validate(errorMessage)
        lastNameInputLayoutModel.validate(errorMessage)
        addressInputTextLayoutModel.validate(errorMessage)

        return isCorrectValidation()
    }

    private fun isCorrectValidation(): Boolean{
        if(firstNameInputLayoutModel.errorContent.value !=null)
            return false
        else if(lastNameInputLayoutModel.errorContent.value != null)
            return false
        else if(addressInputTextLayoutModel.errorContent.value !=null)
            return false

        return true
    }

     fun save(){
        registerUserDto.registerUserDto.firstName = firstNameInputLayoutModel.textContent.value!!
        registerUserDto.registerUserDto.lastName = lastNameInputLayoutModel.textContent.value!!
        registerUserDto. registerUserDto.address = addressInputTextLayoutModel.textContent.value!!
    }
}