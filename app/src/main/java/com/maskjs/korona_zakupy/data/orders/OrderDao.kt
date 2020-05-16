package com.maskjs.korona_zakupy.data.orders
import com.maskjs.korona_zakupy.data.interfaces.IDataAccessObject
import okhttp3.OkHttpClient

class OrderDao(private val client: OkHttpClient) : IDataAccessObject{

    suspend fun getActiveOrders(): String
        = APIGetRequest("http://korona-zakupy.hostingasp.pl/api/orders/active", client)


    suspend fun getAllActiveOrdersOfUser(id:String):String
        = APIGetRequest("http://korona-zakupy.hostingasp.pl/api/orders/active/$id", client)


    suspend fun getAllOrdersOfUser(id:String):String
        = APIGetRequest("http://korona-zakupy.hostingasp.pl/api/orders/all/$id", client)


    suspend fun placeOrder(orderString: String): String
        = APIPostRequest(orderString,"http://korona-zakupy.hostingasp.pl/api/orders/add",client)


    suspend fun acceptOrder(orderId:Long, userId:String):String
        = APIPostRequest("","http://korona-zakupy.hostingasp.pl/api/orders/accept/$orderId/$userId", client)


    suspend fun unAcceptOrder(orderId: Long, userId: String): String
        = APIPostRequest("","http://korona-zakupy.hostingasp.pl/api/orders/accept/cancel/$orderId/$userId", client)


    suspend fun confirmOrder(orderId: Long, userId: String):String
        = APIPostRequest("","http://korona-zakupy.hostingasp.pl/api/orders/confirm/$orderId/$userId", client)


    suspend fun cancelConfirmOrder(orderId: Long, userId: String):String
        = APIPostRequest("","http://korona-zakupy.hostingasp.pl/api/orders/confirm/cancel/$orderId/$userId", client)


    suspend fun checkConfirmation(orderId: Long):String
        = APIGetRequest("http://korona-zakupy.hostingasp.pl/api/orders/confirm/check/$orderId", client)

}