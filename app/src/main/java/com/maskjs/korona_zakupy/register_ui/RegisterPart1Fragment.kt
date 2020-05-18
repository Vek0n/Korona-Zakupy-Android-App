package com.maskjs.korona_zakupy.register_ui

import android.content.Context
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

import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart1Binding
import com.maskjs.korona_zakupy.viewmodels.register.RegisterViewModel


class RegisterPart1Fragment : Fragment() {
    private var onClickChooseButtonListener: OnButtonChooseRoleListener? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var uiDataBinding: FragmentRegisterPart1Binding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onClickChooseButtonListener = (context as? OnButtonChooseRoleListener)

        if(onClickChooseButtonListener == null)
            throw ClassCastException("Error!")
    }

    interface OnButtonChooseRoleListener{
        fun  goToReg2Fragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            onClickChooseButtonListener?.goToReg2Fragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initialize(inflater,container)
        setUiListener()

        return uiDataBinding.root
    }

    private fun initialize(inflater: LayoutInflater,container: ViewGroup?){
        uiDataBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_register_part1,container,false)
        uiDataBinding.lifecycleOwner = this@RegisterPart1Fragment
        uiDataBinding.registerViewModel = registerViewModel
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun setUiListener(){
        uiDataBinding.buttonVolunteer.setOnClickListener(){
            registerViewModel.roleName = getString(R.string.global_volunteer_role)
            saveRoleName(registerViewModel.roleName)
            onClickChooseButtonListener?.goToReg2Fragment()
        }

        uiDataBinding.buttonPersonInQuarantine.setOnClickListener(){
            registerViewModel.roleName = getString(R.string.global_person_in_quarantine_role)
            saveRoleName(registerViewModel.roleName)
            onClickChooseButtonListener?.goToReg2Fragment()
        }
    }

    interface

    private fun saveRoleName(roleName: String){
        sharedPreferences.edit(){
            putString(R.string.user_role_key.toString(), roleName)
            commit()
        }
    }

}


