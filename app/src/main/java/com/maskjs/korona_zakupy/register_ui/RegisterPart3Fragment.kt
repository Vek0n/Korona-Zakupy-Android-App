package com.maskjs.korona_zakupy.register_ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.RegisterNavigation
import com.maskjs.korona_zakupy.VolunteerActivity
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart3Binding
import com.maskjs.korona_zakupy.viewmodels.register.RegisterViewModel
import kotlinx.coroutines.*

class RegisterPart3Fragment : Fragment() {

    private var registerNavigation: RegisterNavigation? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var uiDataBinding: FragmentRegisterPart3Binding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var errorMessages: Map<String,String>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        registerNavigation = (context as? RegisterNavigation)

        if(registerNavigation == null)
            throw ClassCastException("Error!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            registerNavigation?.goToReg2Fragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initialize(inflater,container)
        observeUiElements()
        setUiListener()

        return uiDataBinding.root
    }

    private fun initialize(inflater: LayoutInflater, container: ViewGroup?){
        errorMessages = mapOf(
            Pair("emptyError",getString(R.string.global_empty_field_error))
        )
        uiDataBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_register_part3,container,false)
        uiDataBinding.lifecycleOwner = this@RegisterPart3Fragment
        uiDataBinding.registerViewModel = registerViewModel
    }
    private fun observeUiElements(){
        observeFirstName()
        observeLastName()
        observeAddress()
    }

    private fun observeFirstName(){
        registerViewModel.firstNameInputLayoutViewModel.textContent.observe(viewLifecycleOwner,
            Observer {
                registerViewModel.validateFirstName(errorMessages)
        })
    }

    private fun observeLastName(){
       registerViewModel.lastNameInputLayoutViewModel.textContent.observe(viewLifecycleOwner,
           Observer {
            registerViewModel.validateLastName(errorMessages)
       })
    }

    private fun observeAddress(){
        registerViewModel.addressInputTextLayoutViewModel.textContent.observe(viewLifecycleOwner,
            Observer {
                registerViewModel.validateAddress(errorMessages)
            })
    }

    private  fun setUiListener(){
        uiDataBinding.textViewToLogin.setOnClickListener {
            registerNavigation?.goToLoginActivity()
        }

        uiDataBinding.floatingActionButton.setOnClickListener {
            if(checkValidation()){
                CoroutineScope(Dispatchers.IO).launch {
                    register()
                }
            }
        }
    }

    private fun checkValidation() : Boolean{
        return registerViewModel.checkValidationForPartTwo(errorMessages)
    }

    private suspend fun register(){
        registerViewModel.register()
        withContext(Dispatchers.Main){
            handleRegisterResponse()
        }
    }

    private fun handleRegisterResponse(){
        saveResponse()
        registerNavigation?.goToUserActivity()
    }

    private  fun saveResponse(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit(){
            putString(R.string.user_id_key.toString(), registerViewModel.userRegisterResponseDto.userId)
            putString(R.string.user_token_key.toString(),registerViewModel.userRegisterResponseDto.token)
            commit()
        }
    }
}