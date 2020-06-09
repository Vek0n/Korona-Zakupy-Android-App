package com.maskjs.korona_zakupy.ui.register.part3

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart3Binding
import com.maskjs.korona_zakupy.ui.base.BaseFragment
import com.maskjs.korona_zakupy.ui.register.IRegisterNavigation
import kotlinx.coroutines.*
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.getViewModel
import org.koin.core.parameter.parametersOf

class RegisterPart3Fragment : BaseFragment() {
    private var registerNavigation: IRegisterNavigation? = null

    private lateinit var registerViewModel: RegisterPart3ViewModel

    private lateinit var layoutDataBinding: FragmentRegisterPart3Binding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerViewModel = requireActivity().lifecycleScope.getViewModel<RegisterPart3ViewModel>(requireActivity()){ parametersOf(errorMessages)}
        registerNavigation = (context as? IRegisterNavigation)

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

        return layoutDataBinding.root
    }

    private fun initialize(inflater: LayoutInflater, container: ViewGroup?){
        layoutDataBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_register_part3,container,false)
        layoutDataBinding.lifecycleOwner = this@RegisterPart3Fragment
        layoutDataBinding.registerViewModel = registerViewModel
    }
    private fun observeUiElements(){
        observeFirstName()
        observeLastName()
        observeAddress()
    }

    private fun observeFirstName(){
        registerViewModel.firstNameInputLayoutModel.textContent.observe(viewLifecycleOwner,
            Observer {
                registerViewModel.validateFirstName()
        })
    }

    private fun observeLastName(){
       registerViewModel.lastNameInputLayoutModel.textContent.observe(viewLifecycleOwner,
           Observer {
            registerViewModel.validateLastName()
       })
    }

    private fun observeAddress(){
        registerViewModel.addressInputTextLayoutModel.textContent.observe(viewLifecycleOwner,
            Observer {
                registerViewModel.validateAddress()
            })
    }

    private  fun setUiListener(){
        setOnToLoginActivityListener()
        setOnFabListener()
    }

    private fun setOnToLoginActivityListener(){
        layoutDataBinding.textViewToLogin.setOnClickListener {
            registerNavigation?.goToLoginActivityInRegFragment()
        }
    }
    private fun setOnFabListener(){
        layoutDataBinding.floatingActionButton.setOnClickListener {
            if(registerViewModel.checkValidation()){
                registerViewModel.save()

                CoroutineScope(Dispatchers.IO).launch {
                    register()
                }
            }
        }
    }

    private suspend fun register(){
        registerViewModel.register()
        withContext(Dispatchers.Main){
            handleRegisterResponse()
        }
    }

    private fun handleRegisterResponse(){
        saveResponse()
        registerNavigation?.goToUserActivityInRegFragment()
    }

    private  fun saveResponse(){
        setUserId(registerViewModel.userRegisterResponseDto.userId)
        setUserToken(registerViewModel.userRegisterResponseDto.token)
    }
}
