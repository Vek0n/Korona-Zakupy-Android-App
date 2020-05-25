package com.maskjs.korona_zakupy.ui.register.part1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels

import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart1Binding
import com.maskjs.korona_zakupy.ui.register.RegisterNavigation


class RegisterPart1Fragment : Fragment() {
    private var registerNavigation: RegisterNavigation? = null
    private val registerViewModel: RegisterPartOneViewModel by activityViewModels()
    private lateinit var uiDataBinding: FragmentRegisterPart1Binding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)

        registerNavigation = (context as? RegisterNavigation)

        if(registerNavigation == null)
            throw ClassCastException("Error!")
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
            registerNavigation?.goToReg2Fragment()
        }

        uiDataBinding.buttonPersonInQuarantine.setOnClickListener(){
            registerViewModel.roleName = getString(R.string.global_person_in_quarantine_role)
            saveRoleName(registerViewModel.roleName)
            registerNavigation?.goToReg2Fragment()
        }
    }

    private fun saveRoleName(roleName: String){
        registerViewModel.save()
        sharedPreferences.edit(){
            putString(R.string.user_role_key.toString(), roleName)
            commit()
        }
    }

}


