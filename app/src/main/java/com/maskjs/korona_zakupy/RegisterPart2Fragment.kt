package com.maskjs.korona_zakupy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart2Binding
import com.maskjs.korona_zakupy.viewmodels.Register.RegisterViewModel

class RegisterPart2Fragment : Fragment() {

    private var onBackListener: OnReg2BackButtonPressed? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentRegisterPart2Binding

    companion object{
        fun newInstance(): RegisterPart2Fragment{
            return  RegisterPart2Fragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackListener = (context as? OnReg2BackButtonPressed)

        if(onBackListener == null)
            throw ClassCastException("Error!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            onBackListener?.goToReg1Fragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentRegisterPart2Binding.inflate(inflater)
        return inflater.inflate(R.layout.fragment_register_part2, container, false)
    }

    interface OnReg2BackButtonPressed{
        fun  goToReg1Fragment()
    }
}
