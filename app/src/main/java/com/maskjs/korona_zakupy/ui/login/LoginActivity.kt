package com.maskjs.korona_zakupy.ui.login

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.ActivityLoginBinding
import com.maskjs.korona_zakupy.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.Exception

class LoginActivity : BaseActivity() {
    private val loginViewModel : LoginViewModel by lifecycleScope.viewModel(this){ parametersOf(errorMessages)}

    private lateinit var uiDataBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()

        observeUiElements()

        setUiListener()
    }

    private fun initialize(){

        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        uiDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        uiDataBinding.loginViewModel = loginViewModel
        uiDataBinding.lifecycleOwner = this
    }

    private fun observeUiElements(){
        observeEmail()
        observePassword()
    }

    private fun observeEmail(){
        loginViewModel.emailInputTextLayoutModel.textContent.observe(this, Observer {
            loginViewModel.validateEmail()
        })
    }

    private fun observePassword(){
        loginViewModel.passwordTextInputLayoutModel.textContent.observe(this, Observer {
            loginViewModel.validatePassword()
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
    private fun checkUiValidation() =  loginViewModel.checkValidation()

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

        setUserId( loginViewModel.loginResponseDto.userId)
        setUserToken( loginViewModel.loginResponseDto.token)
        setUserRole(loginViewModel.loginResponseDto.roleName)
    }

    private fun handleException(){
        uiDataBinding.emailTextEditText.error = getString(R.string.log_error_invalid_email_or_password)
        uiDataBinding.passwordEditText.error =getString(R.string.log_error_invalid_email_or_password)
    }
}
