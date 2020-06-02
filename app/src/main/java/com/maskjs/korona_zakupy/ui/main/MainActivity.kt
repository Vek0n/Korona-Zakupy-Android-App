package com.maskjs.korona_zakupy.ui.main

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.ActivityMainBinding
import com.maskjs.korona_zakupy.ui.base.BaseActivity
import com.maskjs.korona_zakupy.ui.person_in_quarantine.PersonInQuarantineActivity
import com.maskjs.korona_zakupy.ui.register.RegisterActivity
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerActivity
import com.maskjs.korona_zakupy.ui.login.LoginActivity


class MainActivity : BaseActivity() {
    private val mainActivityViewModel : MainActivityViewModel by viewModels()
    private lateinit var layoutBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLayoutBinding()

        setUiListeners()

        navigateBetweenActivities()


    }

    private fun setLayoutBinding(){
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun setUiListeners(){
        layoutBinding.mainButtonLogin.setOnClickListener {
            goToLoginActivity()
        }

        layoutBinding.mainButtonRegister.setOnClickListener {
            goToRegisterActivity()
        }
    }

    override fun onRestart() {
        super.onRestart()
        navigateBetweenActivities()
    }

    private fun navigateBetweenActivities(){
        readSavedData()
        chooseActivity()
    }

    private fun readSavedData(){
        val savedData = Triple(getUserId()!!,getUserToken()!!,getUserRole()!!)
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
