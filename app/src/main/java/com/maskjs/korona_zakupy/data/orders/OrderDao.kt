package com.maskjs.korona_zakupy.data.orders
import com.maskjs.korona_zakupy.data.interfaces.IDataAccessObject
import okhttp3.OkHttpClient

class OrderDao(private val client: OkHttpClient) : IDataAccessObject{

    suspend fun getActiveOrders(): String {
        return APIGetRequest("http://10.0.2.2:5001/api/orders/active", client)
    }

    suspend fun getAllActiveOrdersOfUser(id:String):String{
        return APIGetRequest("http://10.0.2.2:5001/api/orders/active/$id", client)
    }

    suspend fun getAllOrdersOfUser(id:String):String{
        //return "\"\"" + APIGetRequest("http://korona-zakupy.hostingasp.pl/api/orders/all/$id", client) + "\"\""
        return APIGetRequest("http://korona-zakupy.hostingasp.pl/api/orders/all/$id", client)
    }

   suspend fun placeOrder(orderString: String): String{
        return APIPostRequest(orderString,"http://10.0.2.2:5001/api/orders/add",client)
    }

   suspend fun acceptOrder(orderId:Long, userId:String):String{
        return APIPostRequest("","http://10.0.2.2:5001/api/orders/accept/$orderId/$userId", client)
    }

   suspend fun confirmOrder(orderId: Long, userId: String):String{
        return APIPostRequest("","http://10.0.2.2:5001/api/orders/confirm/$orderId/$userId", client)
    }

    suspend fun cancelConfirmOrder(orderId: Long, userId: String):String{
        return APIGetRequest("http://10.0.2.2:5001/api/orders/confirm/cancel/$orderId/$userId", client)
    }

    suspend fun checkConfirmation(orderId: Long):String{
        return APIGetRequest("http://10.0.2.2:5001/api/orders/confirm/check/$orderId", client)
    }

    suspend fun finishOrder(orderId: Long):String{
        return APIGetRequest("http://10.0.2.2:5001/api/orders/finish/$orderId", client)
    }
}