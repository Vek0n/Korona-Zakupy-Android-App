package com.maskjs.korona_zakupy.ui.person_in_quarantine.active.choose_order_type

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ChooseOrderTypeViewModel : ViewModel() {
    var orderType = "unDefined"
    val response = MutableLiveData<String>()

    init {
        response.value =""
    }

    fun chooseOrderType(orderType: OrderType){
        this.orderType = orderType.toString().toLowerCase(Locale.ROOT).capitalize()
        response.value = "Selected: $orderType"
    }

    fun validateOrderType() : Boolean{
        return orderType != "unDefined"
    }

 enum class OrderType{
    DOG, GROCERY, GROCERY18PLUS, PHARMACY
}

}