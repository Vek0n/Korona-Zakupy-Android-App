package com.maskjs.korona_zakupy.data.orders

import com.maskjs.korona_zakupy.data.interfaces.IRepository

class OrderRepository<T: Any>(private val orderDao: OrderDao): IRepository<T>{

    fun getActiveOrders(): List<T>
            = parseJsonToObject(
            orderDao.getActiveOrders())


    fun getAllActiveOrdersOfUser(userId:String): List<T>
            = parseJsonToObject(
            orderDao.getAllActiveOrdersOfUser(userId))


    fun getAllOrdersOfUser(userId: String): List<T>
            = parseJsonToObject(orderDao.getAllOrdersOfUser(userId))


    fun placeOrder(orderDto: T): String
            = orderDao.placeOrder(parseObjectToJson(orderDto))


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