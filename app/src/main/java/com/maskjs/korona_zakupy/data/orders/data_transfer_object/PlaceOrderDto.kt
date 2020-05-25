package com.maskjs.korona_zakupy.data.orders.data_transfer_object

data class PlaceOrderDto(
    val userId:String,
    val orderType: String,
    val products: ArrayList<String>
) {
}