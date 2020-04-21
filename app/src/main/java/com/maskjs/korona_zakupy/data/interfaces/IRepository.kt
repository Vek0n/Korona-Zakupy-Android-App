package com.maskjs.korona_zakupy.data.interfaces

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.PlaceOrderDto
import java.lang.reflect.Type


interface IRepository<T:Any> {

    fun parseJsonToObject(json:String): List<T> {
        val collectionType: Type = object : TypeToken<List<T?>?>() {}.type
        return Gson().fromJson(json, collectionType) as List<T>
    }


    fun parseObjectToJson(dto: T): String{
        return Gson().toJson(dto)
    }
}

