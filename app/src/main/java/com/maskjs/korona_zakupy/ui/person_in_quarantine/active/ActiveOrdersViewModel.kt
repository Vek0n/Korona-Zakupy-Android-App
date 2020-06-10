package com.maskjs.korona_zakupy.ui.person_in_quarantine.active

import androidx.lifecycle.ViewModel
import com.google.gson.internal.LinkedTreeMap
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import okhttp3.OkHttpClient

class ActiveOrdersViewModel(
    private val getOrderRepository: OrderRepository<GetOrderDto>,
    private val editOrderRepository: OrderRepository<Any>
) : ViewModel() {

    suspend fun getActiveOrdersFromRepository(userId: String, token: String): ArrayList<GetOrderDto>{
        val allOrders = getOrderRepository
            .getAllOrdersOfUser(userId, token)
        val activeOrders = arrayListOf<LinkedTreeMap<*, *>>()

        for (i in 0 until allOrders.size){
            val order = allOrders[i] as LinkedTreeMap<*,*>
            val orderStatus = order["orderStatus"]
            if(orderStatus == "InProgress" || orderStatus == "AwaitingConfirmation" || orderStatus == "Avalible") activeOrders.add(order)
        }
        return activeOrders as ArrayList<GetOrderDto>
    }

    suspend fun completeOrder(userId: String, orderId: Long, token: String): String{
        return editOrderRepository
            .confirmOrder(userId, orderId, token)
    }

    suspend fun deleteOrder(orderId: Long, token: String): String{
        return editOrderRepository
            .deleteOrder(orderId, token)
    }
}