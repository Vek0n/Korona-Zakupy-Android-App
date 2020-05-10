package com.maskjs.korona_zakupy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart2Binding
import com.maskjs.korona_zakupy.viewmodels.register.RegisterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterPart2Fragment : Fragment() {

    private var onBackListener: OnReg2BackButtonPressed? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()

    lateinit var dataBinding: FragmentRegisterPart2Binding
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

    interface OnReg2BackButtonPressed{
        fun  goToReg1Fragment()
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
        var xd = inflater.inflate(R.layout.fragment_register_part2, container, false)

       // dataBinding = FragmentRegisterPart2Binding.inflate(inflater,container,false)
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_register_part2,container,false)
        dataBinding.lifecycleOwner = this@RegisterPart2Fragment
        dataBinding.registerViewModel = registerViewModel

        dataBinding.floatingActionButton.setOnClickListener {
            val job: Job = CoroutineScope(Dispatchers.IO).launch {
                val result = registerViewModel.register()
                var xD = 5
            }

            job.start()

            if (job.isCompleted){
                job.cancel()
            }
        }

        return dataBinding.root
    }

}
