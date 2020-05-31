package com.maskjs.korona_zakupy.ui.main

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.ui.base.NonUserBaseActivity
import com.maskjs.korona_zakupy.ui.person_in_quarantine.PersonInQuarantineActivity
import com.maskjs.korona_zakupy.ui.register.RegisterActivity
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerActivity
import com.maskjs.korona_zakupy.ui.login.LoginActivity


class MainActivity : NonUserBaseActivity() {
    private val mainActivityViewModel : MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateBetweenActivities()

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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val button4 = findViewById<Button>(R.id.quarantineButton)
        button4.setOnClickListener {
            val intent = Intent(this, PersonInQuarantineActivity::class.java)
            startActivity(intent)
        }

    }

    private fun navigateBetweenActivities(){
        readSavedData()
        chooseActivity()
    }

    private fun readSavedData(){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val savedData = Triple(sharedPreferences.getString(R.string.user_id_key.toString(),""),
            sharedPreferences.getString(R.string.user_token_key.toString(),""),sharedPreferences.getString(R.string.user_role_key.toString(),""))
        mainActivityViewModel.readSavedData(savedData)
    }

    private fun chooseActivity(){
        when(mainActivityViewModel.chooseActivity()){
            MainActivityViewModel.Activities.VOLUNTEER -> goToVolunteerActivity()
            MainActivityViewModel.Activities.PERSON_IN_QUARANTINE -> goToPersonInQuarantineActivity()
            else -> return
        }
    }
}
