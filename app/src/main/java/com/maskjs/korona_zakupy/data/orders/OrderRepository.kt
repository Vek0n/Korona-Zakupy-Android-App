package com.maskjs.korona_zakupy.data.orders

import com.maskjs.korona_zakupy.data.interfaces.IRepository

class OrderRepository<T: Any>(private val orderDao: OrderDao): IRepository<T>{

    suspend fun getActiveOrders(token: String): ArrayList<T>
            = parseJsonToObject(
            orderDao.getActiveOrders(token))


    suspend fun getAllActiveOrdersOfUser(userId:String, token: String): ArrayList<T>
            = parseJsonToObject(
            orderDao.getAllActiveOrdersOfUser(userId, token))


    suspend fun getAllOrdersOfUser(userId: String, token: String): ArrayList<T>
            = parseJsonToObject(orderDao.getAllOrdersOfUser(userId, token))


    suspend fun placeOrder(orderDto: T, token: String): String
            = orderDao.placeOrder(parseObjectToJson(orderDto), token)


    suspend fun acceptOrder(userId: String, orderId: Long, token: String): String
            = orderDao.acceptOrder(orderId, userId, token)


    suspend fun unAcceptOrder(userId: String, orderId: Long, token: String): String
            = orderDao.unAcceptOrder(orderId, userId, token)


    suspend fun confirmOrder(userId: String, orderId: Long, token: String): String
            = orderDao.confirmOrder(orderId, userId, token)


    suspend fun cancelConfirmOrder(userId: String, orderId: Long, token: String): String
            = orderDao.cancelConfirmOrder(orderId, userId, token)


    suspend fun checkConfirmation(orderId: Long,token: String): Boolean
            = orderDao.checkConfirmation(orderId, token)
                .toBoolean()

    suspend fun deleteOrder(orderId: Long,token: String): String
            = orderDao.deleteOrder(orderId, token)
}