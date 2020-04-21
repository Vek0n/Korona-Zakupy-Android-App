package com.maskjs.korona_zakupy.data.users

import com.maskjs.korona_zakupy.data.interfaces.IDataAccessObject
import okhttp3.OkHttpClient

class UserDao(private val client: OkHttpClient): IDataAccessObject {

    fun userRegister(userRegisterJsonString: String): String{
        return APIPostRequest(userRegisterJsonString, "http://10.0.2.2:5001/api/user/register",client)
    }


    fun userLogin(userLoginJsonString: String): String{
        return APIPostRequest(userLoginJsonString, "http://10.0.2.2:5001/api/user/login", client)
    }


    fun getRole(userId: String, headerToken:String): String{
        return APIGetRequestAuth("http://10.0.2.2:5001/api/user/role/$userId", headerToken, client)
    }
}