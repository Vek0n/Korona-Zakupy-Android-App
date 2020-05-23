package com.maskjs.korona_zakupy.viewmodels.volunteer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import okhttp3.OkHttpClient

class AvailableOrdersViewModel : ViewModel() {


    suspend fun getAvailableOrdersFromRepository(token: String): ArrayList<GetOrderDto>{
        return OrderRepository<GetOrderDto>(OrderDao(OkHttpClient()))
            .getActiveOrders(token)
    }

    suspend fun acceptOrder(userId:String, orderId: Long, token: String): String{
        return OrderRepository<Any>(OrderDao(OkHttpClient()))
            .acceptOrder(userId, orderId, token)
    }
}