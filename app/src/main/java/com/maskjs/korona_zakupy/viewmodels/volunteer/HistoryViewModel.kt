package com.maskjs.korona_zakupy.viewmodels.volunteer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.orders.GetOrderDto
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import okhttp3.OkHttpClient

class HistoryViewModel : ViewModel() {

    suspend fun getHistoryOrdersFromRepository(): ArrayList<GetOrderDto>{
        return OrderRepository<GetOrderDto>(OrderDao(OkHttpClient()))
            .getAllOrdersOfUser("dc4d373d-f329-4b4d-afd9-0903520d86d6")
    }
}