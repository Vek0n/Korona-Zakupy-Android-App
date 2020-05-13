package com.maskjs.korona_zakupy

import android.content.Context
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

        val Button2 = findViewById<Button>(R.id.button3)
        Button2.setOnClickListener{
            val intent = Intent(this, VolunteerActivity::class.java)
            startActivity(intent)
        }

        val button3 = findViewById<Button>(R.id.login_button);
        button3.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        val button4 = findViewById<Button>(R.id.quarantineButton)
        button4.setOnClickListener {
            val intent = Intent(this, PersonInQuarantineActivity::class.java)
            startActivity(intent)
        }

    }
}
