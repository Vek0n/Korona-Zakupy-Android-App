package com.maskjs.korona_zakupy

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.databinding.ActivityLoginBinding
import com.maskjs.korona_zakupy.viewmodels.login.LoginViewModel

import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private val loginViewModel : LoginViewModel by viewModels()
    private lateinit var uiDataBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)

        initializeUiDataBinding()
        observeUiElements()

        uiDataBinding.textViewToRegister.setOnClickListener{
          goToRegisterActivity()
        }

        uiDataBinding.fab.setOnClickListener {
            if(validateUi()){

            }
        }
    }

    private fun goToRegisterActivity(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun initializeUiDataBinding(){
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
            if(!loginViewModel.isNotEmpty(it))  uiDataBinding.emailTextInputLayout.error = getString(
                R.string.global_empty_field_error) else {
                uiDataBinding.emailTextInputLayout.error = null
            }
        })
    }

    private fun observePassword(){
        loginViewModel.passwordEditTextContent.observe(this, Observer {
            if(!loginViewModel.isNotEmpty(it))  uiDataBinding.passwordTextInputLayout.error = getString(
                R.string.global_empty_field_error) else {
                uiDataBinding.passwordTextInputLayout.error = null
            }
        })
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

}
