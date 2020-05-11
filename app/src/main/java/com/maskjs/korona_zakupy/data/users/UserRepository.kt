package com.maskjs.korona_zakupy.data.users

import com.maskjs.korona_zakupy.data.interfaces.IRepository

class UserRepository<T: Any>(private  val userDao: UserDao): IRepository<T> {


    suspend fun registerUser(registerUserDto: T): RegisterResponseDto? {
        val registerJsonResponse
                = userDao.userRegister(
                parseObjectToJson(registerUserDto))

        return parseJsonToRegisterResponseDto(registerJsonResponse)
    }

   suspend fun loginUser(loginUserDto: T): String
            = userDao.userLogin(parseObjectToJson(loginUserDto))

   suspend fun getRole(userId: String, headerToken: String): String
            = userDao.getRole(userId, headerToken)
}