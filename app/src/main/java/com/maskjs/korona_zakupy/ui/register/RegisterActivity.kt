package com.maskjs.korona_zakupy.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerActivity
import com.maskjs.korona_zakupy.ui.login.LoginActivity
import com.maskjs.korona_zakupy.ui.person_in_quarantine.PersonInQuarantineActivity
import com.maskjs.korona_zakupy.ui.register.part1.RegisterPart1Fragment
import com.maskjs.korona_zakupy.ui.register.part1.RegisterPartOneViewModel
import com.maskjs.korona_zakupy.ui.register.part2.RegisterPart2Fragment
import com.maskjs.korona_zakupy.ui.register.part3.RegisterPart3Fragment

import kotlinx.android.synthetic.main.activity_register_part.*

class RegisterActivity : AppCompatActivity(),
    RegisterNavigation {

    private val fragmentManager = supportFragmentManager
    private val regFragment1 =
        RegisterPart1Fragment()
    private val regFragment2 =
        RegisterPart2Fragment()
    private val regFragment3 =
        RegisterPart3Fragment()

    val registerViewModel: RegisterPartOneViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_part)
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

     override fun goToReg3Fragment(){
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.reg_fragments, regFragment3)
        fragmentTransaction.commit()
    }

    override fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun goToUserActivity() {
        if(registerViewModel.roleName == getString(R.string.global_volunteer_role)) {
            val intent = Intent(this, VolunteerActivity::class.java)
            this.startActivity(intent)
        }
        if(registerViewModel.roleName == getString(R.string.global_person_in_quarantine_role)){
            val intent = Intent(this, PersonInQuarantineActivity::class.java)
            this.startActivity(intent)
        }
    }
}

interface RegisterNavigation{
    fun goToReg1Fragment()
    fun goToReg2Fragment()
    fun goToReg3Fragment()
    fun goToLoginActivity()
    fun goToUserActivity()
}
