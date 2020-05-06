package com.maskjs.korona_zakupy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart1Binding
import com.maskjs.korona_zakupy.viewmodels.Register.RegisterViewModel


class RegisterPart1Fragment : Fragment() {

    private var onFabListener: OnReg1FabButtonClickedListener? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentRegisterPart1Binding

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
        viewBinding = FragmentRegisterPart1Binding.inflate(inflater)
        val view = inflater.inflate(R.layout.fragment_register_part1, container, false)

        view.let {
            val fab = it.findViewById<FloatingActionButton>(R.id.fab_reg1)
            fab.setOnClickListener(View.OnClickListener {
                onFabListener?.goToReg2Fragment()
            })

        }

        return view
    }

    private fun onFabClickListener(){

    }

    private fun saveUserRegisterInfo(){
    }

    interface OnReg1FabButtonClickedListener{
        fun goToReg2Fragment()
    }


}
