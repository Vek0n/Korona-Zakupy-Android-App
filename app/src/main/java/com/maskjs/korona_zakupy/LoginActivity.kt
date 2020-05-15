package com.maskjs.korona_zakupy

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.databinding.ActivityLoginBinding
import com.maskjs.korona_zakupy.viewmodels.login.LoginViewModel

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Exception

class LoginActivity : AppCompatActivity() {
    private val loginViewModel : LoginViewModel by viewModels()
    private lateinit var uiDataBinding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()

        observeUiElements()

        setUiListener()
    }

    private fun initialize(){
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        uiDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        uiDataBinding.loginViewModel = loginViewModel
        uiDataBinding.lifecycleOwner = this
    }

    private fun observeUiElements(){
        observeEmail()
        observePassword()
    }

    private fun observeEmail(){
        loginViewModel.emailEditTextContent.observe(this, Observer {
            uiDataBinding.emailTextEditText.error = null

            if(!loginViewModel.isNotEmpty(it))  uiDataBinding.emailTextInputLayout.error = getString(
                R.string.global_empty_field_error) else {
                uiDataBinding.emailTextInputLayout.error = null
            }
        })
    }

    private fun observePassword(){
        loginViewModel.passwordEditTextContent.observe(this, Observer {
            uiDataBinding.passwordEditText.error = null

            if(!loginViewModel.isNotEmpty(it))  uiDataBinding.passwordTextInputLayout.error = getString(
                R.string.global_empty_field_error) else {
                uiDataBinding.passwordTextInputLayout.error = null
            }
        })
    }

    private fun setUiListener(){
        setTextViewToRegisterListener()
        setFabListener()
    }

    private fun setTextViewToRegisterListener(){
        uiDataBinding.textViewToRegister.setOnClickListener{
            goToRegisterActivity()
        }
    }

    private  fun setFabListener(){
        uiDataBinding.fab.setOnClickListener {
            if(validateUi()){
                CoroutineScope(Dispatchers.IO).launch {
                    login()
                }
            }
        }
    }

    private fun goToRegisterActivity(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun validateUi() : Boolean {
        var check = true

        if (!loginViewModel.isNotEmpty(loginViewModel.emailEditTextContent.value.toString())) {
            uiDataBinding.emailTextInputLayout.error = getString(R.string.global_empty_field_error)
            check = false
        }

        if(!loginViewModel.isNotEmpty(loginViewModel.passwordEditTextContent.value.toString())){
            uiDataBinding.passwordTextInputLayout.error = getString(R.string.global_empty_field_error)
            check = false
        }

        return check
    }

    private suspend fun login(){
        try {
            loginViewModel.login()
            withContext(Dispatchers.Main){
                handleLoginResponse()
            }
        }catch (ex : Exception){
            withContext(Dispatchers.Main) {
                handleException()
            }
        }
    }

    private fun handleLoginResponse(){
        saveResponse()
        goToUserActivity()
    }

    private fun saveResponse(){
        val editor= sharedPreferences.edit()
        editor.putString(R.string.user_id_key.toString(), loginViewModel.loginResponseDto.userId)
        editor.putString(getString(R.string.user_token_key),loginViewModel.loginResponseDto.token)
        editor.commit()
    }

    private fun goToUserActivity(){
        val intent = Intent(this, VolunteerActivity::class.java)
        this?.startActivity(intent)
    }

    private fun handleException(){
        uiDataBinding.emailTextEditText.error = R.string.log_error_invalid_email_or_password.toString()
        uiDataBinding.passwordEditText.error = R.string.log_error_invalid_email_or_password.toString()
    }
}
