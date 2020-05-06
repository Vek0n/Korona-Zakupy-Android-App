package com.maskjs.korona_zakupy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart2Binding
import com.maskjs.korona_zakupy.viewmodels.Register.RegisterViewModel

class RegisterPart2Fragment : Fragment() {

    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var viewBinding: FragmentRegisterPart2Binding

    companion object{
        fun newInstance(): RegisterPart2Fragment{
            return  RegisterPart2Fragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentRegisterPart2Binding.inflate(inflater)
        return inflater.inflate(R.layout.fragment_register_part2, container, false)
    }

}
