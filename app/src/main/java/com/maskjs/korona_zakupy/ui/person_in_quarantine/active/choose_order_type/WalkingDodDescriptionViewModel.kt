package com.maskjs.korona_zakupy.ui.person_in_quarantine.active.choose_order_type

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import com.maskjs.korona_zakupy.data.orders.data_transfer_object.PlaceOrderDto
import okhttp3.OkHttpClient

class WalkingDodDescriptionViewModel : ViewModel() {
    val walkingTheDogDescription = MutableLiveData<String>()
    private val orderRepository = OrderRepository<PlaceOrderDto>(orderDao = OrderDao(OkHttpClient()))

     suspend fun placeOrder(userId: String, token:String, orderType: String){
        orderRepository.placeOrder(
            PlaceOrderDto(
                userId,
                orderType,
               arrayListOf(walkingTheDogDescription.value ?: "")
            ),token)
    }


}