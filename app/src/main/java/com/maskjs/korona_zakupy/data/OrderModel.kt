package com.maskjs.korona_zakupy.data

import java.util.*

data class OrderModel(
    val orderId: String,
    val groceryLost: Array<String>
) {}