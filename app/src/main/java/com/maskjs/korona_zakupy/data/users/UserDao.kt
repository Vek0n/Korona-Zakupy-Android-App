package com.maskjs.korona_zakupy.data.users

import com.maskjs.korona_zakupy.data.interfaces.IDataAccessObject
import okhttp3.OkHttpClient

class UserDao(private val client: OkHttpClient): IDataAccessObject {

    suspend  fun userRegister(userRegisterJsonString: String): String
            = APIPostRequest(userRegisterJsonString, "http://korona-zakupy.hostingasp.pl/api/user/register",client)



    suspend  fun userLogin(userLoginJsonString: String): String
            = APIPostRequest(userLoginJsonString, "http://korona-zakupy.hostingasp.pl/api/user/login", client)


    suspend  fun getRole(userId: String, headerToken:String): String
            = APIGetRequestAuth("http:///korona-zakupy.hostingasp.pl/api/user/role/$userId", headerToken, client)


    suspend fun getValidation(resource:String,name:String):String
            = APIGetRequest("http://korona-zakupy.hostingasp.pl/api/user/exist/$resource/$name",client)


    suspend fun rateUser(userId: String, rating: Float)
            = APIPostRequest("", "http://korona-zakupy.hostingasp.pl/api/user/rate/$userId/$rating", client)

}