package com.maskjs.korona_zakupy.data.interfaces

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.PlaceOrderDto
import java.lang.reflect.Type


interface IOrderRepository {

    fun parseJsonToOrder(json:String): List<GetOrderDto> {
        val collectionType: Type = object : TypeToken<List<GetOrderDto?>?>() {}.type
        return Gson().fromJson(json, collectionType) as List<GetOrderDto>
    }


    fun parseOrderToJson(placeOrderDto: PlaceOrderDto): String{
        return Gson().toJson(placeOrderDto)
    }
}