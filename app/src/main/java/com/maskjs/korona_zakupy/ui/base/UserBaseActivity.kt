package com.maskjs.korona_zakupy.ui.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.ui.login.LoginActivity
import com.maskjs.korona_zakupy.ui.main.MainActivity
import com.maskjs.korona_zakupy.ui.new_order.NewOrderActivity
import com.maskjs.korona_zakupy.ui.person_in_quarantine.PersonInQuarantineActivity
import com.maskjs.korona_zakupy.ui.register.RegisterActivity
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerActivity

abstract class UserBaseActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.global_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.global_menu_logout -> logOut()
            else -> return true
        }
    }

    private fun logOut() : Boolean{
        cleanSavedData()
        goToMainActivity()
        return true
    }

     private fun cleanSavedData(){
         val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
         val editor= sharedPreferences.edit()
         editor.putString(R.string.user_id_key.toString(), null)
         editor.putString(getString(R.string.user_token_key),null)
         editor.putString(getString(R.string.user_role_key),null)
         editor.commit()
     }

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

     protected fun goToNewOrderActivity(){
         val intent = Intent(this, NewOrderActivity::class.java)
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
}
