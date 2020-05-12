package com.maskjs.korona_zakupy.viewmodels.volunteer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import kotlinx.coroutines.*
import okhttp3.OkHttpClient


class ActiveOrdersViewModel : ViewModel() {

    suspend fun getActiveOrdersFromRepository(): ArrayList<GetOrderDto>{
        return OrderRepository<GetOrderDto>(OrderDao(OkHttpClient()))
            .getAllOrdersOfUser("dc4d373d-f329-4b4d-afd9-0903520d86d6")
    }

    suspend fun unAcceptOrder(userId: String, orderId: Long): String{
        return OrderRepository<Any>(OrderDao(OkHttpClient()))
            .unAcceptOrder(userId, orderId)
    }

    suspend fun completeOrder(userId: String, orderId: Long): String{
        return OrderRepository<Any>(OrderDao(OkHttpClient()))
            .confirmOrder(userId, orderId)
    }

}