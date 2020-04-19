package com.maskjs.korona_zakupy.data.orders
import com.maskjs.korona_zakupy.data.interfaces.IDataAccessObject
import okhttp3.OkHttpClient

class OrderDao : IDataAccessObject{
    private val client = OkHttpClient()

    fun getActiveOrders(): String {
        return APIGetRequest("http://10.0.2.2:5001/api/orders/active", client)
    }


    fun getAllActiveOrdersOfUser(id:String):String{
        return APIGetRequest("http://10.0.2.2:5001/api/orders/active/$id", client)
    }


    fun getAllOrdersOfUser(id:String):String{
        return "\"\"" + APIGetRequest("http://10.0.2.2:5001/api/orders/all/$id", client) + "\"\""
    }


    fun placeOrder(orderString: String): String{
        return APIPostRequest(orderString,"http://10.0.2.2:5001/api/orders/add",client)
    }


    fun acceptOrder(orderId:Long, userId:String):String{
        return APIPostRequest("","http://10.0.2.2:5001/api/orders/accept/$orderId/$userId", client)
    }


    fun confirmOrder(orderId: Long, userId: String):String{
        return APIPostRequest("","http://10.0.2.2:5001/api/orders/confirm/$orderId/$userId", client)
    }


    fun cancelConfirmOrder(orderId: Long, userId: String):String{
        return APIGetRequest("http://10.0.2.2:5001/api/orders/confirm/cancel/$orderId/$userId", client)
    }


    fun checkConfirmation(orderId: Long):String{
        return APIGetRequest("http://10.0.2.2:5001/api/orders/confirm/check/$orderId", client)
    }


    fun finishOrder(orderId: Long):String{
        return APIGetRequest("http://10.0.2.2:5001/api/orders/finish/$orderId", client)
    }
}