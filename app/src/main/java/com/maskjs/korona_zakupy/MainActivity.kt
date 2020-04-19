package com.maskjs.korona_zakupy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.maskjs.korona_zakupy.data.OrderDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val job: Job = CoroutineScope(Dispatchers.IO).launch {
           Log.d("HTTP_JSON", OrderDao().acceptOrder(4,"7bc899f2-c91d-482b-9d4e-5faafb233f97"))
        }
        job.start()

        if (job.isCompleted){
            job.cancel()
        }
    }
}
