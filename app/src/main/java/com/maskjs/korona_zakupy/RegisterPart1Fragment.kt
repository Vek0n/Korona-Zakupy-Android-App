package com.maskjs.korona_zakupy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart1Binding
import com.maskjs.korona_zakupy.viewmodels.register.RegisterViewModel


class RegisterPart1Fragment : Fragment() {

    private var onFabListener: OnReg1FabButtonClickedListener? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    lateinit var dataBinding: FragmentRegisterPart1Binding
    companion object{
        fun newInstance(): RegisterPart1Fragment{
            return  RegisterPart1Fragment()
        }
    }

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
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_register_part1,container,false)
        dataBinding.lifecycleOwner = this@RegisterPart1Fragment
        dataBinding.registerViewModel = registerViewModel

        registerViewModel.confirmPasswordEditTextContent.observe(viewLifecycleOwner, Observer {
            if(!registerViewModel.checkPassword()){
                Toast.makeText(activity,registerViewModel.toastText, Toast.LENGTH_SHORT).show()
            }
        })

        dataBinding.fabReg1.setOnClickListener {
            onFabListener?.goToReg2Fragment()
        }

        return dataBinding.root
    }

    interface OnReg1FabButtonClickedListener{
        fun goToReg2Fragment()
    }
}
