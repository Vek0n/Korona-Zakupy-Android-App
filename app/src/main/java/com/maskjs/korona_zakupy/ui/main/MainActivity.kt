package com.maskjs.korona_zakupy.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.ActivityMainBinding
import com.maskjs.korona_zakupy.ui.base.BaseActivity
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel

class MainActivity : BaseActivity() {
    private val mainActivityViewModel : MainActivityViewModel by lifecycleScope.viewModel(this)

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
