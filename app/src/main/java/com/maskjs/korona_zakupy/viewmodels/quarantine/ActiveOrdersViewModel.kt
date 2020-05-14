package com.maskjs.korona_zakupy.viewmodels.quarantine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.internal.LinkedTreeMap
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import okhttp3.OkHttpClient

class ActiveOrdersViewModel : ViewModel() {

    suspend fun getActiveOrdersFromRepository(userId: String): ArrayList<GetOrderDto>{
        val allOrders = OrderRepository<GetOrderDto>(OrderDao(OkHttpClient()))
            .getAllOrdersOfUser(userId)
        val activeOrders = arrayListOf<LinkedTreeMap<*, *>>()

        for (i in 0 until allOrders.size){
            val order = allOrders[i] as LinkedTreeMap<*,*>
            val orderStatus = order["orderStatus"]
            if(orderStatus == "InProgress" || orderStatus == "AwaitingConfirmation") activeOrders.add(order)
        }
        return activeOrders as ArrayList<GetOrderDto>
    }

    suspend fun completeOrder(userId: String, orderId: Long): String{
        return OrderRepository<Any>(OrderDao(OkHttpClient()))
            .confirmOrder(userId, orderId)
    }
}