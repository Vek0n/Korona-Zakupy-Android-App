package com.maskjs.korona_zakupy.data.orders
import com.maskjs.korona_zakupy.data.interfaces.IDataAccessObject
import okhttp3.OkHttpClient

class OrderDao(private val client: OkHttpClient) : IDataAccessObject{

    suspend fun getActiveOrders(token: String): String
        = APIGetRequestAuth("http://korona-zakupy.hostingasp.pl/api/orders/active",token ,client)


    suspend fun getAllActiveOrdersOfUser(id:String, token: String):String
        = APIGetRequestAuth("http://korona-zakupy.hostingasp.pl/api/orders/active/$id",token, client)


    suspend fun getAllOrdersOfUser(id:String, token: String):String
        = APIGetRequestAuth("http://korona-zakupy.hostingasp.pl/api/orders/all/$id",token,client)


    suspend fun placeOrder(orderString: String,token: String): String
        = APIPostRequestAuth(orderString,"http://korona-zakupy.hostingasp.pl/api/orders/add",token,client)

    suspend fun acceptOrder(orderId:Long, userId:String,token: String):String
        = APIPostRequestAuth("","http://korona-zakupy.hostingasp.pl/api/orders/accept/$orderId/$userId",token, client)


    suspend fun unAcceptOrder(orderId: Long, userId: String, token: String): String
        = APIPostRequestAuth("","http://korona-zakupy.hostingasp.pl/api/orders/accept/cancel/$orderId/$userId",token, client)


    suspend fun confirmOrder(orderId: Long, userId: String, token: String):String
        = APIPostRequestAuth("","http://korona-zakupy.hostingasp.pl/api/orders/confirm/$orderId/$userId",token, client)


    suspend fun cancelConfirmOrder(orderId: Long, userId: String, token: String):String
        = APIPostRequestAuth("","http://korona-zakupy.hostingasp.pl/api/orders/confirm/cancel/$orderId/$userId",token, client)


    suspend fun checkConfirmation(orderId: Long, token: String):String
        = APIGetRequestAuth("http://korona-zakupy.hostingasp.pl/api/orders/confirm/check/$orderId",token, client)

    suspend fun deleteOrder(orderId: Long, token: String): String
            = APIDeleteRequestAuth("http://korona-zakupy.hostingasp.pl/api/orders/$orderId",token,client)

}