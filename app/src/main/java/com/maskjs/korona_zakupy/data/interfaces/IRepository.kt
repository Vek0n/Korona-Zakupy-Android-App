package com.maskjs.korona_zakupy.data.interfaces

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.PlaceOrderDto
import com.maskjs.korona_zakupy.data.users.RegisterResponseDto
import java.lang.reflect.Type


interface IRepository<T> {

    suspend fun parseJsonToObject(json:String): ArrayList<T> {
        val collectionType: Type = object : TypeToken<ArrayList<T?>?>() {}.type
        return Gson().fromJson(json, collectionType) as ArrayList<T>
    }

    fun parseObjectToJson(dto: T): String{
        return Gson().toJson(dto)
    }
//tfu
    fun parseJsonToRegisterResponseDto(json: String): List<RegisterResponseDto>{
        val collectionType: Type = object : TypeToken<List<RegisterResponseDto?>?>() {}.type
        return Gson().fromJson(json, collectionType) as List<RegisterResponseDto>
    }

}

