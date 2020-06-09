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

class RegisterPart3ViewModel(private val errorMessages: Map<String, String>,
                             private val userRepository: UserRepository<RegisterUserDto>,
                             private val registerUserDto: RegisterUserDto,
                             val firstNameInputLayoutModel: InputTextLayoutModel,
                             val lastNameInputLayoutModel: InputTextLayoutModel,
                             val addressInputTextLayoutModel: InputTextLayoutModel
):  ViewModel() {
    lateinit var userRegisterResponseDto: RegisterResponseDto

    suspend fun register() {
        userRegisterResponseDto = userRepository.registerUser(registerUserDto)!! //can be null ? - instant exception!!!
    }

    fun validateFirstName(){
        firstNameInputLayoutModel.validate(errorMessages)
    }

    fun validateLastName(){
        lastNameInputLayoutModel.validate(errorMessages)
    }

    fun validateAddress(){
        addressInputTextLayoutModel.validate(errorMessages)
    }


    fun checkValidation() : Boolean{
        firstNameInputLayoutModel.validate(errorMessages)
        lastNameInputLayoutModel.validate(errorMessages)
        addressInputTextLayoutModel.validate(errorMessages)

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
        registerUserDto.firstName = firstNameInputLayoutModel.textContent.value!!
        registerUserDto.lastName = lastNameInputLayoutModel.textContent.value!!
        registerUserDto.address = addressInputTextLayoutModel.textContent.value!!
    }
}