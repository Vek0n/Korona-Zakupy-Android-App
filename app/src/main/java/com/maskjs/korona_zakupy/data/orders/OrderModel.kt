package com.maskjs.korona_zakupy.data.orders

//import kotlin.collections.ArrayList

data class OrderModel(
    val orderId: Long,
    val orderDate: String,
    val products: ArrayList<String>,
    val isFinished:Boolean,
    val isActive:Boolean,
    val usersInfo:ArrayList<UsersInfoModel>
) {}