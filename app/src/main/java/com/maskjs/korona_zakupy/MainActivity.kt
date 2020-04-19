package com.maskjs.korona_zakupy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maskjs.korona_zakupy.data.orders.OrderDao
import com.maskjs.korona_zakupy.data.orders.OrderRepository
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
            val result = OrderRepository().parseJsonToOrder(
                OrderDao().getActiveOrders())
            val x = result[0].isActive
        }
        job.start()

        if (job.isCompleted){
            job.cancel()
        }
    }
}
