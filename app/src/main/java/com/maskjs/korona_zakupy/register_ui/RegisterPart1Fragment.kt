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
import com.maskjs.korona_zakupy.helpers.InputTextType
import com.maskjs.korona_zakupy.viewmodels.register.RegisterViewModel
import java.util.*


class RegisterPart1Fragment : Fragment() {

    private var onFabListener: OnReg1FabButtonClickedListener? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var uiDataBinding: FragmentRegisterPart1Binding
    private lateinit var errorsText: Map<String,String>
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

        initializeErrorsText()
        initializeUiDataBinding(inflater,container)
        observeUiElements()
        setUiListener()

        return uiDataBinding.root
    }

    private fun initializeErrorsText(){
        errorsText = mapOf(
            Pair("emptyError",getString(R.string.global_empty_field_error)),
            Pair("errorRegexMessage",getString(R.string.reg_error_password_regex)),
            Pair("notMatchError",getString(R.string.reg_error_password_regex))
        )
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
           registerViewModel.validate(it,InputTextType.OTHERS,errorsText)
        })
    }

    private  fun observeEmail(){
        registerViewModel.emailEditTextContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validate(it,InputTextType.OTHERS,errorsText)
        })
    }

    private  fun observePassword(){
        registerViewModel.passwordEditTextContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validate(it,InputTextType.PASSWORD_REGISTER,errorsText)
        })
    }

    private  fun observeConfirmPassword(){
        registerViewModel.confirmPasswordEditTextContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validate(it,InputTextType.CONFIRM_PASSWORD_REGISTER,errorsText)
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
