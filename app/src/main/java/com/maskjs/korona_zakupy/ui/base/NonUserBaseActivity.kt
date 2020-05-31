package com.maskjs.korona_zakupy.ui.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maskjs.korona_zakupy.ui.login.LoginActivity
import com.maskjs.korona_zakupy.ui.main.MainActivity
import com.maskjs.korona_zakupy.ui.new_order.NewOrderActivity
import com.maskjs.korona_zakupy.ui.person_in_quarantine.PersonInQuarantineActivity
import com.maskjs.korona_zakupy.ui.register.RegisterActivity
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerActivity

abstract class NonUserBaseActivity : AppCompatActivity() {

    protected fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    protected fun goToPersonInQuarantineActivity(){
        val intent = Intent(this, PersonInQuarantineActivity::class.java)
        startActivity(intent)
    }

    protected fun goToLoginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


    protected fun goToRegisterActivity(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    protected fun goToVolunteerActivity(){
        val intent = Intent(this, VolunteerActivity::class.java)
        startActivity(intent)
    }

    protected fun goToUserActivity(roleName: String){
        if(roleName == "Volunteer")
            goToVolunteerActivity()
        if(roleName == "PersonInQuarantine")
            goToPersonInQuarantineActivity()
    }
}
