package com.maskjs.korona_zakupy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
import com.maskjs.korona_zakupy.data.orders.PlaceOrderDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val job: Job = CoroutineScope(Dispatchers.IO).launch {
          // Log.d("HTTP_JSON", OrderRepository().parseJsonToOrder())

            val ar = ArrayList<String>(2)
            ar.add("Harnaś")
            ar.add("Perła")
            ar.add("Kustosz")

            val order = PlaceOrderDto(
                userId = "ca5f8496-8441-4e59-b7f5-4b92f19a6aa2",
                products = ar
            )
            val result = OrderRepository(OrderDao())
                .placeOrder(order)
            val y = 1
        }
        job.start()

        if (job.isCompleted){
            job.cancel()
        }
    }
}
