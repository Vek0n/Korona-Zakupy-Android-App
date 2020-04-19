package com.maskjs.korona_zakupy.data.interfaces

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maskjs.korona_zakupy.data.orders.OrderModel
import java.lang.reflect.Type


interface IOrderRepository {

    fun parseJsonToOrder(json:String): List<OrderModel> {

        val collectionType: Type = object : TypeToken<List<OrderModel?>?>() {}.type

        return Gson().fromJson(json, collectionType) as List<OrderModel>

    }
}