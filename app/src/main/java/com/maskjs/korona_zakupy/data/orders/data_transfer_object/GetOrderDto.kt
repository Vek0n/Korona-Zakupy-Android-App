package com.maskjs.korona_zakupy.data.orders.data_transfer_object

import com.maskjs.korona_zakupy.data.users.data_transfer_object.UserDto

//import kotlin.collections.ArrayList

data class GetOrderDto(
    val orderId: Long,
    val orderDate: String,
    val orderType: String,
    val products: ArrayList<String>,
    val orderStatus: String,
    val usersInfo: ArrayList<UserDto>
) {}