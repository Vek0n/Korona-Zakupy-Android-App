package com.maskjs.korona_zakupy.ui.volunteer.available

import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.GetOrderDto
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