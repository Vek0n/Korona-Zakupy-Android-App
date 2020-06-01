package com.maskjs.korona_zakupy.ui.person_in_quarantine.active.choose_order_type

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ChooseOrderTypeViewModel : ViewModel() {
    var orderType = "unDefined"

    fun chooseOrderType(orderType: OrderType){
        this.orderType = orderType.toString().toLowerCase(Locale.ROOT).capitalize()
        if(orderType == OrderType.GROCERY18PLUS)
            this.orderType = "Grocery18Plus" //awful, but it works
    }

    fun validateOrderType() : Boolean{
        return orderType != "unDefined"
    }

 enum class OrderType{
    DOG, GROCERY, GROCERY18PLUS, PHARMACY
}

}