package com.maskjs.korona_zakupy.data.orders

import com.maskjs.korona_zakupy.data.interfaces.IRepository

class OrderRepository<T: Any>(private val orderDao: OrderDao): IRepository<T>{

    suspend fun getActiveOrders(): ArrayList<T>
            = parseJsonToObject(
            orderDao.getActiveOrders())


    suspend fun getAllActiveOrdersOfUser(userId:String): ArrayList<T>
            = parseJsonToObject(
            orderDao.getAllActiveOrdersOfUser(userId))


    suspend fun getAllOrdersOfUser(userId: String): ArrayList<T>
            = parseJsonToObject(orderDao.getAllOrdersOfUser(userId))


    suspend fun placeOrder(orderDto: T): String
            = orderDao.placeOrder(parseObjectToJson(orderDto))


    suspend fun acceptOrder(userId: String, orderId: Long): String
            = orderDao.acceptOrder(orderId, userId)


    suspend fun unAcceptOrder(userId: String, orderId: Long): String
            = orderDao.unAcceptOrder(orderId, userId)


    suspend fun confirmOrder(userId: String, orderId: Long): String
            = orderDao.confirmOrder(orderId, userId)


    suspend fun cancelConfirmOrder(userId: String, orderId: Long): String
            = orderDao.cancelConfirmOrder(orderId, userId)


    suspend fun checkConfirmation(orderId: Long): Boolean
            = orderDao.checkConfirmation(orderId)
                .toBoolean()

    suspend fun finishOrder(orderId: Long): String
            = orderDao.finishOrder(orderId)
}