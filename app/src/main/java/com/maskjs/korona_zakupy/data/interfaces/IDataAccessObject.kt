package com.maskjs.korona_zakupy.data.interfaces

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URL
import okhttp3.RequestBody.Companion.toRequestBody

interface IDataAccessObject {

    fun APIGetRequest(urlString: String, client: OkHttpClient):String {
        val url = URL(urlString)
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()

        return response.body!!.string()
    }


    fun APIGetRequestAuth(urlString: String, headerAuth: String, headerToken: String, client: OkHttpClient):String {
        val url = URL(urlString)
        val request = Request.Builder()
            .addHeader(headerAuth, "Bearer $headerToken")
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()

        return response.body!!.string()
    }


    fun APIPostRequest(postBody:String, urlString: String, client: OkHttpClient):String {
        //val postBody = "{\"Email\":\"6999@gmail.com\",\"Password\":\"hkt8zd6vG?!\",\"ConfirmPassword\":\"hkt8zd6vG?!\",\"Address\":\"Adama mickiewicza 25\",\"FirstName\":\"Damian\",\"LastName\":\"Litkowski\"}"

        val request = Request.Builder()
            .url(urlString)
            .addHeader("Content-Type","application/json")
            .post(postBody.toRequestBody())
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body!!.string()
        }
    }
}