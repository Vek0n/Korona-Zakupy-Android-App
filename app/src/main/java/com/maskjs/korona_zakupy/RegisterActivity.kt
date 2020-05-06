package com.maskjs.korona_zakupy

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.maskjs.korona_zakupy.viewmodels.Register.RegisterViewModel

import kotlinx.android.synthetic.main.activity_register_part1.*

class RegisterActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private val regFragment1 = RegisterPart1Fragment.newInstance()
    private val regFragment2 = RegisterPart2Fragment.newInstance()
    private  var isFirstFragment = true
    val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_part1)
        setSupportActionBar(toolbar)

        goToRegFirstFragment()
    }

    private fun goToRegFirstFragment(){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.reg_fragments, regFragment1)
        fragmentTransaction.commit()

        isFirstFragment = true
    }

    private fun goToRegSecondFragment(){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.reg_fragments, regFragment2)
        fragmentTransaction.commit()
        isFirstFragment = false;
    }


}
