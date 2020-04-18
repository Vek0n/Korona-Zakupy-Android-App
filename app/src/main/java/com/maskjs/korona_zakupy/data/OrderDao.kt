package com.maskjs.korona_zakupy.data
import com.maskjs.korona_zakupy.data.interfaces.IDataAccessObject
import okhttp3.OkHttpClient

class OrderDao : IDataAccessObject{
    private val client = OkHttpClient()

    fun getActiveOrders(): String {
        return APIGetRequest("http://10.0.2.2:5001/api/orders/active", client)
    }


    fun getUsersActiveOrders(id:String):String{
        return APIGetRequest("http://10.0.2.2:5001/api/orders/active/$id", client)
    }


    fun getAllUsersOrders(id:String):String{
        return APIGetRequest("http://10.0.2.2:5001/api/orders/all/$id", client)
    }


    fun placeOrder(orderString: String): String{
        return APIPostRequest(orderString,"http://10.0.2.2:5001/api/orders/add",client)
    }


    fun acceptOrder(orderId:Long, userId:String):String{
        return APIPostRequest("","http://10.0.2.2:5001/api/orders/accept/$orderId/$userId", client)
    }
}