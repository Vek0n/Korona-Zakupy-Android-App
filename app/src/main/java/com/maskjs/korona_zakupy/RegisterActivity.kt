package com.maskjs.korona_zakupy

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.maskjs.korona_zakupy.viewmodels.Register.RegisterViewModel

import kotlinx.android.synthetic.main.activity_register_part1.*

class RegisterActivity : AppCompatActivity(), RegisterPart1Fragment.OnReg1FabButtonClickedListener,
RegisterPart2Fragment.OnReg2BackButtonPressed{

    private val fragmentManager = supportFragmentManager
    private val regFragment1 = RegisterPart1Fragment.newInstance()
    private val regFragment2 = RegisterPart2Fragment.newInstance()

    val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_part1)
        setSupportActionBar(toolbar)

        goToReg1Fragment()
    }

     override fun goToReg1Fragment(){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.reg_fragments, regFragment1)
        fragmentTransaction.commit()


    }

     override fun goToReg2Fragment(){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.reg_fragments, regFragment2)
        fragmentTransaction.commit()
    }
}
