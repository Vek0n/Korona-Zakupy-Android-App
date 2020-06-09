package com.maskjs.korona_zakupy.ui.register.part2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart2Binding
import com.maskjs.korona_zakupy.ui.base.BaseFragment
import com.maskjs.korona_zakupy.ui.register.IRegisterNavigation
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.getViewModel
import org.koin.core.parameter.parametersOf

class RegisterPart2Fragment : BaseFragment() {
    private var registerNavigation: IRegisterNavigation? = null

    private lateinit var registerViewModel: RegisterPart2ViewModel

    private lateinit var layoutDataBinding: FragmentRegisterPart2Binding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerViewModel = requireActivity().lifecycleScope.getViewModel<RegisterPart2ViewModel>(requireActivity()){ parametersOf(errorMessages)}

        registerNavigation = context as? IRegisterNavigation
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

        return layoutDataBinding.root
    }

    private fun initialize(inflater: LayoutInflater,container: ViewGroup?){
        layoutDataBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_register_part2,container,false)
        layoutDataBinding.lifecycleOwner = this@RegisterPart2Fragment
        layoutDataBinding.registerViewModel = registerViewModel
    }

    private fun observeUiElements(){
        observeUserName()
        observeEmail()
        observePassword()
        observeConfirmPassword()
    }

    private fun observeUserName(){
        registerViewModel.userNameInputTextLayoutModel.textContent.observe(viewLifecycleOwner, Observer {
           registerViewModel.validateUserName()
        })
    }

    private  fun observeEmail(){
        registerViewModel.emailInputTextLayoutModel.textContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validateEmail()
        })
    }

    private  fun observePassword(){
        registerViewModel.passwordInputTextLayoutModel.textContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validatePassword()
        })
    }

    private  fun observeConfirmPassword(){
        registerViewModel.confirmPasswordInputTextLayoutModel.textContent.observe(viewLifecycleOwner, Observer {
            registerViewModel.validateConfirmPassword()
        })
    }

    private  fun setUiListener(){
        layoutDataBinding.textViewToLogin.setOnClickListener {
            registerNavigation?.goToLoginActivityInRegFragment()
        }

        layoutDataBinding.fabReg1.setOnClickListener {
            checkValidation()
        }
    }
    private fun checkValidation(){
        if(registerViewModel.checkValidation()) {
            registerViewModel.save()
            registerNavigation?.goToReg3Fragment()
        }
    }

}
