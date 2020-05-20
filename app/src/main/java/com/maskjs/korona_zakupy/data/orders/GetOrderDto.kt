package com.maskjs.korona_zakupy.data.orders

//import kotlin.collections.ArrayList

data class GetOrderDto(
    val orderId: Long,
    val orderDate: String,
    val orderType: String,
    val products: ArrayList<String>,
    val orderStatus: String,
    val usersInfo: ArrayList<UsersInfoModel>
) {}