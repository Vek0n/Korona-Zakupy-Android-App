package com.maskjs.korona_zakupy.ui.register.part2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart2Binding
import com.maskjs.korona_zakupy.ui.register.RegisterNavigation

class RegisterPart2Fragment : Fragment() {

    private var registerNavigation: RegisterNavigation? = null
    private val registerViewModel: RegisterPartTwoViewModel by viewModels()
    private lateinit var uiDataBinding: FragmentRegisterPart2Binding
    private lateinit var errorsText: Map<String,String>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        registerNavigation = context as? RegisterNavigation

        if(registerNavigation == null)
            throw ClassCastException("Error!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            registerNavigation?.goToReg1Fragment()
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

    private fun initialize(inflater: LayoutInflater,container: ViewGroup?){
        errorsText = mapOf(
            Pair("emptyError",getString(R.string.global_empty_field_error)),
            Pair("isAlreadyTaken",getString(R.string.reg_error_is_already_taken)),
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
        registerViewModel.userNameInputTextLayoutModel.textContent.observe(viewLifecycleOwner, Observer {
           registerViewModel.validateUserName(errorsText)
        })
    }

    private  fun observeEmail(){
        registerViewModel.emailInputTextLayoutModel.textContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validateEmail(errorsText)
        })
    }

    private  fun observePassword(){
        registerViewModel.passwordInputTextLayoutModel.textContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validatePassword(errorsText)
        })
    }

    private  fun observeConfirmPassword(){
        registerViewModel.confirmPasswordInputTextLayoutModel.textContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validateConfirmPassword(errorsText)
        })
    }

    private  fun setUiListener(){
        uiDataBinding.textViewToLogin.setOnClickListener {
            registerNavigation?.goToLoginActivity()
        }

        uiDataBinding.fabReg1.setOnClickListener {
            checkValidation()
        }
    }
    private fun checkValidation(){
        if(registerViewModel.checkValidation(errorsText)) {
            registerViewModel.save()
            registerNavigation?.goToReg3Fragment()
        }
    }

}
