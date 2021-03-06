package com.maskjs.korona_zakupy.ui.register.part1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart1Binding
import com.maskjs.korona_zakupy.ui.base.BaseFragment
import com.maskjs.korona_zakupy.ui.register.IRegisterNavigation
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.scope.getViewModel

class RegisterPart1Fragment : BaseFragment() {
    private var registerNavigation: IRegisterNavigation? = null
    private lateinit var registerViewModel: RegisterPart1ViewModel
    private lateinit var layoutDataBinding: FragmentRegisterPart1Binding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        registerViewModel = requireActivity().lifecycleScope.getViewModel<RegisterPart1ViewModel>(requireActivity())

        registerNavigation = (context as? IRegisterNavigation)

        if(registerNavigation == null)
            throw ClassCastException("Error!")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initialize(inflater,container)

        setUiListener()

        return layoutDataBinding.root
    }

    private fun initialize(inflater: LayoutInflater,container: ViewGroup?){
        layoutDataBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_register_part1,container,false)
        layoutDataBinding.lifecycleOwner = this@RegisterPart1Fragment
        layoutDataBinding.registerViewModel = registerViewModel
    }

    private fun setUiListener(){
        layoutDataBinding.buttonVolunteer.setOnClickListener(){
            registerViewModel.roleName = getString(R.string.global_volunteer_role)
            saveRoleName(registerViewModel.roleName)
            registerNavigation?.goToReg2Fragment()
        }

        layoutDataBinding.buttonPersonInQuarantine.setOnClickListener(){
            registerViewModel.roleName = getString(R.string.global_person_in_quarantine_role)
            saveRoleName(registerViewModel.roleName)
            registerNavigation?.goToReg2Fragment()
        }
    }

    private fun saveRoleName(roleName: String){
        registerViewModel.save()
        setUserRole(roleName)
    }

}


