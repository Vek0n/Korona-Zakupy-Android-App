package com.maskjs.korona_zakupy.data.users

import com.maskjs.korona_zakupy.data.interfaces.IRepository

class UserRepository<T: Any>(private  val userDao: UserDao): IRepository<T> {


    fun registerUser(registerUserDto: T): RegisterResponseDto? {
        val registerJsonResponse
                = userDao.userRegister(
                parseObjectToJson(registerUserDto))

        return parseJsonToRegisterResponseDto(registerJsonResponse).firstOrNull()
    }

    fun loginUser(loginUserDto: T): String
            = userDao.userLogin(parseObjectToJson(loginUserDto))

    fun getRole(userId: String, headerToken: String): String
            = userDao.getRole(userId, headerToken)
}