package com.maskjs.korona_zakupy.data.users

import com.maskjs.korona_zakupy.data.interfaces.IDataAccessObject
import okhttp3.OkHttpClient

class UserDao(private val client: OkHttpClient): IDataAccessObject {

    suspend  fun userRegister(userRegisterJsonString: String): String{
        return APIPostRequest(userRegisterJsonString, "http://korona-zakupy.hostingasp.pl/api/user/register",client)
    }


    suspend  fun userLogin(userLoginJsonString: String): String{
        return APIPostRequest(userLoginJsonString, "http:///korona-zakupy.hostingasp.pl/api/user/login", client)
    }


    suspend  fun getRole(userId: String, headerToken:String): String{
        return APIGetRequestAuth("http:///korona-zakupy.hostingasp.pl/api/user/role/$userId", headerToken, client)
    }
}