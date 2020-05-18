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
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart2Binding
import com.maskjs.korona_zakupy.viewmodels.register.RegisterViewModel

class RegisterPart2Fragment : Fragment() {

    private var onFabListener: OnReg1FabButtonClickedListener? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var uiDataBinding: FragmentRegisterPart2Binding
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
        initialize(inflater,container)

        observeUiElements()

        setUiListener()

        return uiDataBinding.root
    }

    private fun initialize(inflater: LayoutInflater,container: ViewGroup?){
        errorsText = mapOf(
            Pair("emptyError",getString(R.string.global_empty_field_error)),
            Pair("userNameIsAlreadyTaken",getString(R.string.reg_error_user_name_is_already_taken)),
            Pair("emailNameIsAlreadyTaken",getString(R.string.reg_error_email_is_already_taken)),
            Pair("errorRegexMessage",getString(R.string.reg_error_password_regex)),
            Pair("notMatchError",getString(R.string.reg_error_password_match))
        )
        uiDataBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_register_part2,container,false)
        uiDataBinding.lifecycleOwner = this@RegisterPart2Fragment
        uiDataBinding.registerViewModel = registerViewModel
    }

    private fun observeUiElements(){
        observeUserName()
        observeEmail()
        observePassword()
        observeConfirmPassword()
    }

    private fun observeUserName(){
        registerViewModel.userNameInputTextLayoutViewModel.textContent.observe(viewLifecycleOwner, Observer {
           registerViewModel.validateUserName(errorsText)
        })
    }

    private  fun observeEmail(){
        registerViewModel.emailInputTextLayoutViewModel.textContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validateEmail(errorsText)
        })
    }

    private  fun observePassword(){
        registerViewModel.passwordInputTextLayoutViewModel.textContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validatePassword(errorsText)
        })
    }

    private  fun observeConfirmPassword(){
        registerViewModel.confirmPasswordInputTextLayoutViewModel.textContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validateConfirmPassword(errorsText,registerViewModel.passwordInputTextLayoutViewModel.textContent.value?:"")
        })
    }

    private  fun setUiListener(){
        uiDataBinding.textViewToLogin.setOnClickListener {
            onFabListener?.goToLoginActivity()
        }

        uiDataBinding.fabReg1.setOnClickListener {
            checkValidation()
        }
    }
    private fun checkValidation(){
        if(registerViewModel.checkValidationForPartOne(errorsText))
            onFabListener?.goToReg3Fragment()
    }
    interface OnReg1FabButtonClickedListener{
        fun goToReg3Fragment()
        fun goToLoginActivity()
    }

    companion object{
        fun newInstance(): RegisterPart2Fragment {
            return RegisterPart2Fragment()
        }
    }

}
