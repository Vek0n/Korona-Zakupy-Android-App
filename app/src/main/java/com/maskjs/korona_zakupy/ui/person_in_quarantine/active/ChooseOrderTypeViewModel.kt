package com.maskjs.korona_zakupy.ui.person_in_quarantine.active

import androidx.lifecycle.ViewModel
import java.util.*

class ChooseOrderTypeViewModel : ViewModel() {
    var orderType = "unDefined"

    fun chooseOrderType(orderType: OrderType){
        this.orderType = orderType.toString().toLowerCase(Locale.ROOT).capitalize()
    }

 enum class OrderType{
    DOG, GROCERY, GROCERY18PLUS, PHARMACY
}

}