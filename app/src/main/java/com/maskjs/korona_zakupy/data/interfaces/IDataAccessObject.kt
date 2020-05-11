package com.maskjs.korona_zakupy.data.interfaces

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URL
import okhttp3.RequestBody.Companion.toRequestBody

interface IDataAccessObject{

    suspend fun APIGetRequest(urlString: String, client: OkHttpClient):String {
        val url = URL(urlString)
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()

        return response.body!!.string()
    }


   suspend fun APIGetRequestAuth(urlString: String, headerToken: String, client: OkHttpClient):String {
        val url = URL(urlString)
        val request = Request.Builder()
            .addHeader("Authorization", "Bearer $headerToken")
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()

        return response.body!!.string()
    }


    suspend fun APIPostRequest(postBody:String, urlString: String, client: OkHttpClient):String {

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


    suspend fun APIPostRequestAuth(postBody:String, urlString: String, headerToken: String , client: OkHttpClient):String {

        val request = Request.Builder()
            .url(urlString)
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization", "Bearer $headerToken")
            .post(postBody.toRequestBody())
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body!!.string()
        }
    }
}