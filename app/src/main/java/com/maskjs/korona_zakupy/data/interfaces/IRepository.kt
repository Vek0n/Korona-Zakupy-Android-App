package com.maskjs.korona_zakupy.data.interfaces

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.PlaceOrderDto
import com.maskjs.korona_zakupy.data.users.LoginResponseDto
import com.maskjs.korona_zakupy.data.users.RegisterResponseDto
import java.lang.reflect.Type


interface IRepository<T> {

    suspend fun parseJsonToObject(json:String): ArrayList<T> {
        val collectionType: Type = object : TypeToken<ArrayList<T?>?>() {}.type
        return Gson().fromJson(json, collectionType) as ArrayList<T>
    }

   suspend fun parseObjectToJson(dto: T): String{
        return Gson().toJson(dto)
    }

    suspend fun parseJsonToRegisterResponseDto(json: String): RegisterResponseDto{
        val collectionType: Type = object : TypeToken<RegisterResponseDto?>() {}.type
        return Gson().fromJson(json, collectionType) as RegisterResponseDto
    }

    suspend fun parseJsonToLoginResponseDto(json: String): LoginResponseDto{
        val collectionType: Type = object : TypeToken<LoginResponseDto?>() {}.type
        return Gson().fromJson(json, collectionType) as LoginResponseDto
    }

}

