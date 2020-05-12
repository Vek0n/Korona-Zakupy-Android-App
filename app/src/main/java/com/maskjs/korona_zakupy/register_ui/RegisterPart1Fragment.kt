package com.maskjs.korona_zakupy.register_ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart1Binding
import com.maskjs.korona_zakupy.viewmodels.register.RegisterViewModel


class RegisterPart1Fragment : Fragment() {

    private var onFabListener: OnReg1FabButtonClickedListener? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var uiDataBinding: FragmentRegisterPart1Binding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onFabListener = context as? OnReg1FabButtonClickedListener

        if(onFabListener == null)
            throw ClassCastException("Error!")
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
            R.layout.fragment_register_part1,container,false)
        uiDataBinding.lifecycleOwner = this@RegisterPart1Fragment
        uiDataBinding.registerViewModel = registerViewModel
    }

    private fun observeUiElements(){
        observeUserName()
        observeEmail()
        observePassword()
        observeConfirmPassword()
    }

    private fun observeUserName(){
        registerViewModel.userNameEditTextContent.observe(viewLifecycleOwner, Observer {
            if(!registerViewModel.isNotEmpty(it))  uiDataBinding.userNameTextInputLayout.error = getString(
                R.string.global_empty_field_error
            ) else {
                uiDataBinding.userNameTextInputLayout.error = null
            }
        })
    }

    private  fun observeEmail(){
        registerViewModel.emailEditTextContent.observe(viewLifecycleOwner, Observer {
            if(!registerViewModel.isNotEmpty(it))  uiDataBinding.emailTextInputLayout.error = getString(
                R.string.global_empty_field_error
            ) else {
                uiDataBinding.emailTextInputLayout.error = null
            }
        })
    }

    private  fun observePassword(){
        registerViewModel.passwordEditTextContent.observe(viewLifecycleOwner, Observer {
            if(!registerViewModel.isNotEmpty(it))  uiDataBinding.passwordInputTextLayout.error = getString(
                R.string.global_empty_field_error
            ) else {
                uiDataBinding.passwordInputTextLayout.error = null
            }

            if(!registerViewModel.checkPassword()) uiDataBinding.passwordInputTextLayout.error = getString(
                R.string.reg_error_password_regex
            ) else {
                uiDataBinding.passwordInputTextLayout.error = null
            }
        })
    }

    private  fun observeConfirmPassword(){
        registerViewModel.confirmPasswordEditTextContent.observe(viewLifecycleOwner, Observer {
            if(!registerViewModel.isNotEmpty(it))  uiDataBinding.confirmPasswordTextInputLayout.error = getString(
                R.string.global_empty_field_error
            ) else {
                uiDataBinding.confirmPasswordTextInputLayout.error = null
            }

            if (!registerViewModel.checkConfirmPassword()) uiDataBinding.confirmPasswordTextInputLayout.error = getString(
                R.string.reg_error_password_match
            ) else {
                uiDataBinding.confirmPasswordTextInputLayout.error = null
            }
        })
    }

    private  fun setUiListener(){
        uiDataBinding.textViewToLogin.setOnClickListener {
            onFabListener?.goToLoginActivity()
        }

        uiDataBinding.fabReg1.setOnClickListener {
            if(uiValidate())
                onFabListener?.goToReg2Fragment()
        }
    }

    private fun uiValidate(): Boolean{
        var check = true

        if(!registerViewModel.checkPassword()) uiDataBinding.passwordInputTextLayout.error = getString(
            R.string.reg_error_password_regex
        ) else {
            uiDataBinding.passwordInputTextLayout.error = null
        }

        if (!registerViewModel.checkConfirmPassword()) uiDataBinding.confirmPasswordTextInputLayout.error = getString(
            R.string.reg_error_password_match
        ) else {
            uiDataBinding.confirmPasswordTextInputLayout.error = null
        }

        if(!registerViewModel.isNotEmpty(registerViewModel.userNameEditTextContent.value.toString())) {
            uiDataBinding.userNameTextInputLayout.error =
                getString(R.string.global_empty_field_error)
                check = false
        }

        if(!registerViewModel.isNotEmpty(registerViewModel.emailEditTextContent.value.toString())) {
            uiDataBinding.emailTextInputLayout.error =
                getString(R.string.global_empty_field_error)
                check = false
        }


        if(!registerViewModel.isNotEmpty(registerViewModel.passwordEditTextContent.value.toString())) {
            uiDataBinding.passwordInputTextLayout.error =
                getString(R.string.global_empty_field_error)
            check = false
        }

        if(!registerViewModel.isNotEmpty(registerViewModel.confirmPasswordEditTextContent.value.toString())) {
            uiDataBinding.confirmPasswordTextInputLayout.error =
                getString(R.string.global_empty_field_error)
                check = false
        }

        return check
    }

    interface OnReg1FabButtonClickedListener{
        fun goToReg2Fragment()
        fun goToLoginActivity()
    }

    companion object{
        fun newInstance(): RegisterPart1Fragment {
            return RegisterPart1Fragment()
        }
    }

}
