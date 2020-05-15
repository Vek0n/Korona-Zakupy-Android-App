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
import com.maskjs.korona_zakupy.viewmodels.register.RegisterViewModel
import kotlinx.coroutines.*

class RegisterPart2Fragment : Fragment() {

    private var onBackListener: OnReg2BackButtonPressed? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var uiDataBinding: FragmentRegisterPart2Binding
    private lateinit var sharedPreferences: SharedPreferences

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
        initializeUiDataBinding(inflater,container)
        observeUiElements()
        setUiListener()

        return uiDataBinding.root
    }

    private fun initializeUiDataBinding(inflater: LayoutInflater,container: ViewGroup?){
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
        registerViewModel.firstNameEditTextContent.observe(viewLifecycleOwner, Observer {
            if(!registerViewModel.isNotEmpty(it))  uiDataBinding.firstNameTextInputLayout.error = getString(
                R.string.global_empty_field_error
            ) else {
                uiDataBinding.firstNameTextInputLayout.error = null
            }
        })
    }

    private fun observeLastName(){
        registerViewModel.lastNameEditTextContent.observe(viewLifecycleOwner, Observer {
            if(!registerViewModel.isNotEmpty(it))  uiDataBinding.lastNameTextInputLayout.error = getString(
                R.string.global_empty_field_error
            ) else {
                uiDataBinding.lastNameTextInputLayout.error = null
            }
        })
    }

    private fun observeAddress(){
        registerViewModel.addressEditTextContent.observe(viewLifecycleOwner, Observer {
            if(!registerViewModel.isNotEmpty(it))  uiDataBinding.addressTextInputLayout.error = getString(
                R.string.global_empty_field_error
            ) else {
                uiDataBinding.addressTextInputLayout.error = null
            }
        })
    }

    private  fun setUiListener(){
        uiDataBinding.textViewToLogin.setOnClickListener {
            onBackListener?.goToLoginActivity()
        }

        uiDataBinding.floatingActionButton.setOnClickListener {
            if(uiValidate()){
                CoroutineScope(Dispatchers.IO).launch {
                    register()
                }
            }
        }
    }

    private fun uiValidate() : Boolean{
       var check = true

        if(!registerViewModel.isNotEmpty(registerViewModel.firstNameEditTextContent.value.toString())) {
            uiDataBinding.firstNameTextInputLayout.error =
                getString(R.string.global_empty_field_error)
            check = false
        }

        if(!registerViewModel.isNotEmpty(registerViewModel.lastNameEditTextContent.value.toString())) {
            uiDataBinding.lastNameTextInputLayout.error =
                getString(R.string.global_empty_field_error)
            check = false
        }

        if(!registerViewModel.isNotEmpty(registerViewModel.addressEditTextContent.value.toString())) {
            uiDataBinding.addressTextInputLayout.error =
                getString(R.string.global_empty_field_error)
            check = false
        }

        return check

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

    private suspend fun saveResponse(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit(){
            putString(R.string.user_id_key.toString(), registerViewModel.userRegisterResponseDto.userId)
            putString(R.string.user_token_key.toString(),registerViewModel.userRegisterResponseDto.token)
            commit()
        }
    }

    private suspend fun goToUserActivity(){
        val intent = Intent(activity, VolunteerActivity::class.java)
        activity?.startActivity(intent)
    }

    companion object{
        fun newInstance(): RegisterPart2Fragment {
            return RegisterPart2Fragment()
        }
    }
}
