package com.maskjs.korona_zakupy.ui.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.maskjs.korona_zakupy.R
import java.lang.StringBuilder


abstract class BaseFragment : Fragment() {
    protected var onBackPress : OnBackPress? = null
    protected lateinit var sharedPreferences : SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    protected lateinit var errorMessages: Map<String,String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setSharedPreferences()
        setErrorMessages()
        onBackPress = context as? OnBackPress

    }

    private fun setSharedPreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
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

    protected fun getNewOrderType(): String?{

        return sharedPreferences?.getString(R.string.new_order_type.toString(),"")
    }

    interface OnBackPress{
       fun leaveApp()
    }
}
