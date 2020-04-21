package com.maskjs.korona_zakupy.data.orders

import com.maskjs.korona_zakupy.data.interfaces.IOrderRepository

class OrderRepository(private val orderDao: OrderDao): IOrderRepository{

    fun getActiveOrders():List<GetOrderDto>
            = parseJsonToOrder(
            orderDao.getActiveOrders())


    fun getAllActiveOrdersOfUser(userId:String):List<GetOrderDto>
            = parseJsonToOrder(
            orderDao.getAllActiveOrdersOfUser(userId))


    fun getAllOrdersOfUser(userId: String): List<GetOrderDto>
            = parseJsonToOrder(orderDao.getAllOrdersOfUser(userId))


    fun placeOrder(orderDto: PlaceOrderDto): String
            = orderDao.placeOrder(parseOrderToJson(orderDto))


    fun acceptOrder(userId: String, orderId: Long): String
            = orderDao.acceptOrder(orderId, userId)


    fun confirmOrder(userId: String, orderId: Long): String
            = orderDao.confirmOrder(orderId, userId)


    fun cancelConfirmOrder(userId: String, orderId: Long): String
            = orderDao.cancelConfirmOrder(orderId, userId)


    fun checkConfirmation(orderId: Long): Boolean
            = orderDao.checkConfirmation(orderId)
                .toBoolean()

    fun finishOrder(orderId: Long): String
            = orderDao.finishOrder(orderId)
}