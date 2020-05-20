package com.maskjs.korona_zakupy.viewmodels.quarantine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.internal.LinkedTreeMap
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import com.maskjs.korona_zakupy.data.users.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import okhttp3.OkHttpClient

class HistoryViewModel : ViewModel() {

    suspend fun getHistoryOrdersFromRepository(userId: String, token: String): ArrayList<GetOrderDto>{
        val allOrders = OrderRepository<GetOrderDto>(OrderDao(OkHttpClient()))
            .getAllOrdersOfUser(userId, token)

        val historyOrders = arrayListOf<LinkedTreeMap<*, *>>()

        for (i in 0 until allOrders.size){
            val order = allOrders[i] as LinkedTreeMap<*, *>
            if(order["orderStatus"] == "Finished") historyOrders.add(order)
        }
        return historyOrders as ArrayList<GetOrderDto>
    }

    suspend fun sendReview(userId: String, rating: Double, token: String)
        = UserRepository<Any>(UserDao(OkHttpClient())).rateUser(userId, rating, token)

}