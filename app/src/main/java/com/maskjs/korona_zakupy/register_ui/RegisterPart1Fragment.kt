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
import com.maskjs.korona_zakupy.helpers.RegistrationPart
import com.maskjs.korona_zakupy.viewmodels.register.RegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
            validateUserName()
        })
    }

    private fun validateUserName(){
        CoroutineScope(Dispatchers.IO).launch {
            registerViewModel.checkIsUserNameAlreadyTaken()
            withContext(Dispatchers.Main) {
                registerViewModel.validate(InputTextType.USER_NAME, errorsText)
            }
        }
    }

    private  fun observeEmail(){
        registerViewModel.emailEditTextContent.observe(viewLifecycleOwner, Observer {
          validateEmail()
        })
    }

    private fun validateEmail(){
        CoroutineScope(Dispatchers.IO).launch {
            registerViewModel.checkIsEmailAlreadyTaken()
            withContext(Dispatchers.Main) {
                registerViewModel.validate(InputTextType.EMAIL, errorsText)
            }
        }
    }

    private  fun observePassword(){
        registerViewModel.passwordEditTextContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validate(InputTextType.PASSWORD_REGISTER,errorsText)
        })
    }

    private  fun observeConfirmPassword(){
        registerViewModel.confirmPasswordEditTextContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validate(InputTextType.CONFIRM_PASSWORD_REGISTER,errorsText)
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
        if(registerViewModel.checkValidation(RegistrationPart.PART_2,errorsText))
            onFabListener?.goToReg2Fragment()
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
