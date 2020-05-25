package com.maskjs.korona_zakupy.data.users

import com.maskjs.korona_zakupy.data.interfaces.IRepository
import com.maskjs.korona_zakupy.data.users.api_communication.LoginResponseDto
import com.maskjs.korona_zakupy.data.users.api_communication.RegisterResponseDto
import com.maskjs.korona_zakupy.data.users.api_communication.UserDao

class UserRepository<T: Any>(private  val userDao: UserDao): IRepository<T> {


    suspend fun registerUser(registerUserDto: T): RegisterResponseDto? {
        val registerJsonResponse
                = userDao.userRegister(
                parseObjectToJson(registerUserDto))

        return parseJsonToRegisterResponseDto(registerJsonResponse)
    }

   suspend fun loginUser(loginUserDto: T): LoginResponseDto {
       val loginJsonResponse = userDao.userLogin(
           parseObjectToJson(loginUserDto))

       return parseJsonToLoginResponseDto(loginJsonResponse)
   }


    suspend fun getValidation(resource:String,name:String):String
            = userDao.getValidation(resource,name)


    suspend fun getRole(userId: String, headerToken: String): String
            = userDao.getRole(userId, headerToken)


    suspend fun rateUser(userId: String, rating: Double, token: String)
            = userDao.rateUser(userId, rating, token)
}