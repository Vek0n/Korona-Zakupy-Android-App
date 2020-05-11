package com.maskjs.korona_zakupy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.maskjs.korona_zakupy.data.users.RegisterUserDto
import com.maskjs.korona_zakupy.databinding.FragmentRegisterPart2Binding
import com.maskjs.korona_zakupy.viewmodels.register.RegisterViewModel
import kotlinx.coroutines.*

class RegisterPart2Fragment : Fragment() {

    private var onBackListener: OnReg2BackButtonPressed? = null
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private lateinit var dataBinding: FragmentRegisterPart2Binding
    private lateinit var sharedPreferences: SharedPreferences
    private  var sharedPreferencesValues = Pair<String,String>("","")

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
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_register_part2,container,false)
        dataBinding.lifecycleOwner = this@RegisterPart2Fragment
        dataBinding.registerViewModel = registerViewModel

        dataBinding.floatingActionButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
               register()
            }
        }

        return dataBinding.root
    }
    private suspend fun register(){
        registerViewModel.register()
        withContext(Dispatchers.Main){
            handleRegisterResponse()
        }
    }

    private suspend fun handleRegisterResponse(){
        saveResponse()
        goToUserActivity()
    }

    private suspend fun saveResponse(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit(){
            putString(getString(R.string.user_id_key), registerViewModel.userRegisterResponseDto.userId)
            putString(getString(R.string.user_token_key),registerViewModel.userRegisterResponseDto.token)
            commit()
        }
    }

    private suspend fun goToUserActivity(){
        val intent = Intent(activity, VolunteerActivity::class.java)
        activity?.startActivity(intent)
    }
}
