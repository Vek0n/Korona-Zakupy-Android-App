package com.maskjs.korona_zakupy

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart1Binding
import com.maskjs.korona_zakupy.viewmodels.Register.RegisterViewModel

class RegisterPart1Fragment : Fragment() {

    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentRegisterPart1Binding

    companion object{
        fun newInstance(): RegisterPart1Fragment{
            return  RegisterPart1Fragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentRegisterPart1Binding.inflate(inflater)
        return inflater.inflate(R.layout.fragment_register_part1, container, false)
    }

}
