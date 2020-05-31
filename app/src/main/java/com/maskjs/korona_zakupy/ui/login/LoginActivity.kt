package com.maskjs.korona_zakupy.ui.login

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.ActivityLoginBinding
import com.maskjs.korona_zakupy.ui.base.NonUserBaseActivity

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Exception

class LoginActivity : NonUserBaseActivity() {
    private val loginViewModel : LoginViewModel by viewModels()
    private lateinit var uiDataBinding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var errorMessages: Map<String,String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()

        observeUiElements()

        setUiListener()
    }

    private fun initialize(){
        errorMessages = mapOf(
            Pair("emptyError",getString(R.string.global_empty_field_error)),
            Pair("userNameIsAlreadyTaken",getString(R.string.reg_error_is_already_taken)),
            Pair("errorRegexMessage",getString(R.string.reg_error_password_regex)),
            Pair("notMatchError",getString(R.string.reg_error_password_match))
        )
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        uiDataBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_login
        )
        uiDataBinding.loginViewModel = loginViewModel
        uiDataBinding.lifecycleOwner = this
    }

    private fun observeUiElements(){
        observeEmail()
        observePassword()
    }

    private fun observeEmail(){
        loginViewModel.emailInputTextLayoutModel.textContent.observe(this, Observer {
            loginViewModel.validateEmail(errorMessages)
        })
    }

    private fun observePassword(){
        loginViewModel.passwordInputTextLayoutModel.textContent.observe(this, Observer {
            loginViewModel.validatePassword(errorMessages)
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
            if(checkUiValidation()){
                CoroutineScope(Dispatchers.IO).launch {
                    login()
                }
            }
        }
    }

    private fun checkUiValidation() =  loginViewModel.checkValidation(errorMessages)

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
        goToUserActivity(loginViewModel.loginResponseDto.roleName)
    }

    private fun saveResponse(){
        val editor= sharedPreferences.edit()
        editor.putString(R.string.user_id_key.toString(), loginViewModel.loginResponseDto.userId)
        editor.putString(getString(R.string.user_token_key),loginViewModel.loginResponseDto.token)
        editor.putString(getString(R.string.user_role_key),loginViewModel.loginResponseDto.roleName)
        editor.commit()
    }

    private fun handleException(){
        uiDataBinding.emailTextEditText.error = getString(R.string.log_error_invalid_email_or_password)
        uiDataBinding.passwordEditText.error =getString(R.string.log_error_invalid_email_or_password)
    }
}
