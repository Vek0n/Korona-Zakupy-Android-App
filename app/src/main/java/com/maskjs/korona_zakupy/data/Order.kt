package com.maskjs.korona_zakupy.data

data class Order(
    val orderId: Long,
    val name: String,
    val photo: String,
    val rating: Float,
    val orderType: String) {

}