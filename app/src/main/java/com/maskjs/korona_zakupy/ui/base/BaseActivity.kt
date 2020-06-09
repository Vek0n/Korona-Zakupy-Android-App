package com.maskjs.korona_zakupy.ui.base

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.maskjs.korona_zakupy.R
import com.maskjs.korona_zakupy.ui.login.LoginActivity
import com.maskjs.korona_zakupy.ui.main.MainActivity
import com.maskjs.korona_zakupy.ui.new_order.NewOrderActivity
import com.maskjs.korona_zakupy.ui.person_in_quarantine.PersonInQuarantineActivity
import com.maskjs.korona_zakupy.ui.register.RegisterActivity
import com.maskjs.korona_zakupy.ui.volunteer.VolunteerActivity

abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var sharedPreferences : SharedPreferences
    protected lateinit var errorMessages : Map<String,String>
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSharedPreferences()
        setErrorMessages()
    }

    private fun setSharedPreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferencesEditor = sharedPreferences.edit()
    }

    private fun setErrorMessages(){
        errorMessages = mapOf(
            Pair("emptyError",getString(R.string.global_empty_field_error)),
            Pair("userNameIsAlreadyTaken",getString(R.string.reg_error_is_already_taken)),
            Pair("errorRegexMessage",getString(R.string.reg_error_password_regex)),
            Pair("notMatchError",getString(R.string.reg_error_password_match))
        )
    }


    protected fun setUserId(userId : String?){
        sharedPreferencesEditor.putString(R.string.user_id_key.toString(), userId)
        sharedPreferencesEditor.commit()
    }

    protected fun setUserToken(userToken: String?){
        sharedPreferencesEditor.putString(R.string.user_token_key.toString(),userToken)
        sharedPreferencesEditor.commit()
    }

    protected fun setUserRole(userRole: String?){
        sharedPreferencesEditor.putString(R.string.user_role_key.toString(),userRole)
        sharedPreferencesEditor.commit()
    }

    protected fun getUserId(): String?{
        return  sharedPreferences?.getString(R.string.user_id_key.toString(),"")
    }

    protected fun getUserToken(): String?{

        return  sharedPreferences?.getString(R.string.user_token_key.toString(),"")
    }

    protected fun getUserRole(): String?{
        return sharedPreferences?.getString(R.string.user_role_key.toString(),"")
    }

    protected fun getNewOrderType(): String?{

        return sharedPreferences?.getString(R.string.new_order_type.toString(),"")
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
