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
import com.maskjs.korona_zakupy.VolunteerActivity
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart2Binding
import com.maskjs.korona_zakupy.helpers.InputTextType
import com.maskjs.korona_zakupy.helpers.RegistrationPart
import com.maskjs.korona_zakupy.viewmodels.register.RegisterViewModel
import kotlinx.coroutines.*

class RegisterPart2Fragment : Fragment() {

    private var onBackListener: OnReg2BackButtonPressed? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var uiDataBinding: FragmentRegisterPart2Binding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var errorMessages: Map<String,String>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onBackListener = (context as? OnReg2BackButtonPressed)

        if(onBackListener == null)
            throw ClassCastException("Error!")
    }

    interface OnReg2BackButtonPressed{
        fun  goToReg1Fragment()
        fun  goToLoginActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            onBackListener?.goToReg1Fragment()
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
            R.layout.fragment_register_part2,container,false)
        uiDataBinding.lifecycleOwner = this@RegisterPart2Fragment
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
                registerViewModel.validateAdress(errorMessages)
            })
    }

    private  fun setUiListener(){
        uiDataBinding.textViewToLogin.setOnClickListener {
            onBackListener?.goToLoginActivity()
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

    private suspend fun handleRegisterResponse(){
        saveResponse()
        goToUserActivity()
    }

    private  fun saveResponse(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit(){
            putString(R.string.user_id_key.toString(), registerViewModel.userRegisterResponseDto.userId)
            putString(R.string.user_token_key.toString(),registerViewModel.userRegisterResponseDto.token)
            commit()
        }
    }

    private  fun goToUserActivity(){
        val intent = Intent(activity, VolunteerActivity::class.java)
        activity?.startActivity(intent)
    }

    companion object{
        fun newInstance(): RegisterPart2Fragment {
            return RegisterPart2Fragment()
        }
    }
}
