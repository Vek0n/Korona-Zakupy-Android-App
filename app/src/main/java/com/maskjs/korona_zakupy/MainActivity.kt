package com.maskjs.korona_zakupy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.data.users.UserDao
import com.maskjs.korona_zakupy.data.users.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val Button = findViewById<Button>(R.id.button)
        Button.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val job: Job = CoroutineScope(Dispatchers.IO).launch {

            val register = RegisterUserDto(
                username = "Grzmot3456",
                email = "szymonkaczmarek2222@gmail.com",
                password = "hkt8zd6vG?!",
                confirmPassword = "hkt8zd6vG?!",
                address = "Lol",
                firstName = "Szymon",
                lastName = "Kaczmarek",
                roleName = "Volunteer"
            )
//          val result = OrderRepository<PlaceOrderDto>( OrderDao( OkHttpClient() ) ).placeOrder(order)
            val result = UserRepository<RegisterUserDto>(UserDao(OkHttpClient())).registerUser(register)
//          val result = UserRepository<Any>(UserDao(OkHttpClient())).getRole("f903862a-6da7-4971-9f64-e72dd84fc973","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJxcXFxcUBnbWFpbC5jb20iLCJqdGkiOiJlN2VhODBhNC0wMjc5LTQ2NGItYTE4NS0xZDBkMmU0NGE2NmEiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjY2Y2Y5N2E1LWQzNDAtNDQ3OC1hMzI2LWQ5ZWM5ZGQ5ZWVjNyIsImV4cCI6MTU4OTgyOTY4MywiaXNzIjoiaHR0cDovL3lvdXJkb21haW4uY29tIiwiYXVkIjoiaHR0cDovL3lvdXJkb21haW4uY29tIn0.x1v2GNdv80zoq7sM2vC98bGl_s5lRCD5jgijRrKDjsY")
        }
        job.start()

        if (job.isCompleted){
            job.cancel()
        }

    }
}
